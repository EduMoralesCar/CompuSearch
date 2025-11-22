import { Modal, ListGroup, Button, Stack, InputGroup } from "react-bootstrap";
import FormatDate from "../../../../../utils/FormatDate"

const ModalInfoEmpleado = ({ show, handleClose, empleado }) => {
    if (!empleado) {
        return null;
    }

    return (
        <Modal show={show} onHide={handleClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>Detalles del Empleado: {empleado.username}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <ListGroup variant="flush">
                    <ListGroup.Item><strong>ID Empleado:</strong> {empleado.idUsuario}</ListGroup.Item>
                    <ListGroup.Item><strong>Nombre Completo:</strong> {empleado.nombre} {empleado.apellido}</ListGroup.Item>
                    <ListGroup.Item><strong>Username:</strong> {empleado.username}</ListGroup.Item>
                    <ListGroup.Item><strong>Email:</strong> {empleado.email}</ListGroup.Item>
                    <ListGroup.Item><strong>Rol:</strong> <span className="badge bg-primary">{empleado.rol}</span></ListGroup.Item>
                    <ListGroup.Item>
                        <strong>Estado:</strong>
                        <span className={`badge bg-${empleado.activo ? 'success' : 'danger'} ms-2`}>
                            {empleado.activo ? 'Activo' : 'Inactivo'}
                        </span>
                    </ListGroup.Item>
                    <ListGroup.Item><strong>Fecha de Asignación:</strong> {FormatDate(empleado.fechaAsignacion)}</ListGroup.Item>
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

export default ModalInfoEmpleado;