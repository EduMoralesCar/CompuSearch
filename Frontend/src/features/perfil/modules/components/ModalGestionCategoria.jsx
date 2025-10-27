import { useState, useEffect } from "react";
import { Modal, Button, Form } from "react-bootstrap";

const ModalGestionCategoria = ({ show, handleClose, categoria }) => {
    
    // Estado interno para el campo de texto del formulario
    const [nombreCategoria, setNombreCategoria] = useState("");

    // Determina si estamos en modo "Crear" o "Editar"
    const modo = categoria ? "Editar" : "Crear";
    const titulo = `${modo} Categoría`;

    // Efecto para actualizar el formulario cuando la categoría seleccionada cambia
    useEffect(() => {
        if (categoria) {
            // Si hay una categoría (modo Editar), llena el campo
            setNombreCategoria(categoria.nombre);
        } else {
            // Si no (modo Crear), vacía el campo
            setNombreCategoria("");
        }
    }, [categoria, show]); // Se ejecuta cuando 'categoria' o 'show' cambian

    // Función de guardado (solo visual - Jesus Chambea)
    const handleGuardar = () => {
        if (modo === "Crear") {
            console.log("(Visual) Creando categoría:", nombreCategoria);
        } else {
            console.log("(Visual) Editando categoría:", categoria.id, nombreCategoria);
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
                    <Form.Group controlId="formCategoriaNombre">
                        <Form.Label>Nombre de la Categoría</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Ej: Laptops, Teclados..."
                            value={nombreCategoria}
                            onChange={(e) => setNombreCategoria(e.target.value)}
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

export default ModalGestionCategoria;