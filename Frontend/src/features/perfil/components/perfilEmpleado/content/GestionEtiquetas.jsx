import { useState } from "react";
import { Card, Button, Table, Stack, Spinner, Alert } from "react-bootstrap";
import ModalGestionEtiqueta from "../modal/ModalGestionEtiqueta";
import ModalConfirmacion from "../../auxiliar/ModalConfirmacion";
import useEtiquetas from "../../../../navigation/hooks/useEtiquetas";

const GestionEtiquetas = () => {
    const {
        etiquetas,
        loading,
        error,
        crearEtiqueta,
        actualizarEtiqueta,
        eliminarEtiqueta,
    } = useEtiquetas();

    const [showModal, setShowModal] = useState(false);
    const [etiquetaSeleccionada, setEtiquetaSeleccionada] = useState(null);

    // 🧩 Estados para el modal de confirmación
    const [showConfirm, setShowConfirm] = useState(false);
    const [etiquetaAEliminar, setEtiquetaAEliminar] = useState(null);
    const [eliminando, setEliminando] = useState(false);

    // ✅ Estados para feedback
    const [mensaje, setMensaje] = useState("");
    const [tipoMensaje, setTipoMensaje] = useState("success");

    // Abrir modal en modo crear
    const handleCrear = () => {
        setEtiquetaSeleccionada(null);
        setMensaje("");
        setShowModal(true);
    };

    // Abrir modal en modo editar
    const handleEditar = (etiqueta) => {
        setEtiquetaSeleccionada(etiqueta);
        setMensaje("");
        setShowModal(true);
    };

    // Mostrar modal de confirmación
    const confirmarEliminar = (etiqueta) => {
        setEtiquetaAEliminar(etiqueta);
        setShowConfirm(true);
    };

    // Ejecutar eliminación
    const handleEliminarConfirmado = async () => {
        if (!etiquetaAEliminar) return;

        setEliminando(true);
        setMensaje("");
        try {
            await eliminarEtiqueta(etiquetaAEliminar.idEtiqueta);
            setTipoMensaje("success");
            setMensaje(`Etiqueta "${etiquetaAEliminar.nombre}" eliminada correctamente.`);
        } catch (err) {
            console.error("Error al eliminar:", err);
            setTipoMensaje("danger");
            setMensaje("Error al eliminar la etiqueta.");
        } finally {
            setEliminando(false);
            setShowConfirm(false);
            setEtiquetaAEliminar(null);
        }
    };

    // Cerrar modal principal
    const handleCloseModal = () => {
        setShowModal(false);
    };

    // Guardar desde el modal (crear o actualizar)
    const handleGuardar = async (nombreEtiqueta) => {
        try {
            if (etiquetaSeleccionada) {
                await actualizarEtiqueta(etiquetaSeleccionada.idEtiqueta, nombreEtiqueta);
                setTipoMensaje("success");
                setMensaje("Etiqueta actualizada correctamente.");
            } else {
                await crearEtiqueta(nombreEtiqueta);
                setTipoMensaje("success");
                setMensaje("Etiqueta creada correctamente.");
            }
        } catch (err) {
            console.error("Error al guardar etiqueta:", err);
            setTipoMensaje("danger");
            setMensaje("Error al guardar la etiqueta.");
        } finally {
            setShowModal(false);
        }
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
                    {loading && (
                        <div className="text-center">
                            <Spinner animation="border" role="status" />
                        </div>
                    )}

                    {/* ✅ Mostrar mensaje de éxito o error */}
                    {mensaje && <Alert variant={tipoMensaje}>{mensaje}</Alert>}

                    {/* Si hay error global del hook */}
                    {!mensaje && error && <Alert variant="danger">{error}</Alert>}

                    {!loading && etiquetas.length === 0 && (
                        <Alert variant="info">No hay etiquetas registradas.</Alert>
                    )}

                    {!loading && etiquetas.length > 0 && (
                        <Table striped bordered hover responsive>
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Nombre</th>
                                    <th style={{ width: "150px" }}>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                {etiquetas.map((et, index) => (
                                    <tr key={et.idEtiqueta}>
                                        <td>{index + 1}</td>
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
                                                    onClick={() => confirmarEliminar(et)}
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

            {/* Modal para crear/editar */}
            <ModalGestionEtiqueta
                show={showModal}
                handleClose={handleCloseModal}
                handleGuardar={handleGuardar}
                etiqueta={etiquetaSeleccionada}
            />

            {/* Modal de confirmación */}
            <ModalConfirmacion
                show={showConfirm}
                onHide={() => setShowConfirm(false)}
                titulo="Eliminar Etiqueta"
                mensaje={`¿Estás seguro de eliminar la etiqueta "${
                    etiquetaAEliminar?.nombre || ""
                }"? Se eliminará de las tiendas que la contengan.`}
                onConfirmar={handleEliminarConfirmado}
                textoConfirmar={eliminando ? "Eliminando..." : "Eliminar"}
                variantConfirmar="danger"
                disabled={eliminando}
            />
        </>
    );
};

export default GestionEtiquetas;
