import { useState, useEffect } from "react";
import { Card, Button, Table, Stack, Spinner, Alert, Pagination } from "react-bootstrap";
import ModalGestionEtiqueta from "../modal/ModalGestionEtiqueta";
import { FiPlus } from "react-icons/fi";
import ModalConfirmacion from "../../auxiliar/ModalConfirmacion";
import useEtiquetas from "../../../../navigation/hooks/useEtiquetas";

const PAGE_SIZE = 10;

const GestionEtiquetas = () => {
    const [currentPage, setCurrentPage] = useState(0);

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

    const [showModal, setShowModal] = useState(false);
    const [etiquetaSeleccionada, setEtiquetaSeleccionada] = useState(null);

    const [showConfirm, setShowConfirm] = useState(false);
    const [etiquetaAEliminar, setEtiquetaAEliminar] = useState(null);
    const [eliminando, setEliminando] = useState(false);

    const [mensaje, setMensaje] = useState("");
    const [tipoMensaje, setTipoMensaje] = useState("success");


    useEffect(() => {
        cargarEtiquetasPaginadas(currentPage, PAGE_SIZE, 'nombre,asc');
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [currentPage]);


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

            const newPage = etiquetasPaginadas.length === 1 && currentPage > 0
                ? currentPage - 1
                : currentPage;

            await cargarEtiquetasPaginadas(newPage, PAGE_SIZE, 'nombre,asc');
            setCurrentPage(newPage);

            setTipoMensaje("success");
            setMensaje(`Etiqueta "${etiquetaAEliminar.nombre}" eliminada correctamente.`);
            // eslint-disable-next-line no-unused-vars
        } catch (err) {
            setTipoMensaje("danger");
            setMensaje("Error al eliminar la etiqueta.");
        } finally {
            setEliminando(false);
            setShowConfirm(false);
            setEtiquetaAEliminar(null);
        }
    };

    const handleCloseModal = () => {
        setShowModal(false);
    };

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

            await cargarEtiquetasPaginadas(currentPage, PAGE_SIZE, 'nombre,asc');

            // eslint-disable-next-line no-unused-vars
        } catch (err) {
            setTipoMensaje("danger");
            setMensaje("Error al guardar la etiqueta.");
        } finally {
            setShowModal(false);
        }
    };

    const getIndex = (index) => currentPage * PAGE_SIZE + index + 1;

    return (
        <>
            <Card className="shadow-lg border-0">
                <Card.Header as="h5" className="d-flex justify-content-between align-items-center bg-light text-primary">                    Gestión de Etiquetas
                    <Button variant="success" onClick={handleCrear} disabled={loading}>
                        <FiPlus size={18} />
                    </Button>
                </Card.Header>

                <Card.Body>
                    {loading && (
                        <div className="text-center">
                            <Spinner animation="border" role="status" />
                        </div>
                    )}

                    {mensaje && <Alert variant={tipoMensaje}>{mensaje}</Alert>}

                    {!mensaje && error && <Alert variant="danger">{error}</Alert>}

                    {!loading && etiquetasPaginadas.length === 0 && (
                        <Alert variant="info">No hay etiquetas registradas.</Alert>
                    )}

                    {!loading && etiquetasPaginadas.length > 0 && (
                        <>
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
                        </>
                    )}
                </Card.Body>

                {totalPages > 1 && (
                                <div className="d-flex justify-content-center mt-4">
                                    <Pagination>
                                        <Pagination.First
                                            onClick={() => setCurrentPage(0)}
                                            disabled={currentPage === 0}
                                        />
                                        <Pagination.Prev
                                            onClick={() => setCurrentPage(currentPage - 1)}
                                            disabled={currentPage === 0}
                                        />

                                        {[...Array(totalPages)].map((_, index) => (
                                            <Pagination.Item
                                                key={index}
                                                active={index === currentPage}
                                                onClick={() => setCurrentPage(index)}
                                            >
                                                {index + 1}
                                            </Pagination.Item>
                                        ))}

                                        <Pagination.Next
                                            onClick={() => setCurrentPage(currentPage + 1)}
                                            disabled={currentPage === totalPages - 1}
                                        />
                                        <Pagination.Last
                                            onClick={() => setCurrentPage(totalPages - 1)}
                                            disabled={currentPage === totalPages - 1}
                                        />
                                    </Pagination>
                                </div>
                            )}
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
                mensaje={`¿Estás seguro de eliminar la etiqueta "${etiquetaAEliminar?.nombre || ""
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