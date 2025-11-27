import { useEffect, useState } from "react";
import useEmpleados from "../../../hooks/useEmpleados";
import {
    Container,
    Row,
    Col,
    Card,
    Table,
    Spinner,
    Alert,
    Badge
} from "react-bootstrap";
import { FaStore, FaUserTie, FaUsers, FaBoxOpen, FaMoneyBillWave, FaExclamationTriangle } from "react-icons/fa";

const EmpleadoDashboard = () => {
    const { obtenerEmpleadoDashboard, isLoading, error } = useEmpleados();
    const [dashboard, setDashboard] = useState(null);

    useEffect(() => {
        const cargarDashboard = async () => {
            const res = await obtenerEmpleadoDashboard();
            if (res.success) setDashboard(res.data);
        };
        cargarDashboard();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    return (
        <>
            <Card className="shadow-lg border-0">
                <Card.Header as="h5" className="d-flex justify-content-between align-items-center bg-light text-primary">
                    Dashboard del Empleado
                    <div style={{ width: "40px", height: "37px" }}></div>
                </Card.Header>

                {/* Loader */}
                {
                    isLoading && (
                        <div className="d-flex justify-content-center my-4">
                            <Spinner animation="border" role="status" />
                        </div>
                    )
                }

                {/* Error */}
                {error && <Alert variant="danger">{error}</Alert>}

                {
                    dashboard && (
                        <>
                            {/* Cards métricas generales */}
                            <Row className="g-4 mb-4">
                                <Col md={3}>
                                    <Card className="shadow-sm p-3 rounded-4 text-center h-100">
                                        <FaStore size={30} className="text-primary mb-2" />
                                        <Card.Title>Total Tiendas</Card.Title>
                                        <h2 className="text-primary">{dashboard.totalTiendas}</h2>
                                        <div className="d-flex justify-content-center flex-wrap mt-2">
                                            <Badge bg="success" className="me-1 mb-1">Verificadas: {dashboard.tiendasVerificadas}</Badge>
                                            <Badge bg="secondary" className="mb-1">No verificadas: {dashboard.tiendasNoVerificadas}</Badge>
                                        </div>
                                    </Card>
                                </Col>

                                <Col md={3}>
                                    <Card className="shadow-sm p-3 rounded-4 text-center h-100">
                                        <FaUserTie size={30} className="text-warning mb-2" />
                                        <Card.Title>Total Empleados</Card.Title>
                                        <h2 className="text-warning">{dashboard.totalEmpleados}</h2>
                                    </Card>
                                </Col>

                                <Col md={3}>
                                    <Card className="shadow-sm p-3 rounded-4 text-center h-100">
                                        <FaUsers size={30} className="text-info mb-2" />
                                        <Card.Title>Total Usuarios</Card.Title>
                                        <h2 className="text-info">{dashboard.totalUsuarios}</h2>
                                    </Card>
                                </Col>

                                <Col md={3}>
                                    <Card className="shadow-sm p-3 rounded-4 text-center h-100">
                                        <FaBoxOpen size={30} className="text-success mb-2" />
                                        <Card.Title>Total Productos</Card.Title>
                                        <h2 className="text-success">{dashboard.totalProductos}</h2>
                                        <div className="d-flex justify-content-center flex-wrap mt-2">
                                            <Badge bg="success" className="me-1 mb-1">Activos: {dashboard.productosActivos}</Badge>
                                            <Badge bg="danger" className="mb-1">Inactivos: {dashboard.productosInactivos}</Badge>
                                        </div>
                                    </Card>
                                </Col>
                            </Row>

                            {/* Solicitudes y Suscripciones */}
                            <Row className="g-4 mb-4">
                                <Col md={6}>
                                    <Card className="shadow-sm rounded-4 h-100">
                                        <Card.Header className="fw-bold bg-light">Solicitudes</Card.Header>
                                        <Card.Body>
                                            <div className="d-flex flex-wrap">
                                                <Badge bg="secondary" className="me-2 mb-1">Pendientes: {dashboard.solicitudesPendientes}</Badge>
                                                <Badge bg="success" className="me-2 mb-1">Aceptadas: {dashboard.solicitudesAceptadas}</Badge>
                                                <Badge bg="danger" className="mb-1">Rechazadas: {dashboard.solicitudesRechazadas}</Badge>
                                            </div>
                                            <p className="mt-2 mb-0 fw-bold">Total: {dashboard.totalSolicitudes}</p>
                                        </Card.Body>
                                    </Card>
                                </Col>

                                <Col md={6}>
                                    <Card className="shadow-sm rounded-4 h-100">
                                        <Card.Header className="fw-bold bg-light">Suscripciones</Card.Header>
                                        <Card.Body>
                                            <div className="d-flex flex-wrap">
                                                <Badge bg="success" className="me-2 mb-1">Activas: {dashboard.suscripcionesActivas}</Badge>
                                                <Badge bg="danger" className="mb-1">Expiradas: {dashboard.suscripcionesExpiradas}</Badge>
                                            </div>
                                            <p className="mt-2 mb-0 fw-bold">Total: {dashboard.totalSuscripciones}</p>
                                        </Card.Body>
                                    </Card>
                                </Col>
                            </Row>

                            {/* Incidentes e ingresos */}
                            <Row className="g-4 mb-4">
                                <Col md={6}>
                                    <Card className="text-center shadow-sm p-3 rounded-4 h-100">
                                        <FaExclamationTriangle size={30} className="text-danger mb-2" />
                                        <Card.Title>Total Incidentes</Card.Title>
                                        <h2 className="text-danger">{dashboard.totalIncidentes}</h2>
                                    </Card>
                                </Col>
                                <Col md={6}>
                                    <Card className="text-center shadow-sm p-3 rounded-4 h-100">
                                        <FaMoneyBillWave size={30} className="text-success mb-2" />
                                        <Card.Title>Ingresos Totales</Card.Title>
                                        <h2 className="text-success">S/.{dashboard.ingresosTotales.toLocaleString()}</h2>
                                        <p className="mb-0">Total pagos: {dashboard.totalPagos}</p>
                                    </Card>
                                </Col>
                            </Row>

                            {/* Tablas de actividad reciente */}
                            <Row className="g-4">
                                <Col md={4}>
                                    <Card className="shadow-sm rounded-4 h-100">
                                        <Card.Header className="fw-bold bg-light">Últimas Tiendas</Card.Header>
                                        <div style={{ maxHeight: "250px", overflowY: "auto" }}>
                                            <Table striped bordered hover size="sm" className="mb-0">
                                                <thead>
                                                    <tr>
                                                        <th>Nombre</th>
                                                        <th>Verificado</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    {dashboard.ultimasTiendas.map((t) => (
                                                        <tr key={t.idTienda}>
                                                            <td>{t.nombreTienda}</td>
                                                            <td>{t.verificado ? "Sí" : "No"}</td>
                                                        </tr>
                                                    ))}
                                                </tbody>
                                            </Table>
                                        </div>
                                    </Card>
                                </Col>

                                <Col md={4}>
                                    <Card className="shadow-sm rounded-4 h-100">
                                        <Card.Header className="fw-bold bg-light">Últimos Empleados</Card.Header>
                                        <div style={{ maxHeight: "250px", overflowY: "auto" }}>
                                            <Table striped bordered hover size="sm" className="mb-0">
                                                <thead>
                                                    <tr>
                                                        <th>Nombre</th>
                                                        <th>Email</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    {dashboard.ultimosEmpleados.map((e) => (
                                                        <tr key={e.idEmpleado}>
                                                            <td>{e.nombre}</td>
                                                            <td>{e.email}</td>
                                                        </tr>
                                                    ))}
                                                </tbody>
                                            </Table>
                                        </div>
                                    </Card>
                                </Col>

                                <Col md={4}>
                                    <Card className="shadow-sm rounded-4 h-100">
                                        <Card.Header className="fw-bold bg-light">Últimos Pagos</Card.Header>
                                        <div style={{ maxHeight: "250px", overflowY: "auto" }}>
                                            <Table striped bordered hover size="sm" className="mb-0">
                                                <thead>
                                                    <tr>
                                                        <th>Tienda</th>
                                                        <th>Monto</th>
                                                        <th>Estado</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    {dashboard.ultimosPagos.map((p) => (
                                                        <tr key={p.idPago}>
                                                            <td>{p.tiendaNombre}</td>
                                                            <td>S/.{p.monto.toLocaleString()}</td>
                                                            <td>{p.estado}</td>
                                                        </tr>
                                                    ))}
                                                </tbody>
                                            </Table>
                                        </div>
                                    </Card>
                                </Col>
                            </Row>
                        </>
                    )
                }
            </Card >
        </>
    );
};

export default EmpleadoDashboard;
