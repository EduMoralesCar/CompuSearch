import { useState, useEffect } from "react";
import { Card, Tabs, Tab, Table, Button, Stack, Spinner, Alert, Modal } from "react-bootstrap";
import ModalInfoUsuario from "../modal/ModalInfoUsuario";
import { useUsuarios } from "../../../hooks/useUsuarios";

const GestionUsuarios = () => {
    const [activeTab, setActiveTab] = useState("USUARIO");
    const [page, setPage] = useState(0);
    const [showModal, setShowModal] = useState(false);
    const [usuarioSeleccionado, setUsuarioSeleccionado] = useState(null);
    const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);
    const [userToDelete, setUserToDelete] = useState(null);

    const {
        usuarios,
        loading,
        error,
        totalPages,
        obtenerUsuariosPorTipo,
        cambiarEstadoUsuario,
        eliminarUsuario,
    } = useUsuarios();

    // Cargar usuarios cuando cambia la pestaña o la página
    useEffect(() => {
        obtenerUsuariosPorTipo(activeTab, page, 10);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [activeTab, page]);

    const handleVerInfo = (usuario) => {
        setUsuarioSeleccionado(usuario);
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setUsuarioSeleccionado(null);
    };

    const handleDeshabilitar = async (usuario) => {
        try {
            const nuevoEstado = !usuario.activo;
            await cambiarEstadoUsuario(usuario.idUsuario, nuevoEstado);
            // Recargar la lista
            obtenerUsuariosPorTipo(activeTab, page, 10);
        } catch (err) {
            console.error("Error al cambiar estado:", err);
        }
    };

    const handleEliminarClick = (usuario) => {
        setUserToDelete(usuario);
        setShowDeleteConfirm(true);
    };

    const handleEliminarConfirm = async () => {
        if (!userToDelete) return;

        try {
            await eliminarUsuario(userToDelete.idUsuario);
            setShowDeleteConfirm(false);
            setUserToDelete(null);
            // Recargar la lista
            obtenerUsuariosPorTipo(activeTab, page, 10);
        } catch (err) {
            console.error("Error al eliminar usuario:", err);
        }
    };

    const handleTabChange = (tab) => {
        setActiveTab(tab);
        setPage(0); // Reset page when changing tabs
    };

    return (
        <>
            <Card>
                <Card.Header as="h5">Gestión de Usuarios</Card.Header>
                <Card.Body>
                    <Tabs
                        activeKey={activeTab}
                        onSelect={handleTabChange}
                        id="tabs-gestion-usuarios"
                        className="mb-3"
                        fill
                    >
                        <Tab eventKey="USUARIO" title="Usuarios">
                            {loading ? (
                                <div className="text-center py-5">
                                    <Spinner animation="border" variant="primary" />
                                </div>
                            ) : error ? (
                                <Alert variant="danger">{error}</Alert>
                            ) : (
                                <TablaUsuarios
                                    usuarios={usuarios}
                                    onVerInfo={handleVerInfo}
                                    onDeshabilitar={handleDeshabilitar}
                                    tipo="usuario"
                                />
                            )}
                        </Tab>

                        <Tab eventKey="EMPLEADO" title="Empleados">
                            {loading ? (
                                <div className="text-center py-5">
                                    <Spinner animation="border" variant="primary" />
                                </div>
                            ) : error ? (
                                <Alert variant="danger">{error}</Alert>
                            ) : (
                                <TablaUsuarios
                                    usuarios={usuarios}
                                    onVerInfo={handleVerInfo}
                                    onDeshabilitar={handleDeshabilitar}
                                    onEliminar={handleEliminarClick}
                                    tipo="empleado"
                                />
                            )}
                        </Tab>

                        <Tab eventKey="ADMIN" title="Administradores">
                            {loading ? (
                                <div className="text-center py-5">
                                    <Spinner animation="border" variant="primary" />
                                </div>
                            ) : error ? (
                                <Alert variant="danger">{error}</Alert>
                            ) : (
                                <TablaUsuarios
                                    usuarios={usuarios}
                                    onVerInfo={handleVerInfo}
                                    tipo="admin"
                                />
                            )}
                        </Tab>
                    </Tabs>

                    {/* Paginación */}
                    {totalPages > 1 && (
                        <div className="d-flex justify-content-between mt-3">
                            <Button
                                onClick={() => setPage(p => Math.max(0, p - 1))}
                                disabled={page === 0 || loading}
                            >
                                Anterior
                            </Button>
                            <span className="align-self-center">
                                Página {page + 1} de {totalPages}
                            </span>
                            <Button
                                onClick={() => setPage(p => Math.min(totalPages - 1, p + 1))}
                                disabled={page >= totalPages - 1 || loading}
                            >
                                Siguiente
                            </Button>
                        </div>
                    )}
                </Card.Body>
            </Card>

            {/* Modal para ver la info */}
            <ModalInfoUsuario
                show={showModal}
                handleClose={handleCloseModal}
                usuario={usuarioSeleccionado}
            />

            {/* Modal de confirmación de eliminación */}
            <Modal show={showDeleteConfirm} onHide={() => setShowDeleteConfirm(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Confirmar Eliminación</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    ¿Estás seguro de que deseas eliminar al usuario <strong>{userToDelete?.username}</strong>?
                    Esta acción no se puede deshacer.
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowDeleteConfirm(false)}>
                        Cancelar
                    </Button>
                    <Button variant="danger" onClick={handleEliminarConfirm}>
                        Eliminar
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
};

// Componente interno para renderizar las tablas
const TablaUsuarios = ({ usuarios, tipo, ...actions }) => {
    const { onVerInfo, onDeshabilitar, onEliminar } = actions;

    if (usuarios.length === 0) {
        return <Alert variant="info">No hay {tipo}s registrados.</Alert>;
    }

    return (
        <Table striped bordered hover responsive size="sm">
            <thead>
                <tr>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Estado</th>
                    <th>Fecha Registro</th>
                    <th style={{ width: "250px" }}>Acciones</th>
                </tr>
            </thead>
            <tbody>
                {usuarios.map((user) => (
                    <tr key={user.idUsuario}>
                        <td>{user.username}</td>
                        <td>{user.email}</td>
                        <td>
                            <span className={`badge ${user.activo ? 'bg-success' : 'bg-danger'}`}>
                                {user.activo ? 'Activo' : 'Inactivo'}
                            </span>
                        </td>
                        <td>{new Date(user.fechaRegistro).toLocaleDateString()}</td>
                        <td>
                            <Stack direction="horizontal" gap={2}>
                                {onVerInfo && (
                                    <Button variant="info" size="sm" onClick={() => onVerInfo(user)}>
                                        Info
                                    </Button>
                                )}
                                {onDeshabilitar && (
                                    <Button
                                        variant={user.activo ? "warning" : "success"}
                                        size="sm"
                                        onClick={() => onDeshabilitar(user)}
                                    >
                                        {user.activo ? 'Deshabilitar' : 'Habilitar'}
                                    </Button>
                                )}
                                {onEliminar && (
                                    <Button variant="danger" size="sm" onClick={() => onEliminar(user)}>
                                        Eliminar
                                    </Button>
                                )}
                            </Stack>
                        </td>
                    </tr>
                ))}
            </tbody>
        </Table>
    );
};

export default GestionUsuarios;