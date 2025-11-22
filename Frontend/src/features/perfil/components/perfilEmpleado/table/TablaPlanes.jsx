import React from "react";
import { Table, Spinner, Button, Badge, Alert, Pagination, Stack } from "react-bootstrap";

export default function TablaPlanes({
    planes,
    loading,
    error,
    mensaje,
    tipoMensaje,
    handleVerDetalle,
    handleEditar,
    confirmarCambioEstado
}) {
    return (
        <>
            <div className="text-center">
                {loading && <Spinner animation="border" variant="primary" />}
            </div>

            {mensaje && <Alert variant={tipoMensaje}>{mensaje}</Alert>}
            {!mensaje && error && <Alert variant="danger">{error}</Alert>}

            {!loading && planes.length === 0 && !error && (
                <Alert variant="info">No se encontraron planes disponibles.</Alert>
            )}

            {!loading && planes.length > 0 && (
                <>
                    <Table striped bordered hover responsive className="mb-4">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nombre del Plan</th>
                                <th>Duración (Meses)</th>
                                <th>Precio Mensual</th>
                                <th>Estado</th>
                                <th style={{ width: "250px" }} className="text-center">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            {planes.map((plan) => (
                                <tr key={plan.idPlan}>
                                    <td>{plan.idPlan}</td>
                                    <td>{plan.nombre}</td>
                                    <td>{plan.duracionMeses}</td>
                                    <td>
                                        <span className="fw-semibold">
                                            S/.{parseFloat(plan.precioMensual).toFixed(2)}
                                        </span>
                                    </td>
                                    <td>
                                        <Badge bg={plan.activo ? "success" : "secondary"}>
                                            {plan.activo ? "Activo" : "Inactivo"}
                                        </Badge>
                                    </td>
                                    <td>
                                        <Stack direction="horizontal" gap={2} className="justify-content-center">
                                            <Button
                                                variant="info"
                                                size="sm"
                                                onClick={() => handleVerDetalle(plan)}
                                            >
                                                Detalle
                                            </Button>

                                            <Button
                                                variant="warning"
                                                size="sm"
                                                onClick={() => handleEditar(plan)}
                                            >
                                                Editar
                                            </Button>

                                            <Button
                                                variant={plan.activo ? "danger" : "success"}
                                                size="sm"
                                                onClick={() => confirmarCambioEstado(plan)}
                                            >
                                                {plan.activo ? "Desactivar" : "Activar"}
                                            </Button>
                                        </Stack>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>
                </>
            )}
        </>
    );
}
