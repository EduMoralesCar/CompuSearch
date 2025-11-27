import { useState } from "react";
import { Card, Button, Form, Row, Col, Alert, Spinner } from "react-bootstrap";
import { FiDownload, FiCalendar, FiBarChart2, FiUsers, FiShoppingBag } from "react-icons/fi";
import HeaderBase from "../auxiliar/HeaderBase";
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

    const [inputs, setInputs] = useState({
        fechaTiendas: "",
        fechaUsuarios: "",
        topN: 5
    });

    const handleChange = (field, value) => setInputs(prev => ({ ...prev, [field]: value }));

    const downloadButton = (variant, onClick, disabled, icon, text) => (
        <Button variant={variant} disabled={disabled} onClick={onClick} className="px-3">
            {loading ? <Spinner size="sm" /> : icon}{" "}{text}
        </Button>
    );

    return (
        <Card className="shadow-lg border-0">
            <HeaderBase title="Gestión de Reportes del Administrador">
                <div style={{ width: "40px", height: "37px" }}></div>
            </HeaderBase>

            <Card.Body className="px-4">
                {error && <Alert variant="danger"><strong>Error:</strong> {error.message}</Alert>}

                {/* Reportes de Tiendas */}
                <h5 className="text-secondary mt-3"><FiShoppingBag className="me-2" /> Reportes de Tiendas</h5>
                <hr />

                <Row className="align-items-end mb-4">
                    <Col md={4}>
                        <Form.Label className="fw-bold"><FiCalendar className="me-2" /> Fecha Inicio</Form.Label>
                        <Form.Control
                            type="datetime-local"
                            value={inputs.fechaTiendas}
                            onChange={e => handleChange("fechaTiendas", e.target.value)}
                            className="shadow-sm"
                        />
                    </Col>
                    <Col md="auto" className="mt-3">
                        {downloadButton(
                            "primary",
                            () => exportTiendasDesdeFecha(inputs.fechaTiendas),
                            loading || !inputs.fechaTiendas,
                            <FiDownload />,
                            "Descargar"
                        )}
                    </Col>
                </Row>

                {[["success", exportTopTiendasPorProductos, "Top por Productos"],
                  ["warning", exportTopTiendasPorVisitas, "Top por Visitas"]].map(([variant, action, text], idx) => (
                    <Row key={idx} className="align-items-end mb-4">
                        <Col md={3}>
                            <Form.Label className="fw-bold"><FiBarChart2 className="me-2" /> Top N</Form.Label>
                            <Form.Control
                                type="number"
                                min="1"
                                value={inputs.topN}
                                className="shadow-sm"
                                onChange={e => handleChange("topN", parseInt(e.target.value))}
                            />
                        </Col>
                        <Col md="auto" className="mt-3">
                            {downloadButton(
                                variant,
                                () => action(inputs.topN),
                                loading,
                                <FiDownload />,
                                text
                            )}
                        </Col>
                    </Row>
                ))}

                {/* Reportes de Usuarios */}
                <h5 className="text-secondary mt-4"><FiUsers className="me-2" /> Reportes de Usuarios</h5>
                <hr />

                <Row className="align-items-end mb-4">
                    <Col md={4}>
                        <Form.Label className="fw-bold"><FiCalendar className="me-2" /> Fecha Inicio</Form.Label>
                        <Form.Control
                            type="datetime-local"
                            value={inputs.fechaUsuarios}
                            onChange={e => handleChange("fechaUsuarios", e.target.value)}
                            className="shadow-sm"
                        />
                    </Col>
                    <Col md="auto" className="mt-3">
                        {downloadButton(
                            "info",
                            () => exportUsuariosDesdeFecha(inputs.fechaUsuarios),
                            loading || !inputs.fechaUsuarios,
                            <FiDownload />,
                            "Descargar"
                        )}
                    </Col>
                </Row>

                <Row className="align-items-end mb-4">
                    <Col md="auto" className="mt-3">
                        {downloadButton(
                            "dark",
                            exportUsuariosActivosInactivos,
                            loading,
                            <FiUsers />,
                            "Activos vs Inactivos"
                        )}
                    </Col>
                </Row>
            </Card.Body>
        </Card>
    );
};

export default GestionReportesEmpleado;
