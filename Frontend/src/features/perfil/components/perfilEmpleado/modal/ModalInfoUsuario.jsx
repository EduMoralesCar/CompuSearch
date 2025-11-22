import { Modal, Button, ListGroup, Badge } from "react-bootstrap";

const ModalInfoUsuario = ({ show, handleClose, usuario }) => {

    // Función auxiliar para formatear la fecha ISO a un formato local legible
    const formatFecha = (isoDate) => {
        if (!isoDate) return 'N/A';
        try {
            // Intentar formatear la fecha
            const date = new Date(isoDate);
            if (isNaN(date)) return 'Fecha inválida';
            
            return date.toLocaleDateString('es-ES', {
                year: 'numeric',
                month: 'long',
                day: 'numeric',
                hour: '2-digit',
                minute: '2-digit'
            });
        // eslint-disable-next-line no-unused-vars
        } catch (e) {
            return 'Error de formato de fecha';
        }
    };

    // Función auxiliar para renderizar el estado Activo
    const renderEstadoActivo = (activo) => {
        if (activo === true) {
            return <Badge bg="success">Activo (Sí)</Badge>;
        }
        if (activo === false) {
            return <Badge bg="danger">Inactivo (No)</Badge>;
        }
        return <Badge bg="secondary">Desconocido</Badge>;
    };

    return (
        <Modal show={show} onHide={handleClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>
                    Información Detallada de {usuario ? usuario.username : "Usuario"}
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {usuario ? (
                    <>
                        <ListGroup variant="flush" className="mb-4">
                            {/* Información principal fija */}
                            <ListGroup.Item><strong>ID Usuario:</strong> {usuario.idUsuario}</ListGroup.Item>
                            <ListGroup.Item><strong>Username:</strong> {usuario.username}</ListGroup.Item>
                            <ListGroup.Item><strong>Email:</strong> {usuario.email}</ListGroup.Item>
                            
                            {/* Estado Activo con Badge */}
                            <ListGroup.Item>
                                <strong>Estado:</strong> {renderEstadoActivo(usuario.activo)}
                            </ListGroup.Item>

                            {/* Fecha de Registro formateada */}
                            <ListGroup.Item>
                                <strong>Fecha de Registro:</strong> {formatFecha(usuario.fechaRegistro)}
                            </ListGroup.Item>
                        </ListGroup>

                        {/* Contadores agrupados */}
                        <h6>Actividad del Usuario</h6>
                        <ListGroup horizontal className="text-center">
                            <ListGroup.Item className="flex-fill">
                                <div><strong>Solicitudes</strong></div>
                                <h5><Badge bg="info">{usuario.cantidadSolicitudes || 0}</Badge></h5>
                            </ListGroup.Item>
                            <ListGroup.Item className="flex-fill">
                                <div><strong>Builds</strong></div>
                                <h5><Badge bg="primary">{usuario.cantidadBuilds || 0}</Badge></h5>
                            </ListGroup.Item>
                            <ListGroup.Item className="flex-fill">
                                <div><strong>Incidentes</strong></div>
                                <h5><Badge bg="danger">{usuario.cantidadIncidentes || 0}</Badge></h5>
                            </ListGroup.Item>
                        </ListGroup>
                    </>
                ) : (
                    <p>No se pudo cargar la información del usuario.</p>
                )}
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