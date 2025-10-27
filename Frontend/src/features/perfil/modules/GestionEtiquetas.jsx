import { useState } from "react";
import { Card, Button, Table, Stack } from "react-bootstrap";
import ModalGestionEtiqueta from "./components/ModalGestionEtiqueta";

// Datos de ejemplo (Jesus borra esta webada)
const mockEtiquetas = [
    { id: 1, nombre: "Oferta" },
    { id: 2, nombre: "Nuevo" },
    { id: 3, nombre: "RGB" },
    { id: 4, nombre: "Gamer" },
    { id: 5, nombre: "Wireless" },
];

const GestionEtiquetas = () => {
    // Estado para controlar el modal
    const [showModal, setShowModal] = useState(false);
    
    // Estado para saber qué etiqueta estamos editando
    const [etiquetaSeleccionada, setEtiquetaSeleccionada] = useState(null);

    // Función para abrir el modal en modo "Crear"
    const handleCrear = () => {
        setEtiquetaSeleccionada(null);
        setShowModal(true);
    };

    // Función para abrir el modal en modo "Editar"
    const handleEditar = (etiqueta) => {
        setEtiquetaSeleccionada(etiqueta); // Pasamos la etiqueta al modal
        setShowModal(true);
    };

    // Función para el botón de eliminar (solo visual - Jesus Chambea)
    const handleEliminar = (id) => {
        alert(`(Visual) Se eliminaría la etiqueta con ID: ${id}`);
    };

    // Función para cerrar el modal
    const handleCloseModal = () => {
        setShowModal(false);
    };

    return (
        <>
            <Card>
                <Card.Header as="h5" className="d-flex justify-content-between align-items-center">
                    Gestión de Etiquetas
                    <Button variant="primary" onClick={handleCrear}>
                        Crear Nueva
                    </Button>
                </Card.Header>
                <Card.Body>
                    <Table striped bordered hover responsive>
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Nombre</th>
                                <th style={{ width: "150px" }}>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            {mockEtiquetas.map((et) => (
                                <tr key={et.id}>
                                    <td>{et.id}</td>
                                    <td>{et.nombre}</td>
                                    <td>
                                        <Stack direction="horizontal" gap={2}>
                                            <Button 
                                                variant="warning" 
                                                size="sm"
                                                onClick={() => handleEditar(et)}
                                            >
                                                Editar
                                            </Button>
                                            <Button 
                                                variant="danger" 
                                                size="sm"
                                                onClick={() => handleEliminar(et.id)}
                                            >
                                                Eliminar
                                            </Button>
                                        </Stack>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>
                </Card.Body>
            </Card>

            {/* El Modal: Le pasamos la función para cerrarlo y la etiqueta seleccionada. */}
            <ModalGestionEtiqueta 
                show={showModal}
                handleClose={handleCloseModal}
                etiqueta={etiquetaSeleccionada}
            />
        </>
    );
};

export default GestionEtiquetas;