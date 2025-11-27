import { useState, useEffect } from "react";
import { Card, Button, Table, Stack, Spinner, Alert } from "react-bootstrap";
import { FiPlus } from "react-icons/fi";
import HeaderBase from "../auxiliar/HeaderBase";
import PaginacionBase from "../auxiliar/PaginacionBase";
import ModalGestionCategoria from "../modal/ModalGestionCategoria";
import ModalConfirmacion from "../../auxiliar/ModalConfirmacion";
import { useCategorias } from "../../../../navigation/hooks/useCategorias";

const PAGE_SIZE = 10;

const TablaCategorias = ({ categorias, onEditar, onEliminar }) => (
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
            {categorias.map((cat) => (
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
                                onClick={() => onEditar(cat)}
                            >
                                Editar
                            </Button>
                            <Button
                                variant="danger"
                                size="sm"
                                onClick={() => onEliminar(cat)}
                            >
                                Eliminar
                            </Button>
                        </Stack>
                    </td>
                </tr>
            ))}
        </tbody>
    </Table>
);

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

    // ---- HANDLERS ----
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
            const newPage = currentPage >= updatedTotalPages ? updatedTotalPages - 1 : currentPage;

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
            setTipoMensaje(result.success ? "success" : "danger");
            setMensaje(result.success ? "Categoría creada correctamente" : (result.error || "Error al crear la categoría"));
        } else {
            result = await actualizarCategoria(categoriaSeleccionada.idCategoria, categoriaData);
            setTipoMensaje(result.success ? "success" : "danger");
            setMensaje(result.success ? "Categoría actualizada correctamente" : (result.error || "Error al actualizar la categoría"));
        }
        setShowModal(false);
        obtenerCategoriasPaginadas(currentPage, PAGE_SIZE);
    };

    const content = categoriasPage ? categoriasPage.content : [];

    return (
        <>
            <Card className="shadow-lg border-0">
                <HeaderBase title="Gestión de Categorias">
                    <Button variant="success" onClick={handleCrear} disabled={loading}>
                        <FiPlus size={18} />
                    </Button>
                </HeaderBase>

                <Card.Body>
                    <div className="text-center">{loading && <Spinner animation="border" />}</div>

                    {mensaje && <Alert variant={tipoMensaje}>{mensaje}</Alert>}
                    {!mensaje && error && <Alert variant="danger">{error}</Alert>}

                    {!loading && categoriasPage && content.length === 0 && (
                        <Alert variant="info">No se encontraron categorías en la página actual.</Alert>
                    )}

                    {!loading && categoriasPage && content.length > 0 && (
                        <TablaCategorias
                            categorias={content}
                            onEditar={handleEditar}
                            onEliminar={confirmarEliminar}
                        />
                    )}
                </Card.Body>

                <Card.Footer>
                    <div className="d-flex justify-content-center">
                        <PaginacionBase
                            page={currentPage}
                            totalPages={categoriasPage?.totalPages || 1}
                            loading={loading}
                            onPageChange={(newPage) => {
                                setCurrentPage(newPage);
                                obtenerCategoriasPaginadas(newPage, PAGE_SIZE);
                            }}
                        />
                    </div>
                </Card.Footer>
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
                mensaje={`¿Estás seguro de eliminar la categoría "${categoriaAEliminar?.nombre || ""}"? Esta acción no se puede deshacer.`}
                onConfirmar={handleEliminar}
                textoConfirmar="Eliminar"
                variantConfirmar="danger"
            />
        </>
    );
};

export default GestionCategorias;
