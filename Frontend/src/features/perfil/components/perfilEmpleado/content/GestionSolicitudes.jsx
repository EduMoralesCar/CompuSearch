import { useEffect, useState } from "react";
import {
    Card,
    Button,
    Stack,
    Row,
    Col,
    Spinner,
    Alert,
    Modal,
    Table,
    Pagination,
} from "react-bootstrap";
import { useSolicitudes } from "../../../hooks/useSolicitudes";

const PAGE_SIZE = 10;

const GestionSolicitudes = ({ idEmpleado }) => {
    const {
        respuesta: solicitudes,
        totalPages,
        loading,
        error,
        obtenerTodasSolicitudes,
        actualizarEstadoSolicitud,
    } = useSolicitudes();

    const [currentPage, setCurrentPage] = useState(0);

    const [showModal, setShowModal] = useState(false);
    const [solicitudSeleccionada, setSolicitudSeleccionada] = useState(null);
    const [feedbackMessage, setFeedbackMessage] = useState(null);
    const [feedbackType, setFeedbackType] = useState(null);

    useEffect(() => {
        obtenerTodasSolicitudes(currentPage, PAGE_SIZE);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [currentPage]);

    const mostrarMensaje = (mensaje, tipo = "success") => {
        setFeedbackMessage(mensaje);
        setFeedbackType(tipo);
        setTimeout(() => {
            setFeedbackMessage(null);
            setFeedbackType(null);
        }, 3000);
    };

    const recargarPagina = () => {
        obtenerTodasSolicitudes(currentPage, PAGE_SIZE);
    };

    const handleAceptar = async (idSolicitud) => {
        try {
            await actualizarEstadoSolicitud(idSolicitud, "APROBADA", idEmpleado);
            mostrarMensaje("Solicitud aprobada correctamente", "success");
            recargarPagina();
            // eslint-disable-next-line no-unused-vars
        } catch (err) {
            mostrarMensaje("Error al aprobar la solicitud", "danger");
        }
    };

    const handleRechazar = async (idSolicitud) => {
        try {
            await actualizarEstadoSolicitud(idSolicitud, "RECHAZADA", idEmpleado);
            mostrarMensaje("Solicitud rechazada correctamente", "success");
            recargarPagina();
            // eslint-disable-next-line no-unused-vars
        } catch (err) {
            mostrarMensaje("Error al rechazar la solicitud", "danger");
        }
    };

    const handleVerDetalles = (solicitud) => {
        setSolicitudSeleccionada(solicitud);
        setShowModal(true);
    };

    const formatKey = (key) => {
        const map = {
            ruc: "RUC",
            sitioWeb: "Sitio Web",
            telefono: "Teléfono",
            direccion: "Dirección",
            fotoLocal: "Foto del Local",
            descripcion: "Descripción",
            nombreTienda: "Nombre de la Tienda",
            emailContacto: "Email de Contacto",
            redesSociales: "Redes Sociales",
            aniosExperiencia: "Años de Experiencia",
            documentoIdentidad: "Documento de Identidad",
        };
        if (map[key]) return map[key];

        const result = key.replace(/([A-Z])/g, " $1");
        return result.charAt(0).toUpperCase() + result.slice(1);
    };

    const renderDatosFormulario = (datosFormulario) => {
        try {
            const parsed = datosFormulario;
            return (
                <Table striped bordered hover size="sm">
                    <tbody>
                        {Object.entries(parsed).map(([clave, valor]) => (
                            <tr key={clave}>
                                <td style={{ width: "30%" }}>
                                    <strong>{formatKey(clave)}</strong>
                                </td>
                                <td>{valor?.toString()}</td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            );
            // eslint-disable-next-line no-unused-vars
        } catch (e) {
            return <p>{datosFormulario}</p>;
        }
    };

    const renderPaginationItems = () => {
        const items = [];
        const maxPagesToShow = 5;

        let startPage = Math.max(0, currentPage - Math.floor(maxPagesToShow / 2));
        let endPage = Math.min(totalPages - 1, startPage + maxPagesToShow - 1);

        if (totalPages > maxPagesToShow && endPage - startPage + 1 < maxPagesToShow) {
            startPage = Math.max(0, endPage - maxPagesToShow + 1);
        }

        for (let number = startPage; number <= endPage; number++) {
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


    return (
        <Card className="shadow-lg border-0">
            <Card.Header as="h5" className="d-flex justify-content-between align-items-center bg-light text-primary">
                Gestión de Solicitudes
            </Card.Header>
            <Card.Body>
                {loading && (
                    <div className="text-center my-4">
                        <Spinner animation="border" role="status" />
                        <p className="mt-2">Cargando solicitudes...</p>
                    </div>
                )}

                {error && <Alert variant="danger">{error}</Alert>}

                {feedbackMessage && (
                    <Alert variant={feedbackType} className="text-center">
                        {feedbackMessage}
                    </Alert>
                )}

                {!loading && !error && solicitudes?.length === 0 && (
                    <p className="text-muted text-center">
                        No hay solicitudes pendientes en la página actual.
                    </p>
                )}

                <Row xs={1} md={2} className="g-4">
                    {solicitudes?.map((solicitud) => (
                        <Col key={solicitud.idSolicitudTienda}>
                            <Card className="h-100">
                                <Card.Header>
                                    <strong>Usuario:</strong>{" "}
                                    {solicitud.nombreUsuario}
                                </Card.Header>
                                <Card.Body className="d-flex flex-column">
                                    <Card.Text>
                                        <strong>Fecha:</strong>{" "}
                                        {new Date(
                                            solicitud.fechaSolicitud
                                        ).toLocaleString()}
                                        <br />
                                        <strong>Estado:</strong> {solicitud.estado}
                                        {solicitud.nombreEmpleado && (
                                            <>
                                                <br />
                                                <strong>Empleado:</strong>{" "}
                                                {solicitud.nombreEmpleado} (ID:{" "}
                                                {solicitud.idEmpleado})
                                            </>
                                        )}
                                    </Card.Text>

                                    <Stack
                                        direction="horizontal"
                                        gap={2}
                                        className="mt-auto"
                                    >
                                        <Button
                                            variant="primary"
                                            onClick={() =>
                                                handleVerDetalles(solicitud)
                                            }
                                        >
                                            Ver Detalles
                                        </Button>
                                        <Button
                                            variant="success"
                                            onClick={() =>
                                                handleAceptar(
                                                    solicitud.idSolicitudTienda
                                                )
                                            }
                                            disabled={
                                                solicitud.estado !== "OBSERVACION"
                                            }
                                        >
                                            Aceptar
                                        </Button>
                                        <Button
                                            variant="danger"
                                            onClick={() =>
                                                handleRechazar(
                                                    solicitud.idSolicitudTienda
                                                )
                                            }
                                            disabled={
                                                solicitud.estado !== "OBSERVACION"
                                            }
                                        >
                                            Rechazar
                                        </Button>
                                    </Stack>
                                </Card.Body>
                            </Card>
                        </Col>
                    ))}
                </Row>

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

                            {renderPaginationItems()}

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
            </Card.Body>

            <Modal
                show={showModal}
                onHide={() => setShowModal(false)}
                centered
                size="lg"
            >
                <Modal.Header closeButton>
                    <Modal.Title>Detalles de la Solicitud</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {solicitudSeleccionada ? (
                        <>
                            <p>
                                <strong>ID Solicitud:</strong>{" "}
                                {solicitudSeleccionada.idSolicitudTienda}
                                <br />
                                <strong>Usuario:</strong>{" "}
                                {solicitudSeleccionada.nombreUsuario}
                                <br />
                                <strong>Fecha:</strong>{" "}
                                {new Date(
                                    solicitudSeleccionada.fechaSolicitud
                                ).toLocaleString()}
                                <br />
                                <strong>Estado:</strong>{" "}
                                {solicitudSeleccionada.estado}
                                {solicitudSeleccionada.nombreEmpleado && (
                                    <>
                                        <br />
                                        <strong>Empleado:</strong>{" "}
                                        {solicitudSeleccionada.nombreEmpleado} (
                                        ID: {solicitudSeleccionada.idEmpleado})
                                    </>
                                )}
                            </p>
                            <hr />
                            <h6>Datos del Formulario</h6>
                            {renderDatosFormulario(
                                solicitudSeleccionada.datosFormulario
                            )}
                        </>
                    ) : (
                        <p>No se han encontrado datos.</p>
                    )}
                </Modal.Body>
                <Modal.Footer>
                    <Button
                        variant="secondary"
                        onClick={() => setShowModal(false)}
                    >
                        Cerrar
                    </Button>
                </Modal.Footer>
            </Modal>
        </Card>
    );
};

export default GestionSolicitudes;