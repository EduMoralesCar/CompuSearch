import { useState, useEffect } from "react";
import { Card, Button, Table, Stack, Spinner, Alert, Pagination } from "react-bootstrap";
import ModalGestionCategoria from "../modal/ModalGestionCategoria";
import { FiPlus } from "react-icons/fi";
import ModalConfirmacion from "../../auxiliar/ModalConfirmacion";
import { useCategorias } from "../../../../navigation/hooks/useCategorias";

const PAGE_SIZE = 10;

const GestionCategorias = () => {
    const {
        categoriasPage,
        obtenerCategoriasPaginadas,
        eliminarCategoria,
        actualizarCategoria,
        crearCategoria,
        loading,
        error
    } = useCategorias();

    const [currentPage, setCurrentPage] = useState(0);

    const [showModal, setShowModal] = useState(false);
    const [categoriaSeleccionada, setCategoriaSeleccionada] = useState(null);
    const [mensaje, setMensaje] = useState("");
    const [tipoMensaje, setTipoMensaje] = useState("success");

    const [showConfirm, setShowConfirm] = useState(false);
    const [categoriaAEliminar, setCategoriaAEliminar] = useState(null);

    useEffect(() => {
        obtenerCategoriasPaginadas(currentPage, PAGE_SIZE);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [currentPage]);

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

            const remainingElements = categoriasPage.totalElements - 1;

            const updatedTotalPages = Math.max(1, Math.ceil(remainingElements / PAGE_SIZE));

            const newPage = currentPage >= updatedTotalPages
                ? updatedTotalPages - 1
                : currentPage;

            obtenerCategoriasPaginadas(newPage, PAGE_SIZE);

            setCurrentPage(newPage);

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

        obtenerCategoriasPaginadas(currentPage, PAGE_SIZE);
    };

    const renderPaginationItems = () => {
        if (!categoriasPage) return null;

        const items = [];
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

    const content = categoriasPage ? categoriasPage.content : [];

    return (
        <>
            <Card className="shadow-lg border-0">
                <Card.Header as="h5" className="d-flex justify-content-between align-items-center bg-light text-primary">
                    Gestión de Categorías
                    <Button variant="success" onClick={handleCrear} disabled={loading}>
                        <FiPlus size={18} />
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

                            {categoriasPage.totalPages > 1 && (
                                <div className="d-flex justify-content-center">
                                    <Pagination>
                                        <Pagination.First onClick={() => setCurrentPage(0)} disabled={categoriasPage.first} />
                                        <Pagination.Prev onClick={() => setCurrentPage(currentPage - 1)} disabled={categoriasPage.first} />

                                        {renderPaginationItems()}

                                        <Pagination.Next onClick={() => setCurrentPage(currentPage + 1)} disabled={categoriasPage.last} />
                                        <Pagination.Last onClick={() => setCurrentPage(categoriasPage.totalPages - 1)} disabled={categoriasPage.last} />
                                    </Pagination>
                                </div>
                            )}
                        </>
                    )}
                </Card.Body>
            </Card>

            <ModalGestionCategoria
                show={showModal}
                handleClose={handleCloseModal}
                categoria={categoriaSeleccionada}
                onSubmit={handleSubmit}
            />

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