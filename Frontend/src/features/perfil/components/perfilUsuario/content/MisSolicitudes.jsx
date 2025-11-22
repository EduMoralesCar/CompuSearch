import React, { useEffect, useState } from "react";
import { Accordion, Spinner, Alert, Button } from "react-bootstrap";
import { useSolicitudes } from "../../../hooks/useSolicitudes";
import { useAuthStatus } from "../../../../../hooks/useAuthStatus"

const MisSolicitudes = () => {
    const { respuesta, loading, error, obtenerSolicitudes, totalPages } = useSolicitudes();
    const { idUsuario } = useAuthStatus();
    const [page, setPage] = useState(0);

    useEffect(() => {
        if (idUsuario) {
            obtenerSolicitudes(idUsuario, page, 5);
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [idUsuario, page]);

    return (
        <div className="container mt-4">
            <h3 className="mb-4">Mis Solicitudes</h3>

            {loading && (
                <div className="text-center my-4">
                    <Spinner animation="border" variant="primary" />
                    <p className="mt-2">Cargando solicitudes...</p>
                </div>
            )}

            {error && <Alert variant="danger">{error}</Alert>}

            {!loading && !error && respuesta.length === 0 && (
                <Alert variant="info">No tienes solicitudes registradas.</Alert>
            )}

            {!loading && respuesta.length > 0 && (
                <Accordion defaultActiveKey="0">
                    {respuesta.map((solicitud, index) => {
                        const datos = (() => {
                            try {
                                return JSON.parse(solicitud.datosFormulario);
                            } catch {
                                return solicitud.datosFormulario;
                            }
                        })();

                        return (
                            <Accordion.Item key={solicitud.idSolicitud} eventKey={String(index)}>
                                <Accordion.Header>
                                    <div className="w-100 d-flex justify-content-between align-items-center">
                                        <div>
                                            <strong>Solicitud #{solicitud.idSolicitud}</strong> —{" "}
                                            <span className="text-primary">{solicitud.estado}</span>
                                        </div>
                                        <small className="text-muted">
                                            {new Date(solicitud.fechaSolicitud).toLocaleString()}
                                        </small>
                                    </div>
                                </Accordion.Header>
                                <Accordion.Body>
                                    <h6>Datos del formulario:</h6>

                                    {typeof datos === "object" && datos !== null ? (
                                        <div
                                            style={{
                                                backgroundColor: "#f8f9fa",
                                                padding: "15px",
                                                borderRadius: "10px",
                                                border: "1px solid #e0e0e0",
                                            }}
                                        >
                                            <ul style={{ listStyle: "none", paddingLeft: 0, marginBottom: 0 }}>
                                                {Object.entries(datos).map(([clave, valor]) => (
                                                    <li
                                                        key={clave}
                                                        style={{
                                                            marginBottom: "6px",
                                                            borderBottom: "1px dashed #ddd",
                                                            paddingBottom: "4px",
                                                        }}
                                                    >
                                                        <strong style={{ textTransform: "capitalize" }}>
                                                            {clave.replace(/([A-Z])/g, " $1")}:
                                                        </strong>{" "}
                                                        <span style={{ color: "#555" }}>
                                                            {typeof valor === "object"
                                                                ? JSON.stringify(valor, null, 2)
                                                                : String(valor)}
                                                        </span>
                                                    </li>
                                                ))}
                                            </ul>
                                        </div>
                                    ) : (
                                        <pre
                                            style={{
                                                backgroundColor: "#f8f9fa",
                                                padding: "10px",
                                                borderRadius: "5px",
                                                whiteSpace: "pre-wrap",
                                                border: "1px solid #e0e0e0",
                                            }}
                                        >
                                            {datos}
                                        </pre>
                                    )}
                                </Accordion.Body>
                            </Accordion.Item>
                        );
                    })}
                </Accordion>
            )}

            {totalPages > 1 && (
                <div className="d-flex justify-content-between align-items-center mt-4">
                    <Button
                        variant="secondary"
                        disabled={page === 0}
                        onClick={() => setPage((prev) => Math.max(prev - 1, 0))}
                    >
                        ⬅ Anterior
                    </Button>
                    <span>
                        Página {page + 1} de {totalPages}
                    </span>
                    <Button
                        variant="secondary"
                        disabled={page + 1 >= totalPages}
                        onClick={() => setPage((prev) => prev + 1)}
                    >
                        Siguiente ➡
                    </Button>
                </div>
            )}
        </div>
    );
};

export default MisSolicitudes;
