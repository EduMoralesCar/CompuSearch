import { useState, useEffect } from "react";
import { Card, Button, Table, Stack, Spinner, Alert } from "react-bootstrap";
import { FiPlus } from "react-icons/fi";
import useEtiquetas from "../../../../navigation/hooks/useEtiquetas";
import ModalGestionEtiqueta from "../modal/ModalGestionEtiqueta";
import ModalConfirmacion from "../../auxiliar/ModalConfirmacion";
import HeaderBase from "../auxiliar/HeaderBase";
import PaginacionBase from "../auxiliar/PaginacionBase";

const PAGE_SIZE = 10;

const GestionEtiquetas = () => {
    const [currentPage, setCurrentPage] = useState(0);
    const [showModal, setShowModal] = useState(false);
    const [etiquetaSeleccionada, setEtiquetaSeleccionada] = useState(null);
    const [showConfirm, setShowConfirm] = useState(false);
    const [etiquetaAEliminar, setEtiquetaAEliminar] = useState(null);
    const [eliminando, setEliminando] = useState(false);
    const [mensaje, setMensaje] = useState("");
    const [tipoMensaje, setTipoMensaje] = useState("success");

    const {
        etiquetasPaginadas,
        totalPages,
        loading,
        error,
        cargarEtiquetasPaginadas,
        crearEtiqueta,
        actualizarEtiqueta,
        eliminarEtiqueta,
    } = useEtiquetas();

    // Cargar etiquetas al cambiar de página
    useEffect(() => {
        cargarEtiquetasPaginadas(currentPage, PAGE_SIZE, "nombre,asc");
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [currentPage]);

    // ---- HANDLERS MODALES ----
    const handleCrear = () => {
        setEtiquetaSeleccionada(null);
        setMensaje("");
        setShowModal(true);
    };

    const handleEditar = (etiqueta) => {
        setEtiquetaSeleccionada(etiqueta);
        setMensaje("");
        setShowModal(true);
    };

    const handleCloseModal = () => setShowModal(false);

    const handleGuardar = async (nombreEtiqueta) => {
        try {
            if (etiquetaSeleccionada) {
                await actualizarEtiqueta(etiquetaSeleccionada.idEtiqueta, nombreEtiqueta);
                setMensaje("Etiqueta actualizada correctamente.");
            } else {
                await crearEtiqueta(nombreEtiqueta);
                setMensaje("Etiqueta creada correctamente.");
            }
            setTipoMensaje("success");
            await cargarEtiquetasPaginadas(currentPage, PAGE_SIZE, "nombre,asc");
        // eslint-disable-next-line no-unused-vars
        } catch (err) {
            setMensaje("Error al guardar la etiqueta.");
            setTipoMensaje("danger");
        } finally {
            setShowModal(false);
        }
    };

    // ---- ELIMINAR ETIQUETA ----
    const confirmarEliminar = (etiqueta) => {
        setEtiquetaAEliminar(etiqueta);
        setShowConfirm(true);
    };

    const handleEliminarConfirmado = async () => {
        if (!etiquetaAEliminar) return;
        setEliminando(true);
        setMensaje("");

        try {
            await eliminarEtiqueta(etiquetaAEliminar.idEtiqueta);

            const newPage = etiquetasPaginadas.length === 1 && currentPage > 0 ? currentPage - 1 : currentPage;
            await cargarEtiquetasPaginadas(newPage, PAGE_SIZE, "nombre,asc");
            setCurrentPage(newPage);

            setMensaje(`Etiqueta "${etiquetaAEliminar.nombre}" eliminada correctamente.`);
            setTipoMensaje("success");
        // eslint-disable-next-line no-unused-vars
        } catch (err) {
            setMensaje("Error al eliminar la etiqueta.");
            setTipoMensaje("danger");
        } finally {
            setEliminando(false);
            setShowConfirm(false);
            setEtiquetaAEliminar(null);
        }
    };

    const getIndex = (index) => currentPage * PAGE_SIZE + index + 1;

    return (
        <>
            <Card className="shadow-lg border-0">
                <HeaderBase title="Gestión de Etiquetas">
                    <Button variant="success" onClick={handleCrear} disabled={loading}>
                        <FiPlus size={18} />
                    </Button>
                </HeaderBase>

                <Card.Body>
                    {loading && (
                        <div className="text-center">
                            <Spinner animation="border" role="status" />
                        </div>
                    )}

                    {mensaje && <Alert variant={tipoMensaje}>{mensaje}</Alert>}
                    {!mensaje && error && <Alert variant="danger">{error}</Alert>}
                    {!loading && etiquetasPaginadas.length === 0 && <Alert variant="info">No hay etiquetas registradas.</Alert>}

                    {!loading && etiquetasPaginadas.length > 0 && (
                        <Table striped bordered hover responsive>
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Nombre</th>
                                    <th style={{ width: "150px" }}>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                {etiquetasPaginadas.map((et, index) => (
                                    <tr key={et.idEtiqueta}>
                                        <td>{getIndex(index)}</td>
                                        <td>{et.nombre}</td>
                                        <td>
                                            <Stack direction="horizontal" gap={2}>
                                                <Button variant="warning" size="sm" onClick={() => handleEditar(et)}>
                                                    Editar
                                                </Button>
                                                <Button variant="danger" size="sm" onClick={() => confirmarEliminar(et)}>
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

                <Card.Footer>
                    {totalPages > 1 && (
                        <div className="d-flex justify-content-center mt-3 mb-4">
                            <PaginacionBase
                                page={currentPage}
                                totalPages={totalPages}
                                loading={loading}
                                onPageChange={(newPage) => {
                                    setCurrentPage(newPage);
                                    cargarEtiquetasPaginadas(newPage, PAGE_SIZE, "nombre,asc");
                                }}
                            />
                        </div>
                    )}
                </Card.Footer>
            </Card>

            <ModalGestionEtiqueta
                show={showModal}
                handleClose={handleCloseModal}
                handleGuardar={handleGuardar}
                etiqueta={etiquetaSeleccionada}
            />

            <ModalConfirmacion
                show={showConfirm}
                onHide={() => setShowConfirm(false)}
                titulo="Eliminar Etiqueta"
                mensaje={`¿Estás seguro de eliminar la etiqueta "${etiquetaAEliminar?.nombre || ""}"? Se eliminará de las tiendas que la contengan.`}
                onConfirmar={handleEliminarConfirmado}
                textoConfirmar={eliminando ? "Eliminando..." : "Eliminar"}
                variantConfirmar="danger"
                disabled={eliminando}
            />
        </>
    );
};

export default GestionEtiquetas;
