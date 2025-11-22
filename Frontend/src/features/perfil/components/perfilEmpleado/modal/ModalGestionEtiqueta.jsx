import { useState, useEffect } from "react";
import { Modal, Button, Form } from "react-bootstrap";

const ModalGestionEtiqueta = ({ show, handleClose, handleGuardar, etiqueta }) => {
    const [nombreEtiqueta, setNombreEtiqueta] = useState("");
    const [error, setError] = useState("");

    const modo = etiqueta ? "Editar" : "Crear";
    const titulo = `${modo} Etiqueta`;

    useEffect(() => {
        if (etiqueta) {
            setNombreEtiqueta(etiqueta.nombre);
        } else {
            setNombreEtiqueta("");
        }
        setError("");
    }, [etiqueta, show]);

    const validar = () => {
        if (!nombreEtiqueta.trim()) {
            setError("El nombre de la etiqueta no puede estar vacío.");
            return false;
        }

        if (etiqueta && nombreEtiqueta.trim() === etiqueta.nombre) {
            setError("Debe ingresar un nombre diferente al actual.");
            return false;
        }

        setError("");
        return true;
    };

    const onGuardar = () => {
        if (!validar()) return;

        handleGuardar(nombreEtiqueta.trim());

        handleClose();
    };

    return (
        <Modal show={show} onHide={handleClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>{titulo}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group controlId="formEtiquetaNombre">
                        <Form.Label>Nombre de la Etiqueta</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Ej: Oferta, Nuevo, Gamer..."
                            value={nombreEtiqueta}
                            onChange={(e) => setNombreEtiqueta(e.target.value)}
                            isInvalid={!!error}
                        />
                        {error && (
                            <Form.Control.Feedback type="invalid">
                                {error}
                            </Form.Control.Feedback>
                        )}
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Cancelar
                </Button>
                <Button variant="primary" onClick={onGuardar}>
                    {modo === "Crear" ? "Crear Etiqueta" : "Guardar Cambios"}
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ModalGestionEtiqueta;
