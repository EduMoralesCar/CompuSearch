import { useState, useEffect } from "react";
import { Card, Button, Table, Stack, Spinner, Alert, Pagination } from "react-bootstrap";
import ModalGestionCategoria from "../modal/ModalGestionCategoria";
import ModalConfirmacion from "../../auxiliar/ModalConfirmacion";
import { useCategorias } from "../../../../navigation/hooks/useCategorias";

// ⚠️ Constantes para la paginación
const PAGE_SIZE = 10; // Define el tamaño de la página

const GestionCategorias = () => {
    // 1. IMPORTAR la data paginada y el método
    const {
        categoriasPage, // <-- Data paginada (objeto Page de Spring)
        obtenerCategoriasPaginadas, // <-- Nuevo método
        eliminarCategoria,
        actualizarCategoria,
        crearCategoria,
        loading,
        error
    } = useCategorias();

    // 2. ESTADOS DE PAGINACIÓN
    const [currentPage, setCurrentPage] = useState(0); // Página actual (inicia en 0)

    const [showModal, setShowModal] = useState(false);
    const [categoriaSeleccionada, setCategoriaSeleccionada] = useState(null);
    const [mensaje, setMensaje] = useState("");
    const [tipoMensaje, setTipoMensaje] = useState("success");

    // Estados para el modal de confirmación
    const [showConfirm, setShowConfirm] = useState(false);
    const [categoriaAEliminar, setCategoriaAEliminar] = useState(null);

    // 3. EFECTO PARA CARGAR DATA PAGINADA
    useEffect(() => {
        // Al cargar el componente o cambiar la página, se llama al nuevo método
        obtenerCategoriasPaginadas(currentPage, PAGE_SIZE);
    }, [currentPage]); // Se vuelve a ejecutar cuando cambia la página actual

    // HANDLERS

    const handleCrear = () => {
        setCategoriaSeleccionada(null);
        setMensaje("");
        setShowModal(true);
    };

    const handleEditar = (categoria) => {
        setCategoriaSeleccionada(categoria);
        setMensaje("");
        setShowModal(true);
    };

    // Abre el modal de confirmación
    const confirmarEliminar = (categoria) => {
        setCategoriaAEliminar(categoria);
        setShowConfirm(true);
    };

    const handleEliminar = async () => {
        if (!categoriaAEliminar) return;

        const result = await eliminarCategoria(categoriaAEliminar.idCategoria);
        if (result.success) {
            setTipoMensaje("success");
            setMensaje("Categoría eliminada correctamente");

            // IMPORTANTE: Tras eliminar, recarga la página actual para reflejar el cambio.
            // Esto también maneja si la última categoría de una página fue eliminada.
            obtenerCategoriasPaginadas(currentPage, PAGE_SIZE);
        } else {
            setTipoMensaje("danger");
            setMensaje(result.error || "Error al eliminar la categoría");
        }

        setShowConfirm(false);
        setCategoriaAEliminar(null);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setMensaje("");
    };

    const handleSubmit = async (categoriaData) => {
        let result;
        if (!categoriaSeleccionada) {
            result = await crearCategoria(categoriaData);
            if (result.success) {
                setTipoMensaje("success");
                setMensaje("Categoría creada correctamente");
            } else {
                setTipoMensaje("danger");
                setMensaje(result.error || "Error al crear la categoría");
            }
        } else {
            result = await actualizarCategoria(categoriaSeleccionada.idCategoria, categoriaData);
            if (result.success) {
                setTipoMensaje("success");
                setMensaje("Categoría actualizada correctamente");
            } else {
                setTipoMensaje("danger");
                setMensaje(result.error || "Error al actualizar la categoría");
            }
        }
        setShowModal(false);

        // IMPORTANTE: Tras crear/actualizar, recarga la página actual para ver el resultado.
        obtenerCategoriasPaginadas(currentPage, PAGE_SIZE);
    };

    // ----------------------------------------------------------------------
    // 4. Lógica de Paginación (Renderizado)
    // ----------------------------------------------------------------------
    const renderPaginationItems = () => {
        if (!categoriasPage) return null;

        const items = [];
        // Muestra los botones de página del 1 al totalPages
        for (let number = 0; number < categoriasPage.totalPages; number++) {
            items.push(
                <Pagination.Item
                    key={number}
                    active={number === currentPage}
                    onClick={() => setCurrentPage(number)}
                >
                    {number + 1}
                </Pagination.Item>
            );
        }
        return items;
    };

    // Muestra el contenido (la lista de categorías) si la data paginada existe
    const content = categoriasPage ? categoriasPage.content : [];

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
                    <div className="text-center">
                        {loading && <Spinner animation="border" />}
                    </div>

                    {mensaje && <Alert variant={tipoMensaje}>{mensaje}</Alert>}
                    {!mensaje && error && <Alert variant="danger">{error}</Alert>}

                    {!loading && categoriasPage && categoriasPage.content.length === 0 && (
                        <Alert variant="info">No se encontraron categorías en la página actual.</Alert>
                    )}

                    {!loading && categoriasPage && content.length > 0 && (
                        <>
                            <Table striped bordered hover responsive className="mb-4">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Nombre</th>
                                        <th>Descripción</th>
                                        <th>Ruta de Imagen</th>
                                        <th style={{ width: "150px" }}>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {/* 5. USAR EL CONTENIDO PAGINADO */}
                                    {content.map((cat) => (
                                        <tr key={cat.idCategoria}>
                                            <td>{cat.idCategoria}</td>
                                            <td>{cat.nombre}</td>
                                            <td>{cat.descripcion}</td>
                                            <td>{cat.nombreImagen}</td>
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
                                                        onClick={() => confirmarEliminar(cat)}
                                                    >
                                                        Eliminar
                                                    </Button>
                                                </Stack>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </Table>

                            {/* COMPONENTE DE PAGINACIÓN */}
                            <div className="d-flex justify-content-center">
                                <Pagination>
                                    <Pagination.First onClick={() => setCurrentPage(0)} disabled={categoriasPage.first} />
                                    <Pagination.Prev onClick={() => setCurrentPage(currentPage - 1)} disabled={categoriasPage.first} />

                                    {renderPaginationItems()} {/* Renderiza los números de página */}

                                    <Pagination.Next onClick={() => setCurrentPage(currentPage + 1)} disabled={categoriasPage.last} />
                                    <Pagination.Last onClick={() => setCurrentPage(categoriasPage.totalPages - 1)} disabled={categoriasPage.last} />
                                </Pagination>
                            </div>
                        </>
                    )}
                </Card.Body>
            </Card>

            {/* Modal de gestión */}
            <ModalGestionCategoria
                show={showModal}
                handleClose={handleCloseModal}
                categoria={categoriaSeleccionada}
                onSubmit={handleSubmit}
            />

            {/* Modal de confirmación reutilizable */}
            <ModalConfirmacion
                show={showConfirm}
                onHide={() => setShowConfirm(false)}
                titulo="Eliminar Categoría"
                mensaje={`¿Estás seguro de eliminar la categoría "${categoriaAEliminar?.nombre || ""
                    }"? Esta acción no se puede deshacer.`}
                onConfirmar={handleEliminar}
                textoConfirmar="Eliminar"
                variantConfirmar="danger"
            />
        </>
    );
};

export default GestionCategorias;