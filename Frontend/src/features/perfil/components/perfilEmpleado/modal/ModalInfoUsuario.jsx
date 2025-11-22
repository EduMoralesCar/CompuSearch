import { Modal, Button, ListGroup, Badge } from "react-bootstrap";

const ModalInfoUsuario = ({ show, handleClose, usuario }) => {

    if (!usuario) {
        return (
            <Modal show={show} onHide={handleClose} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Información de Usuario</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>No se pudo cargar la información del usuario.</p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Cerrar
                    </Button>
                </Modal.Footer>
            </Modal>
        );
    }

    // Determinar el nombre a mostrar
    const getNombre = () => {
        if (usuario.nombre && usuario.apellido) {
            return `${usuario.nombre} ${usuario.apellido}`;
        }
        if (usuario.nombre) return usuario.nombre;
        return usuario.username;
    };

    // Formatear fecha
    const formatearFecha = (fecha) => {
        if (!fecha) return 'N/A';
        return new Date(fecha).toLocaleDateString('es-ES', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    };

    // Determinar el tipo de usuario para mostrar información relevante
    const getTipoUsuario = () => {
        if (usuario.tipoUsuario === 'EMPLEADO' && usuario.rol === 'ADMIN') {
            return 'Administrador';
        }
        if (usuario.tipoUsuario === 'EMPLEADO') {
            return 'Empleado';
        }
        if (usuario.tipoUsuario === 'TIENDA') {
            return 'Tienda';
        }
        return 'Usuario';
    };

    return (
        <Modal show={show} onHide={handleClose} centered size="lg">
            <Modal.Header closeButton>
                <Modal.Title>
                    Información de {getNombre()}
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <ListGroup variant="flush">
                    {/* Información básica (para todos) */}
                    <ListGroup.Item>
                        <strong>Tipo:</strong> {getTipoUsuario()}
                        {usuario.rol && usuario.rol !== 'ADMIN' && (
                            <Badge bg="info" className="ms-2">{usuario.rol}</Badge>
                        )}
                    </ListGroup.Item>

                    <ListGroup.Item>
                        <strong>Username:</strong> {usuario.username}
                    </ListGroup.Item>

                    <ListGroup.Item>
                        <strong>Email:</strong> {usuario.email}
                    </ListGroup.Item>

                    <ListGroup.Item>
                        <strong>Estado:</strong>{' '}
                        <Badge bg={usuario.activo ? 'success' : 'danger'}>
                            {usuario.activo ? 'Activo' : 'Inactivo'}
                        </Badge>
                    </ListGroup.Item>

                    <ListGroup.Item>
                        <strong>Fecha de Registro:</strong> {formatearFecha(usuario.fechaRegistro)}
                    </ListGroup.Item>

                    {/* Información específica para Empleados/Admins */}
                    {(usuario.nombre || usuario.apellido) && (
                        <ListGroup.Item>
                            <strong>Nombre Completo:</strong> {getNombre()}
                        </ListGroup.Item>
                    )}

                    {/* Información específica para Tiendas */}
                    {usuario.tipoUsuario === 'TIENDA' && (
                        <>
                            {usuario.telefono && (
                                <ListGroup.Item>
                                    <strong>Teléfono:</strong> {usuario.telefono}
                                </ListGroup.Item>
                            )}
                            {usuario.direccion && (
                                <ListGroup.Item>
                                    <strong>Dirección:</strong> {usuario.direccion}
                                </ListGroup.Item>
                            )}
                            {usuario.verificado !== undefined && (
                                <ListGroup.Item>
                                    <strong>Verificado:</strong>{' '}
                                    <Badge bg={usuario.verificado ? 'success' : 'warning'}>
                                        {usuario.verificado ? 'Sí' : 'No'}
                                    </Badge>
                                </ListGroup.Item>
                            )}
                        </>
                    )}
                </ListGroup>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Cerrar
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ModalInfoUsuario;