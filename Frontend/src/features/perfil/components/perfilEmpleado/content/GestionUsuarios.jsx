import { useState, useEffect, useCallback } from "react";
import { Card, Table, Button, Stack, Alert, Spinner, Form, InputGroup } from "react-bootstrap";
import { FiRefreshCw } from "react-icons/fi";
import ModalInfoUsuario from "../modal/ModalInfoUsuario";
import { useUsuario } from "../../../hooks/useUsuario";
import TablaUsuarios from "../table/TablaUsuarios";

const GestionUsuarios = () => {
    const { obtenerUsuariosPaginados, actualizarEstadoActivo, loading, error } = useUsuario();

    const [usuarios, setUsuarios] = useState([]);
    const [isTogglingId, setIsTogglingId] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [usuarioSeleccionado, setUsuarioSeleccionado] = useState(null);
    const [gestionError, setGestionError] = useState(null);

    const [searchTerm, setSearchTerm] = useState('');

    const fetchUsuarios = useCallback(async (search) => {
        setGestionError(null);
        const response = await obtenerUsuariosPaginados(0, 50, search);
        if (response.success) {
            setUsuarios(response.data.content);
        } else {
            setGestionError(response.error || "Error al cargar la lista de usuarios.");
        }
    }, [obtenerUsuariosPaginados]);

    useEffect(() => {
        fetchUsuarios('');
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const handleInputChange = (e) => {
        setSearchTerm(e.target.value);
    };

    const handleSearchSubmit = () => {
        fetchUsuarios(searchTerm);
    };

    const handleClearSearch = () => {
        setSearchTerm('');
        fetchUsuarios('');
    };

    const handleToggleActivo = async (idUsuario, currentActivo) => {
        setIsTogglingId(idUsuario);
        setGestionError(null);

        const nuevoEstado = !currentActivo;
        const response = await actualizarEstadoActivo(idUsuario, nuevoEstado);

        if (response.success) {
            setUsuarios(prev =>
                prev.map(u => u.idUsuario === idUsuario ? { ...u, activo: nuevoEstado } : u)
            );
            console.log(`Usuario ID ${idUsuario} actualizado a activo=${nuevoEstado}`);
        } else {
            setGestionError(response.error || `Error al cambiar el estado activo del usuario ID ${idUsuario}.`);
        }

        setIsTogglingId(null);
    };

    const handleVerInfo = (usuario) => {
        setUsuarioSeleccionado(usuario);
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setUsuarioSeleccionado(null);
    };

    return (
        <>
            <Card className="shadow-lg border-0">
                <Card.Header
                    as="h5"
                    className="d-flex justify-content-between align-items-center bg-light text-primary"
                >
                    Gestión de Usuarios Clientes

                    <div className="d-flex align-items-center gap-3">
                        <Form.Control
                            type="text"
                            placeholder="Buscar por Username..."
                            value={searchTerm}
                            onChange={handleInputChange}
                            disabled={loading && usuarios.length === 0}
                            style={{ minWidth: "250px" }}
                        />

                        <Button
                            variant="primary"
                            disabled={loading}
                            onClick={handleSearchSubmit}
                            className="px-3"
                        >
                            {loading && isTogglingId === null ? (
                                <Spinner animation="border" size="sm" className="me-2" />
                            ) : 'Buscar'}
                        </Button>

                        <Button
                            variant="outline-secondary"
                            onClick={handleClearSearch}
                            disabled={loading || searchTerm === ''}
                            className="px-3"
                        >
                            Limpiar
                        </Button>

                        <Button
                            variant="outline-secondary"
                            onClick={handleSearchSubmit}
                            disabled={loading}
                            className="d-flex align-items-center justify-content-center"
                            style={{ width: "42px", height: "38px" }}
                        >
                            <FiRefreshCw size={18} />
                        </Button>

                    </div>
                </Card.Header>


                <Card.Body>


                    {(error || gestionError) && (
                        <Alert variant="danger">{error || gestionError}</Alert>
                    )}

                    {loading && usuarios.length === 0 && (
                        <div className="text-center p-5">
                            <Spinner animation="border" role="status" className="me-2" />
                            Cargando usuarios...
                        </div>
                    )}

                    {!(loading && usuarios.length === 0) && (
                        <TablaUsuarios
                            usuarios={usuarios}
                            onVerInfo={handleVerInfo}
                            onToggleActivo={handleToggleActivo}
                            isTogglingId={isTogglingId}
                        />
                    )}
                </Card.Body>
            </Card>

            <ModalInfoUsuario
                show={showModal}
                handleClose={handleCloseModal}
                usuario={usuarioSeleccionado}
            />
        </>
    );
};

export default GestionUsuarios;