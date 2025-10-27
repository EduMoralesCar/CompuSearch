import { Modal, Button, ListGroup } from "react-bootstrap";

const ModalInfoUsuario = ({ show, handleClose, usuario }) => {

    // Capitaliza la primera letra de un string (ej: "nombre" -> "Nombre")
    const capitalizar = (str) => {
        if (!str) return '';
        return str.charAt(0).toUpperCase() + str.slice(1);
    };

    return (
        <Modal show={show} onHide={handleClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>
                    Información de {usuario ? usuario.nombre : "Usuario"}
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {usuario ? (
                    <ListGroup variant="flush">
                        {/* Iteramos sobre todas las propiedades del objeto usuario 
                        para mostrarlas dinámicamente
                        */}
                        {Object.entries(usuario).map(([key, value]) => (
                            <ListGroup.Item key={key}>
                                <strong>{capitalizar(key)}:</strong> {value.toString()}
                            </ListGroup.Item>
                        ))}
                    </ListGroup>
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