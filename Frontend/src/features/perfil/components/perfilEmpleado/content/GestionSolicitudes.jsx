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
} from "react-bootstrap";
import { useSolicitudes } from "../../../hooks/useSolicitudes";

const GestionSolicitudes = ({ idEmpleado }) => {
    const {
        respuesta: solicitudes,
        totalPages,
        loading,
        error,
        obtenerTodasSolicitudes,
        actualizarEstadoSolicitud,
    } = useSolicitudes();

    const [showModal, setShowModal] = useState(false);
    const [solicitudSeleccionada, setSolicitudSeleccionada] = useState(null);
    const [feedbackMessage, setFeedbackMessage] = useState(null);
    const [feedbackType, setFeedbackType] = useState(null);

    // Cargar solicitudes al montar el componente
    useEffect(() => {
        obtenerTodasSolicitudes(0, 10);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const mostrarMensaje = (mensaje, tipo = "success") => {
        setFeedbackMessage(mensaje);
        setFeedbackType(tipo);
        setTimeout(() => {
            setFeedbackMessage(null);
            setFeedbackType(null);
        }, 3000);
    };

    const handleAceptar = async (idSolicitud) => {
        try {
            await actualizarEstadoSolicitud(idSolicitud, "APROBADA", idEmpleado);
            mostrarMensaje("Solicitud aprobada correctamente", "success");
            obtenerTodasSolicitudes(0, 10); // recarga después de aprobar
        // eslint-disable-next-line no-unused-vars
        } catch (err) {
            mostrarMensaje("Error al aprobar la solicitud", "danger");
        }
    };

    const handleRechazar = async (idSolicitud) => {
        try {
            await actualizarEstadoSolicitud(idSolicitud, "RECHAZADA", idEmpleado);
            mostrarMensaje("Solicitud rechazada correctamente", "success");
            obtenerTodasSolicitudes(0, 10); // recarga después de rechazar
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
            const parsed = JSON.parse(datosFormulario);
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

    return (
        <Card>
            <Card.Header as="h5">Gestión de Solicitudes</Card.Header>
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

                {!loading && !error && solicitudes.length === 0 && (
                    <p className="text-muted text-center">
                        No hay solicitudes pendientes.
                    </p>
                )}

                <Row xs={1} md={2} className="g-4">
                    {solicitudes.map((solicitud) => (
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
                        <Button
                            variant="secondary"
                            onClick={() => obtenerTodasSolicitudes(0, 10)}
                        >
                            Recargar
                        </Button>
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
