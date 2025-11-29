import { useEffect, useState } from "react";
import {
    Container, Row, Col, Card, Button, Spinner,
    Alert, Modal, Pagination, Badge
} from "react-bootstrap";

import { usePlanes } from "../../../hooks/usePlanes";
import { useSuscripciones } from "../../../hooks/useSuscripciones";

const ObtenerPlan = ({ idTienda }) => {
    const {
        planes,
        obtenerTodosLosPlanes,
        loading: loadingPlanes,
        error: errorPlanes
    } = usePlanes();

    const {
        suscripcionActual,
        suscripciones,
        fetchSuscripcionesPaginadas,
        fetchSuscripcionActual,
        cambiarSuscripcion,
        crearSuscripcion,
        cancelarSuscripcion,
        loading: loadingSus,
        error: errorSus
    } = useSuscripciones();

    const [showModal, setShowModal] = useState(false);
    const [planSeleccionado, setPlanSeleccionado] = useState(null);
    const [procesando, setProcesando] = useState(false);
    const [errorModal, setErrorModal] = useState(null);
    const [pagina, setPagina] = useState(0);
    const [loadingCancel, setLoadingCancel] = useState(false);
    const [errorCancel, setErrorCancel] = useState(null);

    const abrirModal = (plan) => {
        setPlanSeleccionado(plan);
        setErrorModal(null);
        setShowModal(true);
    };

    const cerrarModal = () => {
        setPlanSeleccionado(null);
        setShowModal(false);
        setProcesando(false);
        setErrorModal(null);
    };

    useEffect(() => {
        obtenerTodosLosPlanes(0, 20, "", false);
        fetchSuscripcionActual(idTienda);
        fetchSuscripcionesPaginadas(idTienda, pagina, 10);
        // eslint-disable-next-line
    }, [idTienda, pagina]);

    const loading = loadingPlanes || loadingSus || loadingCancel;

    // Simulación de pago
    const simularPagoExitoso = async () => {
        if (!planSeleccionado) return;

        setProcesando(true);
        setErrorModal(null);

        try {
            if (!suscripcionActual) {
                await crearSuscripcion(planSeleccionado.idPlan, idTienda, true);
            } else {
                await cambiarSuscripcion(planSeleccionado.idPlan, idTienda, true);
            }
            cerrarModal();
            fetchSuscripcionActual(idTienda);
            fetchSuscripcionesPaginadas(idTienda, pagina, 10);
        } catch (err) {
            setErrorModal(err?.message || "Error inesperado durante la simulación.");
            setProcesando(false);
        }
    };

    const simularPagoFallido = () => {
        setErrorModal("❌ El pago ha fallado (simulación).");
    };

    // Cancelar suscripción (solo activas o pendientes)
    const handleCancelarSuscripcion = async () => {
        if (!suscripcionActual) return;
        setLoadingCancel(true);
        setErrorCancel(null);
        try {
            await cancelarSuscripcion(idTienda);
            fetchSuscripcionActual(idTienda);
            fetchSuscripcionesPaginadas(idTienda, pagina, 10);
        } catch (err) {
            setErrorCancel(err?.message || "Error al cancelar suscripción.");
        } finally {
            setLoadingCancel(false);
        }
    };

    // Función para asignar color según estado
    const getColorEstado = (estado) => {
        switch (estado) {
            case "ACTIVA": return "success";
            case "PENDIENTE": return "warning";
            case "CANCELADA":
            case "CANCELADA_PROGRAMADA": return "danger";
            case "CAMBIO_PROGRAMADO": return "info";
            case "TERMINADA": return "secondary";
            case "RECHAZADA": return "dark";
            default: return "primary";
        }
    };

    if (loading) {
        return (
            <Container className="py-5 text-center">
                <Spinner animation="border" />
                <p className="text-muted mt-2">Cargando información...</p>
            </Container>
        );
    }

    return (
        <Container className="py-4">
            <Row className="mb-4">
                <Col>
                    <h2 className="fw-bold text-primary mb-1">Planes Disponibles</h2>
                    <p className="text-muted mb-0">
                        Elige un plan para mejorar la visibilidad y funciones de tu tienda.
                    </p>
                </Col>
            </Row>

            {(errorPlanes || errorSus) && <Alert variant="danger">{errorPlanes || errorSus}</Alert>}

            {suscripcionActual ? (
                <>
                    {/* Suscripción Actual */}
                    {suscripcionActual.actual && (
                        <Card className="mb-4 shadow-sm border-primary">
                            <Card.Body>
                                <h5 className="fw-bold text-primary">Tu Suscripción Actual</h5>
                                <p className="mb-1"><strong>Plan:</strong> {suscripcionActual.actual.nombrePlan}</p>
                                <p className="mb-1"><strong>Precio:</strong> S/ {suscripcionActual.actual.precio}</p>
                                <p className="mb-1">
                                    <strong>Estado:</strong>{" "}
                                    <Badge bg={getColorEstado(suscripcionActual.actual.estado)}>
                                        {suscripcionActual.actual.estado}
                                    </Badge>
                                </p>
                                <p className="mb-1"><strong>Inicio:</strong> {new Date(suscripcionActual.actual.fechaInicio).toLocaleDateString()}</p>
                                <p className="mb-1"><strong>Fin:</strong> {new Date(suscripcionActual.actual.fechaFin).toLocaleDateString()}</p>

                                {(suscripcionActual.actual.estado === "ACTIVA" || suscripcionActual.actual.estado === "CAMBIO_PROGRAMADO") && (
                                    <Button
                                        variant="danger"
                                        className="mt-2"
                                        onClick={handleCancelarSuscripcion}
                                        disabled={loadingCancel}
                                    >
                                        {loadingCancel ? (
                                            <>
                                                <Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" />
                                                Cancelando...
                                            </>
                                        ) : (
                                            "Cancelar Suscripción"
                                        )}
                                    </Button>
                                )}

                                {errorCancel && <Alert variant="danger" className="mt-2">{errorCancel}</Alert>}
                            </Card.Body>
                        </Card>
                    )}

                    {/* Suscripción Pendiente */}
                    {suscripcionActual.pendiente && (
                        <Card className="mb-4 shadow-sm border-warning">
                            <Card.Body>
                                <h5 className="fw-bold text-warning">Plan Pendiente</h5>
                                <p className="mb-1"><strong>Plan:</strong> {suscripcionActual.pendiente.nombrePlan}</p>
                                <p className="mb-1"><strong>Precio:</strong> S/ {suscripcionActual.pendiente.precio}</p>
                                <p className="mb-1">
                                    <strong>Estado:</strong>{" "}
                                    <Badge bg={getColorEstado(suscripcionActual.pendiente.estado)}>
                                        {suscripcionActual.pendiente.estado}
                                    </Badge>
                                </p>
                                <p className="mb-1"><strong>Inicio:</strong> {new Date(suscripcionActual.pendiente.fechaInicio).toLocaleDateString()}</p>
                                <p className="mb-1"><strong>Fin:</strong> {new Date(suscripcionActual.pendiente.fechaFin).toLocaleDateString()}</p>
                            </Card.Body>
                        </Card>
                    )}
                </>
            ) : (
                <Alert variant="info" className="mb-4">
                    <strong>No tienes un plan activo.</strong> Selecciona uno para comenzar.
                </Alert>
            )}


            {/* ---------------- LISTA DE PLANES ---------------- */}
            <Row>
                {planes.length === 0 && (
                    <Col className="text-center text-muted py-4">
                        No se encontraron planes disponibles.
                    </Col>
                )}

                {planes.map((plan) => {
                    const esActual = suscripcionActual?.nombrePlan === plan.nombre;
                    const esMasCaro = suscripcionActual
                        ? Number(plan.precioMensual) > Number(suscripcionActual.precio)
                        : false;

                    return (
                        <Col md={4} key={plan.idPlan} className="mb-4">
                            <Card className="shadow-sm h-100">
                                <Card.Body>
                                    <Card.Title className="fw-bold text-primary">{plan.nombre}</Card.Title>
                                    <h4 className="fw-bold text-dark">
                                        S/ {plan.precioMensual} <span className="fs-6 text-muted">/mes</span>
                                    </h4>
                                    <p className="text-muted">{plan.descripcion}</p>
                                    <div className="mt-3">
                                        {esActual ? (
                                            <Button variant="secondary" disabled className="w-100">
                                                Plan Actual
                                            </Button>
                                        ) : (
                                            <Button
                                                variant={
                                                    !suscripcionActual
                                                        ? "success"
                                                        : esMasCaro
                                                            ? "primary"
                                                            : "outline-primary"
                                                }
                                                className="w-100"
                                                onClick={() => abrirModal(plan)}
                                            >
                                                {!suscripcionActual
                                                    ? "Obtener este Plan"
                                                    : esMasCaro
                                                        ? "Actualizar Plan"
                                                        : "Cambiar a este Plan"}
                                            </Button>
                                        )}
                                    </div>
                                </Card.Body>
                            </Card>
                        </Col>
                    );
                })}
            </Row>

            {/* ---------------- HISTORIAL DE SUSCRIPCIONES ---------------- */}
            {suscripciones && suscripciones.content.length > 0 && (
                <>
                    <h4 className="mt-5 mb-3 text-primary">Historial de Suscripciones</h4>
                    <Row>
                        {suscripciones.content.map((sub) => {
                            const puedeCancelar = sub.estado === "ACTIVA" || sub.estado === "CAMBIO_PROGRAMADO";

                            return (
                                <Col md={4} key={sub.idSuscripcion} className="mb-3">
                                    <Card className="shadow-sm h-100">
                                        <Card.Body>
                                            <p className="mb-1"><strong>Plan:</strong> {sub.nombrePlan}</p>
                                            <p className="mb-1"><strong>Precio:</strong> S/ {sub.precio}</p>
                                            <p className="mb-1">
                                                <strong>Estado:</strong>{" "}
                                                <Badge bg={getColorEstado(sub.estado)}>{sub.estado}</Badge>
                                            </p>
                                            <p className="mb-1"><strong>Inicio:</strong> {new Date(sub.fechaInicio).toLocaleDateString()}</p>
                                            <p className="mb-1"><strong>Fin:</strong> {new Date(sub.fechaFin).toLocaleDateString()}</p>

                                            {puedeCancelar && (
                                                <Button
                                                    variant="danger"
                                                    size="sm"
                                                    onClick={async () => {
                                                        setLoadingCancel(true);
                                                        setErrorCancel(null);
                                                        try {
                                                            await cancelarSuscripcion(idTienda);
                                                            fetchSuscripcionActual(idTienda);
                                                            fetchSuscripcionesPaginadas(idTienda, pagina, 10);
                                                        } catch (err) {
                                                            setErrorCancel(err?.message || "Error al cancelar suscripción.");
                                                        } finally {
                                                            setLoadingCancel(false);
                                                        }
                                                    }}
                                                    disabled={loadingCancel}
                                                >
                                                    {loadingCancel ? "Cancelando..." : "Cancelar"}
                                                </Button>
                                            )}
                                        </Card.Body>
                                    </Card>
                                </Col>
                            );
                        })}

                    </Row>

                    {suscripciones.totalElements > 10 && (
                        <Pagination>
                            {Array.from({ length: Math.ceil(suscripciones.totalElements / suscripciones.size) }, (_, i) => (
                                <Pagination.Item
                                    key={i}
                                    active={i === suscripciones.number}
                                    onClick={() => setPagina(i)}
                                >
                                    {i + 1}
                                </Pagination.Item>
                            ))}
                        </Pagination>
                    )}
                </>
            )}

            <Modal show={showModal} onHide={cerrarModal} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Simular Pago</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {errorModal && <Alert variant="danger">{errorModal}</Alert>}

                    <p>
                        Estás por {suscripcionActual ? "cambiar" : "obtener"} el plan:
                        <strong> {planSeleccionado?.nombre}</strong>
                    </p>

                    <p className="text-muted">
                        Selecciona un resultado de simulación para continuar.
                    </p>

                    <div className="d-flex flex-column gap-2">
                        <Button
                            variant="success"
                            onClick={simularPagoExitoso}
                            disabled={procesando}
                        >
                            {procesando ? (
                                <>
                                    <Spinner
                                        as="span"
                                        animation="border"
                                        size="sm"
                                        role="status"
                                        aria-hidden="true"
                                    /> Procesando...
                                </>
                            ) : (
                                "Simular Pago Exitoso"
                            )}
                        </Button>

                        <Button
                            variant="danger"
                            onClick={simularPagoFallido}
                            disabled={procesando}
                        >
                            Simular Pago Fallido
                        </Button>
                    </div>
                </Modal.Body>
            </Modal>
        </Container>
    );
};

export default ObtenerPlan;
