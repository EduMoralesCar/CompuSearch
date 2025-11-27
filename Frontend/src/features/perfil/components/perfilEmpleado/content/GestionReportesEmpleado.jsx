import { useState } from "react";
import { Card, Button, Form, Row, Col, Alert, Spinner } from "react-bootstrap";
import {
    FiDownload,
    FiCalendar,
    FiBarChart2,
    FiUserCheck,
    FiUsers,
    FiShoppingBag  
} from "react-icons/fi";
import useReportesEmpleado from "../../../hooks/useReportesEmpleado";

const GestionReportesEmpleado = () => {

    const {
        exportTiendasDesdeFecha,
        exportTopTiendasPorProductos,
        exportTopTiendasPorVisitas,
        exportUsuariosDesdeFecha,
        exportUsuariosActivosInactivos,
        loading,
        error
    } = useReportesEmpleado();

    const [fechaInicio, setFechaInicio] = useState("");
    const [topN, setTopN] = useState(5);
    const [fechaUsuarios, setFechaUsuarios] = useState("");

    return (
        <>
            <Card className="shadow-lg border-0">
                <Card.Header
                    as="h5"
                    className="d-flex justify-content-between align-items-center bg-light text-primary py-3 px-4"
                >
                    Gestión de Reportes del Administrador
                    <div style={{ width: "40px", height: "37px" }}></div>
                </Card.Header>

                <Card.Body className="px-4">

                    {error && (
                        <Alert variant="danger">
                            <strong>Error:</strong> {error.message}
                        </Alert>
                    )}

                    <h5 className="text-secondary mt-3">
                        <FiShoppingBag className="me-2" /> Reportes de Tiendas
                    </h5>
                    <hr />

                    <Row className="align-items-end mb-4">
                        <Col md={4}>
                            <Form.Label className="fw-bold">
                                <FiCalendar className="me-2" /> Fecha Inicio
                            </Form.Label>
                            <Form.Control
                                type="datetime-local"
                                value={fechaInicio}
                                onChange={(e) => setFechaInicio(e.target.value)}
                                className="shadow-sm"
                            />
                        </Col>

                        <Col md="auto" className="mt-3">
                            <Button
                                variant="primary"
                                disabled={loading || !fechaInicio}
                                onClick={() => exportTiendasDesdeFecha(fechaInicio)}
                                className="px-3"
                            >
                                {loading ? <Spinner size="sm" /> : <FiDownload />}
                                {" "} Descargar
                            </Button>
                        </Col>
                    </Row>

                    <Row className="align-items-end mb-4">
                        <Col md={3}>
                            <Form.Label className="fw-bold">
                                <FiBarChart2 className="me-2" /> Top N
                            </Form.Label>
                            <Form.Control
                                type="number"
                                min="1"
                                value={topN}
                                className="shadow-sm"
                                onChange={(e) => setTopN(parseInt(e.target.value))}
                            />
                        </Col>

                        <Col md="auto" className="mt-3">
                            <Button
                                variant="success"
                                disabled={loading}
                                onClick={() => exportTopTiendasPorProductos(topN)}
                                className="px-3"
                            >
                                {loading ? <Spinner size="sm" /> : <FiDownload />}
                                {" "}Top por Productos
                            </Button>
                        </Col>
                    </Row>

                    <Row className="align-items-end mb-4">
                        <Col md={3}>
                            <Form.Label className="fw-bold">
                                <FiBarChart2 className="me-2" /> Top N
                            </Form.Label>
                            <Form.Control
                                type="number"
                                min="1"
                                value={topN}
                                className="shadow-sm"
                                onChange={(e) => setTopN(parseInt(e.target.value))}
                            />
                        </Col>

                        <Col md="auto" className="mt-3">
                            <Button
                                variant="warning"
                                disabled={loading}
                                onClick={() => exportTopTiendasPorVisitas(topN)}
                                className="px-3 text-dark"
                            >
                                {loading ? <Spinner size="sm" /> : <FiDownload />}
                                {" "}Top por Visitas
                            </Button>
                        </Col>
                    </Row>

                    <h5 className="text-secondary mt-4">
                        <FiUsers className="me-2" /> Reportes de Usuarios
                    </h5>
                    <hr />

                    <Row className="align-items-end mb-4">
                        <Col md={4}>
                            <Form.Label className="fw-bold">
                                <FiCalendar className="me-2" /> Fecha Inicio
                            </Form.Label>
                            <Form.Control
                                type="datetime-local"
                                value={fechaUsuarios}
                                className="shadow-sm"
                                onChange={(e) => setFechaUsuarios(e.target.value)}
                            />
                        </Col>

                        <Col md="auto" className="mt-3">
                            <Button
                                variant="info"
                                disabled={loading || !fechaUsuarios}
                                onClick={() => exportUsuariosDesdeFecha(fechaUsuarios)}
                                className="px-3 text-white"
                            >
                                {loading ? <Spinner size="sm" /> : <FiDownload />}
                                {" "}Descargar
                            </Button>
                        </Col>
                    </Row>

                    <Row className="align-items-end mb-4">
                        <Col md="auto" className="mt-3">
                            <Button
                                variant="dark"
                                disabled={loading}
                                onClick={exportUsuariosActivosInactivos}
                                className="px-3"
                            >
                                {loading ? <Spinner size="sm" /> : <FiUsers />}
                                {" "}Activos vs Inactivos
                            </Button>
                        </Col>
                    </Row>

                </Card.Body>
            </Card>
        </>
    );
};

export default GestionReportesEmpleado;
