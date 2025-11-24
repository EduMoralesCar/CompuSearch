import { useEffect, useState } from "react";
import { useTiendas } from "../../../hooks/useTiendas";
import { Card, Button, Form, Alert, Spinner, Badge } from "react-bootstrap";

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
            setApiActual(prev => ({
                ...prev,
                estadoAPI: resp.data
            }));

            showStatus("success", "API probada exitosamente.");
        } else {
            showStatus("danger", "No se pudo probar la API.");
        }

    };

    return (
        <div className="container mt-4" style={{ maxWidth: "650px" }}>
            <h3 className="mb-4 fw-semibold text-secondary">Conexión a API</h3>

            {status.message && (
                <Alert variant={status.type} className="mb-3">
                    {status.message}
                </Alert>
            )}

            {loading && !apiActual && (
                <div className="d-flex justify-content-center my-4">
                    <Spinner animation="border" />
                </div>
            )}

            {error && <Alert variant="danger">{error}</Alert>}

            <Card className="shadow-sm border-0">
                <Card.Body>
                    {!apiActual?.urlBase ? (
                        <Alert variant="secondary">
                            <strong>Aún no tienes una API configurada.</strong><br />
                            Ingresa la URL base para registrar tu API.
                        </Alert>
                    ) : (
                        <>
                            <p className="mb-1"><strong>URL actual:</strong></p>
                            <p className="text-muted">
                                {apiActual.urlBase}
                            </p>

                            <p className="mb-1"><strong>Estado:</strong></p>
                            <Badge
                                bg={
                                    apiActual.estadoAPI === "ACTIVA" ? "success"
                                        : apiActual.estadoAPI === "INACTIVA" ? "warning"
                                            : "danger"
                                }
                                className="mb-3"
                            >
                                {apiActual.estadoAPI}
                            </Badge>
                        </>
                    )}

                    <Form.Group className="mb-3">
                        <Form.Label className="fw-medium">Nueva URL base</Form.Label>
                        <Form.Control
                            type="text"
                            value={nuevaUrl}
                            placeholder="https://miapi.com"
                            onChange={(e) => setNuevaUrl(e.target.value)}
                            disabled={loading}
                        />
                    </Form.Group>

                    <Button
                        variant="primary"
                        className="w-100 mb-2"
                        onClick={handleGuardar}
                        disabled={loading}
                    >
                        {loading ? (
                            <>
                                <Spinner size="sm" className="me-2" animation="border" />
                                Guardando...
                            </>
                        ) : (
                            "Guardar URL"
                        )}
                    </Button>

                    {apiActual?.urlBase && (
                        <Button
                            variant="dark"
                            className="w-100"
                            onClick={handleProbar}
                            disabled={loading}
                        >
                            {loading ? (
                                <>
                                    <Spinner size="sm" className="me-2" animation="border" />
                                    Probando...
                                </>
                            ) : (
                                "Probar API"
                            )}
                        </Button>
                    )}
                </Card.Body>
            </Card>
        </div>
    );
};

export default ConexionAPI;
