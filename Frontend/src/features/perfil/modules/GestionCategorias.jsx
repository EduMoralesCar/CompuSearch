import { useState } from "react";
import { Card, Button, Table, Stack } from "react-bootstrap";
import ModalGestionCategoria from "./components/ModalGestionCategoria";

// Datos de ejemplo (backend se encargará de esto) - Borra esta webada Jesus
const mockCategorias = [
    { id: 1, nombre: "Laptops" },
    { id: 2, nombre: "Teclados" },
    { id: 3, nombre: "Monitores" },
    { id: 4, nombre: "Mouses" },
];

const GestionCategorias = () => {
    // Estado para controlar el modal
    const [showModal, setShowModal] = useState(false);
    
    // Estado para saber qué categoría estamos editando
    const [categoriaSeleccionada, setCategoriaSeleccionada] = useState(null);

    // Función para abrir el modal en modo "Crear"
    const handleCrear = () => {
        setCategoriaSeleccionada(null);
        setShowModal(true);
    };

    // Función para abrir el modal en modo "Editar"
    const handleEditar = (categoria) => {
        setCategoriaSeleccionada(categoria); 
        setShowModal(true);
    };

    // Función para el botón de eliminar (solo visual - Jesus Chambea)
    const handleEliminar = (id) => {
        // Lógica de backend iría aquí
        alert(`(Visual) Se eliminaría la categoría con ID: ${id}`);
    };

    // Función para cerrar el modal
    const handleCloseModal = () => {
        setShowModal(false);
    };

    return (
        <>
            <Card>
                <Card.Header as="h5" className="d-flex justify-content-between align-items-center">
                    Gestión de Categorías
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
                            {mockCategorias.map((cat) => (
                                <tr key={cat.id}>
                                    <td>{cat.id}</td>
                                    <td>{cat.nombre}</td>
                                    <td>
                                        <Stack direction="horizontal" gap={2}>
                                            <Button 
                                                variant="warning" 
                                                size="sm"
                                                onClick={() => handleEditar(cat)}
                                            >
                                                Editar
                                            </Button>
                                            <Button 
                                                variant="danger" 
                                                size="sm"
                                                onClick={() => handleEliminar(cat.id)}
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
            <ModalGestionCategoria 
                show={showModal}
                handleClose={handleCloseModal}
                categoria={categoriaSeleccionada}
            />
        </>
    );
};

export default GestionCategorias;