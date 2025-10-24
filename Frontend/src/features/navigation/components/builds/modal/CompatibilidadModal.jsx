import React from "react";
import { Modal, Button, ListGroup } from "react-bootstrap";

const CompatibilidadModal = ({ show, errores, onConfirmar, onCancelar }) => {
    return (
        <Modal show={show} onHide={onCancelar} centered>
            <Modal.Header closeButton>
                <Modal.Title>Compatibilidad detectada</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p className="fw-semibold text-danger">
                    Algunos productos no son compatibles:
                </p>
                <ListGroup variant="flush" className="mb-3">
                    {errores.map((error, index) => (
                        <ListGroup.Item key={index} className="text-danger">
                            {error}
                        </ListGroup.Item>
                    ))}
                </ListGroup>
                <p className="fw-semibold text-center">
                    ¿Deseas guardar igualmente?
                </p>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onCancelar}>
                    Cancelar
                </Button>
                <Button variant="danger" onClick={onConfirmar}>
                    Guardar de todas formas
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default CompatibilidadModal;
