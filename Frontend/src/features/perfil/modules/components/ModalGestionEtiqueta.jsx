import { useState, useEffect } from "react";
import { Modal, Button, Form } from "react-bootstrap";

const ModalGestionEtiqueta = ({ show, handleClose, etiqueta }) => {
    
    // Estado interno para el campo de texto del formulario
    const [nombreEtiqueta, setNombreEtiqueta] = useState("");

    // Determina si estamos en modo "Crear" o "Editar"
    const modo = etiqueta ? "Editar" : "Crear";
    const titulo = `${modo} Etiqueta`;

    // Efecto para actualizar el formulario cuando la etiqueta seleccionada cambia
    useEffect(() => {
        if (etiqueta) {
            // Si hay una etiqueta (modo Editar), llena el campo
            setNombreEtiqueta(etiqueta.nombre);
        } else {
            // Si no (modo Crear), vacía el campo
            setNombreEtiqueta("");
        }
    }, [etiqueta, show]); // Se ejecuta cuando 'etiqueta' o 'show' cambian

    // Función de guardado (solo visual- Jesus Chambea)
    const handleGuardar = () => {
        if (modo === "Crear") {
            console.log("(Visual) Creando etiqueta:", nombreEtiqueta);
        } else {
            console.log("(Visual) Editando etiqueta:", etiqueta.id, nombreEtiqueta);
        }
        
        handleClose(); // Cierra el modal después de guardar
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
                        />
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Cancelar
                </Button>
                <Button variant="primary" onClick={handleGuardar}>
                    Guardar Cambios
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ModalGestionEtiqueta;