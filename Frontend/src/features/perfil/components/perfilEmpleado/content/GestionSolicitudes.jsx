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
import HeaderBase from "../auxiliar/HeaderBase";
import PaginacionBase from "../auxiliar/PaginacionBase";

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
    const [feedback, setFeedback] = useState({ message: null, type: null });

    useEffect(() => {
        obtenerTodasSolicitudes(currentPage, PAGE_SIZE);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [currentPage]);

    const mostrarMensaje = (message, type = "success") => {
        setFeedback({ message, type });
        setTimeout(() => setFeedback({ message: null, type: null }), 3000);
    };

    const recargar = () => obtenerTodasSolicitudes(currentPage, PAGE_SIZE);

    const handleActualizarEstado = async (idSolicitud, estado) => {
        try {
            await actualizarEstadoSolicitud(idSolicitud, estado, idEmpleado);
            mostrarMensaje(`Solicitud ${estado.toLowerCase()} correctamente`, "success");
            recargar();
        } catch {
            mostrarMensaje(`Error al ${estado.toLowerCase()} la solicitud`, "danger");
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
        if (!datosFormulario || typeof datosFormulario !== "object") return <p>{datosFormulario}</p>;

        return (
            <Table striped bordered hover size="sm">
                <tbody>
                    {Object.entries(datosFormulario).map(([clave, valor]) => (
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
    };

    return (
        <Card className="shadow-lg border-0">
            <HeaderBase title="Gestión de Solicitudes">
                <div style={{ width: "40px", height: "37px" }}></div>
            </HeaderBase>

            <Card.Body>
                {loading && (
                    <div className="text-center my-4">
                        <Spinner animation="border" role="status" />
                        <p className="mt-2">Cargando solicitudes...</p>
                    </div>
                )}

                {error && <Alert variant="danger">{error}</Alert>}

                {feedback.message && (
                    <Alert variant={feedback.type} className="text-center">
                        {feedback.message}
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
                                    <strong>Usuario:</strong> {solicitud.nombreUsuario}
                                </Card.Header>
                                <Card.Body className="d-flex flex-column">
                                    <Card.Text>
                                        <strong>Fecha:</strong>{" "}
                                        {new Date(solicitud.fechaSolicitud).toLocaleString()}
                                        <br />
                                        <strong>Estado:</strong> {solicitud.estado}
                                        {solicitud.nombreEmpleado && (
                                            <>
                                                <br />
                                                <strong>Empleado:</strong>{" "}
                                                {solicitud.nombreEmpleado} (ID: {solicitud.idEmpleado})
                                            </>
                                        )}
                                    </Card.Text>

                                    <Stack direction="horizontal" gap={2} className="mt-auto">
                                        <Button
                                            variant="primary"
                                            onClick={() => handleVerDetalles(solicitud)}
                                        >
                                            Ver Detalles
                                        </Button>
                                        <Button
                                            variant="success"
                                            onClick={() =>
                                                handleActualizarEstado(solicitud.idSolicitudTienda, "APROBADA")
                                            }
                                            disabled={solicitud.estado !== "OBSERVACION"}
                                        >
                                            Aceptar
                                        </Button>
                                        <Button
                                            variant="danger"
                                            onClick={() =>
                                                handleActualizarEstado(solicitud.idSolicitudTienda, "RECHAZADA")
                                            }
                                            disabled={solicitud.estado !== "OBSERVACION"}
                                        >
                                            Rechazar
                                        </Button>
                                    </Stack>
                                </Card.Body>
                            </Card>
                        </Col>
                    ))}
                </Row>
            </Card.Body>

            <Card.Footer>
                <PaginacionBase
                    page={currentPage}
                    totalPages={totalPages}
                    loading={loading}
                    onPageChange={(newPage) => setCurrentPage(newPage)}
                />
            </Card.Footer>

            <Modal show={showModal} onHide={() => setShowModal(false)} centered size="lg">
                <Modal.Header closeButton>
                    <Modal.Title>Detalles de la Solicitud</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {solicitudSeleccionada ? (
                        <>
                            <p>
                                <strong>ID Solicitud:</strong> {solicitudSeleccionada.idSolicitudTienda}
                                <br />
                                <strong>Usuario:</strong> {solicitudSeleccionada.nombreUsuario}
                                <br />
                                <strong>Fecha:</strong>{" "}
                                {new Date(solicitudSeleccionada.fechaSolicitud).toLocaleString()}
                                <br />
                                <strong>Estado:</strong> {solicitudSeleccionada.estado}
                                {solicitudSeleccionada.nombreEmpleado && (
                                    <>
                                        <br />
                                        <strong>Empleado:</strong>{" "}
                                        {solicitudSeleccionada.nombreEmpleado} (ID: {solicitudSeleccionada.idEmpleado})
                                    </>
                                )}
                            </p>
                            <hr />
                            <h6>Datos del Formulario</h6>
                            {renderDatosFormulario(solicitudSeleccionada.datosFormulario)}
                        </>
                    ) : (
                        <p>No se han encontrado datos.</p>
                    )}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowModal(false)}>
                        Cerrar
                    </Button>
                </Modal.Footer>
            </Modal>
        </Card>
    );
};

export default GestionSolicitudes;
