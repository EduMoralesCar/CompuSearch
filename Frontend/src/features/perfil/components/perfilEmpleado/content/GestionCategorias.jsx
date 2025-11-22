import { useState } from "react";
import { Card, Button, Table, Stack, Spinner, Alert } from "react-bootstrap";
import ModalGestionCategoria from "../modal/ModalGestionCategoria";
import ModalConfirmacion from "../../auxiliar/ModalConfirmacion";
import { useCategorias } from "../../../../navigation/hooks/useCategorias";

const GestionCategorias = () => {
    const { categorias, eliminarCategoria, actualizarCategoria, crearCategoria, loading, error } = useCategorias();

    const [showModal, setShowModal] = useState(false);
    const [categoriaSeleccionada, setCategoriaSeleccionada] = useState(null);
    const [mensaje, setMensaje] = useState("");
    const [tipoMensaje, setTipoMensaje] = useState("success");

    // 👉 Estados para el modal de confirmación
    const [showConfirm, setShowConfirm] = useState(false);
    const [categoriaAEliminar, setCategoriaAEliminar] = useState(null);

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

    // 👉 Abre el modal de confirmación
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
                    <div className="text-center">
                        {loading && <Spinner animation="border" />}
                    </div>

                    {mensaje && <Alert variant={tipoMensaje}>{mensaje}</Alert>}
                    {!mensaje && error && <Alert variant="danger">{error}</Alert>}

                    {!loading && (
                        <Table striped bordered hover responsive>
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

            {/* ✅ Modal de confirmación reutilizable */}
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
