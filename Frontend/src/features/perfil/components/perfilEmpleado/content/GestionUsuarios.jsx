import { useState, useEffect, useCallback } from "react";
import { Card, Button, Alert, Spinner, FormControl, InputGroup } from "react-bootstrap";
import { FiRefreshCw } from "react-icons/fi";
import ModalInfoUsuario from "../modal/ModalInfoUsuario";
import { useUsuario } from "../../../hooks/useUsuario";
import TablaUsuarios from "../table/TablaUsuarios";
import HeaderBase from "../auxiliar/HeaderBase";
import PaginacionBase from "../auxiliar/PaginacionBase";

const PAGE_SIZE = 25;

const GestionUsuarios = () => {
    const { obtenerUsuariosPaginados, actualizarEstadoActivo, loading, error } = useUsuario();

    const [usuarios, setUsuarios] = useState([]);
    const [isTogglingId, setIsTogglingId] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [usuarioSeleccionado, setUsuarioSeleccionado] = useState(null);
    const [gestionError, setGestionError] = useState(null);

    const [searchTerm, setSearchTerm] = useState('');
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);

    // 🔹 Fetch usuarios paginados
    const fetchUsuarios = useCallback(async (search = '', page = 0) => {
        setGestionError(null);
        const response = await obtenerUsuariosPaginados(page, PAGE_SIZE, search);

        if (response.success) {
            setUsuarios(response.data.content);
            setTotalPages(response.data.totalPages);
            setCurrentPage(page);
        } else {
            setGestionError(response.error || "Error al cargar la lista de usuarios.");
        }
    }, [obtenerUsuariosPaginados]);

    useEffect(() => {
        fetchUsuarios();
    }, [fetchUsuarios]);

    // 🔹 Handlers de búsqueda
    const handleInputChange = (e) => setSearchTerm(e.target.value);
    const handleSearchSubmit = () => fetchUsuarios(searchTerm, 0);
    const handleClearSearch = () => {
        setSearchTerm('');
        fetchUsuarios('', 0);
    };

    // 🔹 Toggle activo/inactivo
    const handleToggleActivo = async (idUsuario, currentActivo) => {
        setIsTogglingId(idUsuario);
        setGestionError(null);

        const nuevoEstado = !currentActivo;
        const response = await actualizarEstadoActivo(idUsuario, nuevoEstado);

        if (response.success) {
            setUsuarios(prev =>
                prev.map(u => u.idUsuario === idUsuario ? { ...u, activo: nuevoEstado } : u)
            );
        } else {
            setGestionError(response.error || `Error al cambiar el estado del usuario.`);
        }

        setIsTogglingId(null);
    };

    // 🔹 Modal de info de usuario
    const handleVerInfo = (usuario) => {
        setUsuarioSeleccionado(usuario);
        setShowModal(true);
    };
    const handleCloseModal = () => {
        setShowModal(false);
        setUsuarioSeleccionado(null);
    };

    const isGlobalLoading = loading && usuarios.length === 0;

    return (
        <>
            <Card className="shadow-lg border-0">
                <HeaderBase title="Gestión de Usuarios">
                    <InputGroup>
                        <FormControl
                            type="text"
                            placeholder="Buscar por Nombre"
                            value={searchTerm}
                            onChange={handleInputChange}
                            disabled={isGlobalLoading}
                        />
                        <Button variant="primary" onClick={handleSearchSubmit} disabled={loading}>
                            {loading ? <Spinner animation="border" size="sm" /> : 'Buscar'}
                        </Button>
                        <Button
                            variant="outline-secondary"
                            onClick={handleClearSearch}
                            disabled={loading || searchTerm === ''}
                        >
                            Limpiar
                        </Button>
                    </InputGroup>

                    <Button
                        variant="outline-secondary"
                        onClick={() => fetchUsuarios(searchTerm, currentPage)}
                        disabled={loading}
                        className="d-flex align-items-center justify-content-center"
                        style={{ width: "42px", height: "38px" }}
                    >
                        <FiRefreshCw size={18} />
                    </Button>
                </HeaderBase>

                <Card.Body>
                    {(error || gestionError) && <Alert variant="danger">{error || gestionError}</Alert>}

                    {isGlobalLoading && (
                        <div className="text-center p-5">
                            <Spinner animation="border" role="status" className="me-2" />
                            Cargando usuarios...
                        </div>
                    )}

                    {!isGlobalLoading && (
                        <>
                            <TablaUsuarios
                                usuarios={usuarios}
                                onVerInfo={handleVerInfo}
                                onToggleActivo={handleToggleActivo}
                                isTogglingId={isTogglingId}
                            />
                            {totalPages > 1 && (
                                <div className="d-flex justify-content-center mt-4">
                                    <PaginacionBase
                                        page={currentPage}
                                        totalPages={totalPages}
                                        loading={loading}
                                        onPageChange={(newPage) => fetchUsuarios(searchTerm, newPage)}
                                    />
                                </div>
                            )}
                        </>
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
