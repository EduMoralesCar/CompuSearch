import { useEffect, useState } from "react";
import { useTiendas } from "../../../hooks/useTiendas";
import Tienda_Sin_Logo from "../../../../../assets/logo/Tienda_Sin_Logo.jpg";
import {
    Container,
    Row,
    Col,
    Card,
    ListGroup,
    Badge,
    Image,
    Spinner,
    Alert
} from "react-bootstrap";

const TiendaDashboard = ({ idTienda }) => {

    const { ObtenerTiendaDashboard, loading, error } = useTiendas();
    const [dashboard, setDashboard] = useState(null);

    const defaultLogo = Tienda_Sin_Logo;

    useEffect(() => {
        const cargarDashboard = async () => {
            const res = await ObtenerTiendaDashboard(idTienda);
            if (res.success) setDashboard(res.data);
        };

        if (idTienda) cargarDashboard();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [idTienda]);

    const obtenerLogo = () => {
        if (!dashboard) return defaultLogo;
        const logo = dashboard.logoBase64;
        return !logo || logo === "" ? defaultLogo : `data:image/jpeg;base64,${logo}`;
    };

    return (
        <Container className="py-4">

            {/* TÍTULO PRINCIPAL */}
            <Row className="mb-4">
                <Col>
                    <h2 className="fw-bold text-primary mb-1">
                        Dashboard de Tienda
                    </h2>
                    <p className="text-muted mb-0">
                        Información general, métricas, plan y configuración de tu tienda.
                    </p>
                </Col>
            </Row>

            {/* Loader */}
            {loading && (
                <div className="d-flex justify-content-center my-4">
                    <Spinner animation="border" role="status" />
                </div>
            )}

            {/* Error */}
            {error && <Alert variant="danger">{error}</Alert>}

            {dashboard && (
                <Row className="g-4">

                    {/* CARD PRINCIPAL */}
                    <Col md={4}>
                        <Card className="shadow-sm text-center h-100">
                            <Card.Body>
                                <Image
                                    src={obtenerLogo()}
                                    alt="Logo Tienda"
                                    roundedCircle
                                    width={140}
                                    height={140}
                                    className="object-fit-cover mb-3 border"
                                />
                                <Card.Title className="fw-bold fs-4">{dashboard.nombre}</Card.Title>
                                <Card.Text className="text-muted">{dashboard.descripcion}</Card.Text>

                                {dashboard.verificado ? (
                                    <Badge bg="success" className="px-3 py-2">Verificado</Badge>
                                ) : (
                                    <Badge bg="secondary" className="px-3 py-2">No verificado</Badge>
                                )}
                            </Card.Body>

                            <ListGroup variant="flush">
                                <ListGroup.Item><strong>Teléfono:</strong> {dashboard.telefono}</ListGroup.Item>
                                <ListGroup.Item><strong>Dirección:</strong> {dashboard.direccion}</ListGroup.Item>
                                <ListGroup.Item><strong>Página Web:</strong> {dashboard.urlPagina}</ListGroup.Item>
                                <ListGroup.Item><strong>Afiliación:</strong> {dashboard.fechaAfiliacion}</ListGroup.Item>
                            </ListGroup>
                        </Card>
                    </Col>

                    {/* SECCIÓN DERECHA */}
                    <Col md={8}>

                        {/* STATS */}
                        <Row className="g-4">

                            <Col md={4}>
                                <Card className="shadow-sm text-center p-3">
                                    <Card.Title className="text-muted small">Total Productos</Card.Title>
                                    <h2 className="text-primary fw-bold">{dashboard.totalProductos}</h2>
                                </Card>
                            </Col>

                            <Col md={4}>
                                <Card className="shadow-sm text-center p-3">
                                    <Card.Title className="text-muted small">Total Etiquetas</Card.Title>
                                    <h2 className="text-warning fw-bold">{dashboard.totalEtiquetas}</h2>
                                </Card>
                            </Col>

                            <Col md={4}>
                                <Card className="shadow-sm text-center p-3">
                                    <Card.Title className="text-muted small">Suscripciones</Card.Title>
                                    <h2 className="text-success fw-bold">{dashboard.totalSuscripciones}</h2>
                                </Card>
                            </Col>

                        </Row>

                        {/* PLAN ACTUAL */}
                        <Card className="shadow-sm mt-4">
                            <Card.Header className="fw-bold bg-light">
                                Plan de Suscripción
                            </Card.Header>
                            <Card.Body>
                                {dashboard.plan ? (
                                    <>
                                        <p><strong>Nombre:</strong> {dashboard.plan.nombre}</p>
                                        <p><strong>Inicio:</strong> {dashboard.plan.fechaInicio}</p>
                                        <p><strong>Fin:</strong> {dashboard.plan.fechaFin}</p>
                                        <Badge bg="info">{dashboard.plan.estado}</Badge>
                                    </>
                                ) : (
                                    <p className="text-muted">La tienda no tiene un plan activo.</p>
                                )}
                            </Card.Body>
                        </Card>

                        {/* API TIENDA */}
                        <Card className="shadow-sm mt-4 mb-4">
                            <Card.Header className="fw-bold bg-light">
                                Configuración de API
                            </Card.Header>
                            <Card.Body>
                                {dashboard.tienda ? (
                                    <>
                                        <p><strong>URL Base:</strong> {dashboard.tienda.urlBase}</p>
                                        <p>
                                            <strong>Estado API:</strong>{" "}
                                            <Badge
                                                bg={
                                                    dashboard.tienda.estadoAPI === "ACTIVO"
                                                        ? "success"
                                                        : dashboard.tienda.estadoAPI === "INACTIVO"
                                                            ? "danger"
                                                            : "secondary"
                                                }
                                            >
                                                {dashboard.tienda.estadoAPI}
                                            </Badge>
                                        </p>
                                    </>
                                ) : (
                                    <p className="text-muted">La tienda no tiene API configurada.</p>
                                )}
                            </Card.Body>
                        </Card>


                    </Col>

                </Row>
            )}
        </Container>
    );
};

export default TiendaDashboard;
