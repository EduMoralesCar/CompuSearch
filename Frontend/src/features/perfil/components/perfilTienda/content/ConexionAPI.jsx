import { useEffect, useState } from "react";
import { useTiendas } from "../../../hooks/useTiendas";
import { Card, Button, Form, Alert, Spinner, Badge, Container, Row, Col, Stack } from "react-bootstrap";

const ConexionAPI = ({ idTienda }) => {
    const { obtenerApi, actualizarApi, probarApi, loading, error } = useTiendas();

    const [apiActual, setApiActual] = useState(null);
    const [nuevaUrl, setNuevaUrl] = useState("");
    const [status, setStatus] = useState({ type: "", message: "" });

    const showStatus = (type, message) => {
        setStatus({ type, message });
        setTimeout(() => setStatus({ type: "", message: "" }), 4000);
    };

    useEffect(() => {
        const cargarApi = async () => {
            const resp = await obtenerApi(idTienda);
            if (resp.success) {
                setApiActual(resp.data);
                setNuevaUrl(resp.data?.urlBase || "");
            }
        };
        cargarApi();
    }, [idTienda, obtenerApi]);

    const handleGuardar = async () => {
        if (!nuevaUrl.trim()) {
            showStatus("danger", "La URL no puede estar vacía.");
            return;
        }

        const resp = await actualizarApi(idTienda, nuevaUrl);

        if (resp.success) {
            setApiActual(prev => ({ ...prev, urlBase: nuevaUrl, estadoAPI: resp.data }));
            showStatus("success", "API actualizada correctamente.");
        } else {
            showStatus("danger", "Error al actualizar la API.");
        }
    };

    const handleProbar = async () => {
        const resp = await probarApi(idTienda);
        if (resp.success) {
            setApiActual(prev => ({ ...prev, estadoAPI: resp.data }));
            showStatus("success", "API probada exitosamente.");
        } else {
            showStatus("danger", "No se pudo probar la API.");
        }
    };

    return (
        <Container className="py-4">
            {/* Título y descripción */}
            <Row className="mb-4">
                <Col>
                    <h2 className="fw-bold text-primary mb-1">Conexión a API</h2>
                    <p className="text-muted mb-0">
                        Revisa y edita la API de tu tienda para mantener tus productos al día.
                    </p>
                </Col>
            </Row>

            {/* Mensajes */}
            {status.message && <Alert variant={status.type}>{status.message}</Alert>}
            {error && <Alert variant="danger">{error}</Alert>}

            {/* Cargando */}
            {loading && !apiActual && (
                <div className="d-flex justify-content-center my-4">
                    <Spinner animation="border" />
                </div>
            )}

            {/* Card principal */}
            <Card className="shadow-sm border-0">
                <Card.Body>
                    {/* Estado de la API */}
                    {apiActual ? (
                        <>
                            <div className="mb-3">
                                <p className="mb-1"><strong>URL actual:</strong></p>
                                <p className="text-muted mb-0">{apiActual.urlBase || "-"}</p>
                            </div>

                            <div className="mb-4">
                                <p className="mb-1"><strong>Estado:</strong></p>
                                <Badge
                                    bg={
                                        apiActual.estadoAPI === "ACTIVA" ? "success" :
                                            apiActual.estadoAPI === "INACTIVA" ? "warning" :
                                                "danger"
                                    }
                                >
                                    {apiActual.estadoAPI || "DESCONOCIDO"}
                                </Badge>
                            </div>
                        </>
                    ) : (
                        <Alert variant="secondary" className="mb-4">
                            <strong>Aún no tienes una API configurada.</strong><br />
                            Ingresa la URL base para registrar tu API.
                        </Alert>
                    )}

                    {/* Formulario de URL */}
                    <Form.Group className="mb-4">
                        <Form.Label className="fw-medium">Nueva URL base</Form.Label>
                        <Form.Text className="text-muted d-block mb-2">
                            Ingresa aquí el endpoint base de tu API para realizar las pruebas.
                        </Form.Text>
                        <Form.Control
                            type="text"
                            placeholder="https://miapi.com"
                            value={nuevaUrl}
                            onChange={(e) => setNuevaUrl(e.target.value)}
                            disabled={loading}
                        />
                    </Form.Group>

                    {/* Botones */}
                    <div className="d-flex flex-wrap gap-2">
                        <Button
                            variant="primary"
                            onClick={handleGuardar}
                            disabled={loading}
                            className="flex-shrink-0"
                        >
                            {loading ? (
                                <>
                                    <Spinner size="sm" className="me-2" animation="border" />
                                    Guardando...
                                </>
                            ) : "Guardar URL"}
                        </Button>

                        {apiActual?.urlBase && (
                            <Button
                                variant="dark"
                                onClick={handleProbar}
                                disabled={loading}
                                className="flex-shrink-0"
                            >
                                {loading ? (
                                    <>
                                        <Spinner size="sm" className="me-2" animation="border" />
                                        Probando...
                                    </>
                                ) : "Probar API"}
                            </Button>
                        )}
                    </div>

                </Card.Body>
            </Card>
        </Container>
    );
};

export default ConexionAPI;
