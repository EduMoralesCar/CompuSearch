import { useEffect, useState } from "react";
import useEmpleados from "../../../hooks/useEmpleados";
import HeaderBase from "../auxiliar/HeaderBase";
import { Row, Col, Card, Table, Spinner, Alert, Badge } from "react-bootstrap";
import { FaStore, FaUserTie, FaUsers, FaBoxOpen, FaMoneyBillWave, FaExclamationTriangle } from "react-icons/fa";

const InfoCard = ({ icon: Icon, title, value, color, badges }) => (
    <Card className="shadow-sm p-3 rounded-4 text-center h-100">
        {Icon && <Icon size={30} className={`mb-2 text-${color}`} />}
        <Card.Title>{title}</Card.Title>
        <h2 className={`text-${color}`}>{value}</h2>
        {badges && (
            <div className="d-flex justify-content-center flex-wrap mt-2">
                {badges.map((b, idx) => (
                    <Badge key={idx} bg={b.bg} className={`me-1 mb-1`}>
                        {b.label}: {b.value}
                    </Badge>
                ))}
            </div>
        )}
    </Card>
);

const TableCard = ({ header, columns, data, rowKey }) => (
    <Card className="shadow-sm rounded-4 h-100">
        <Card.Header className="fw-bold bg-light">{header}</Card.Header>
        <div style={{ maxHeight: "250px", overflowY: "auto" }}>
            <Table striped bordered hover size="sm" className="mb-0">
                <thead>
                    <tr>
                        {columns.map((col, idx) => (
                            <th key={idx}>{col}</th>
                        ))}
                    </tr>
                </thead>
                <tbody>
                    {data.map((item) => (
                        <tr key={item[rowKey]}>
                            {columns.map((col, idx) => (
                                <td key={idx}>{item[col.toLowerCase()] ?? item[col]}</td>
                            ))}
                        </tr>
                    ))}
                </tbody>
            </Table>
        </div>
    </Card>
);

const EmpleadoDashboard = () => {
    const { obtenerEmpleadoDashboard, isLoading, error } = useEmpleados();
    const [dashboard, setDashboard] = useState(null);

    useEffect(() => {
        obtenerEmpleadoDashboard().then(res => {
            if (res.success) setDashboard(res.data);
        });
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    if (isLoading) {
        return (
            <div className="d-flex justify-content-center my-4">
                <Spinner animation="border" role="status" />
            </div>
        );
    }

    if (error) return <Alert variant="danger">{error}</Alert>;

    return (
        <Card className="shadow-lg border-0">
            <HeaderBase title="Dashboard del Empleado">
                <div style={{ width: "40px", height: "37px" }}></div>
            </HeaderBase>

            {dashboard && (
                <>
                    <Row className="g-4 mb-4">
                        <Col md={3}>
                            <InfoCard
                                icon={FaStore}
                                title="Total Tiendas"
                                value={dashboard.totalTiendas}
                                color="primary"
                                badges={[
                                    { label: "Verificadas", value: dashboard.tiendasVerificadas, bg: "success" },
                                    { label: "No verificadas", value: dashboard.tiendasNoVerificadas, bg: "secondary" },
                                ]}
                            />
                        </Col>
                        <Col md={3}>
                            <InfoCard
                                icon={FaUserTie}
                                title="Total Empleados"
                                value={dashboard.totalEmpleados}
                                color="warning"
                            />
                        </Col>
                        <Col md={3}>
                            <InfoCard
                                icon={FaUsers}
                                title="Total Usuarios"
                                value={dashboard.totalUsuarios}
                                color="info"
                            />
                        </Col>
                        <Col md={3}>
                            <InfoCard
                                icon={FaBoxOpen}
                                title="Total Productos"
                                value={dashboard.totalProductos}
                                color="success"
                                badges={[
                                    { label: "Activos", value: dashboard.productosActivos, bg: "success" },
                                    { label: "Inactivos", value: dashboard.productosInactivos, bg: "danger" },
                                ]}
                            />
                        </Col>
                    </Row>

                    <Row className="g-4 mb-4">
                        <Col md={6}>
                            <InfoCard
                                title="Solicitudes"
                                value={dashboard.totalSolicitudes}
                                badges={[
                                    { label: "Pendientes", value: dashboard.solicitudesPendientes, bg: "secondary" },
                                    { label: "Aceptadas", value: dashboard.solicitudesAceptadas, bg: "success" },
                                    { label: "Rechazadas", value: dashboard.solicitudesRechazadas, bg: "danger" },
                                ]}
                            />
                        </Col>
                        <Col md={6}>
                            <InfoCard
                                title="Suscripciones"
                                value={dashboard.totalSuscripciones}
                                badges={[
                                    { label: "Activas", value: dashboard.suscripcionesActivas, bg: "success" },
                                    { label: "Expiradas", value: dashboard.suscripcionesExpiradas, bg: "danger" },
                                ]}
                            />
                        </Col>
                    </Row>

                    <Row className="g-4 mb-4">
                        <Col md={6}>
                            <InfoCard icon={FaExclamationTriangle} title="Total Incidentes" value={dashboard.totalIncidentes} color="danger" />
                        </Col>
                        <Col md={6}>
                            <InfoCard
                                icon={FaMoneyBillWave}
                                title="Ingresos Totales"
                                value={`S/.${dashboard.ingresosTotales.toLocaleString()}`}
                                color="success"
                                badges={[{ label: "Total pagos", value: dashboard.totalPagos, bg: "light" }]}
                            />
                        </Col>
                    </Row>

                    <Row className="g-4">
                        <Col md={4}>
                            <TableCard
                                header="Últimas Tiendas"
                                columns={["Nombre", "Verificado"]}
                                data={dashboard.ultimasTiendas.map(t => ({ Nombre: t.nombreTienda, Verificado: t.verificado ? "Sí" : "No", id: t.idTienda }))}
                                rowKey="id"
                            />
                        </Col>
                        <Col md={4}>
                            <TableCard
                                header="Últimos Empleados"
                                columns={["Nombre", "Email"]}
                                data={dashboard.ultimosEmpleados.map(e => ({ Nombre: e.nombre, Email: e.email, id: e.idEmpleado }))}
                                rowKey="id"
                            />
                        </Col>
                        <Col md={4}>
                            <TableCard
                                header="Últimos Pagos"
                                columns={["Tienda", "Monto", "Estado"]}
                                data={dashboard.ultimosPagos.map(p => ({ Tienda: p.tiendaNombre, Monto: `S/.${p.monto.toLocaleString()}`, Estado: p.estado, id: p.idPago }))}
                                rowKey="id"
                            />
                        </Col>
                    </Row>
                </>
            )}
        </Card>
    );
};

export default EmpleadoDashboard;
