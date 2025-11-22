import React from "react";
import { Table, Spinner, Badge, Button, Form } from "react-bootstrap";
import FormatDate from "../../../../../utils/FormatDate";

export const StatusBadge = ({ isActive }) => (
    <Badge bg={isActive ? 'success' : 'danger'} className="text-uppercase">
        {isActive ? 'Activa' : 'Inactiva'}
    </Badge>
);

export const VerifiedStatus = ({ isVerified }) => (
    <span className={isVerified ? 'text-success' : 'text-warning'}>
        <i className={`fas fa-certificate me-1 ${isVerified ? 'text-success' : 'text-warning'}`}></i>
        {isVerified ? 'Verificada' : 'Sin Verificar'}
    </span>
);

export default function TablaTiendas({
    loading,
    tiendasData,
    error,
    handleToggleEstado,
    handleToggleVerificacion,
    viewDetails
}) {
    return (
        <>
            {error && (
                <div className="alert alert-danger" role="alert">
                    <i className="fas fa-exclamation-triangle me-2"></i>
                    <span className="fw-medium">Error:</span> {error}
                </div>
            )}

            <Table responsive striped bordered hover className="mb-0">
                <thead className="table-light">
                    <tr>
                        <th>ID / Nombre</th>
                        <th>Afiliación</th>
                        <th>Métricas</th>
                        <th className="text-center">Activa</th>
                        <th className="text-center">Verificada</th>
                        <th className="text-center">Acciones</th>
                    </tr>
                </thead>

                <tbody>
                    {loading && tiendasData.content.length === 0 && (
                        <tr>
                            <td colSpan="6" className="text-center py-5">
                                <Spinner animation="border" size="sm" variant="primary" className="me-2" />
                                Cargando datos...
                            </td>
                        </tr>
                    )}

                    {!loading && tiendasData.content.length === 0 && (
                        <tr>
                            <td colSpan="6" className="text-center py-4 text-muted">
                                No se encontraron tiendas.
                            </td>
                        </tr>
                    )}

                    {!loading && tiendasData.content.map((tienda) => (
                        <tr key={tienda.idUsuario}>
                            <td>
                                <div className="fw-semibold">{tienda.nombre}</div>
                                <div className="small text-muted">ID: {tienda.idUsuario} | {tienda.email}</div>
                            </td>
                            <td>
                                <i className="far fa-calendar-alt me-1 text-secondary"></i>
                                {FormatDate(tienda.fechaAfiliacion)}
                            </td>
                            <td>
                                <Badge bg="info" className="me-2">Prod: {tienda.productos}</Badge>
                                <Badge bg="warning">Subs: {tienda.suscripciones}</Badge>
                            </td>

                            <td className="text-center">
                                <Form.Check
                                    type="switch"
                                    checked={tienda.activo}
                                    onChange={(e) => handleToggleEstado(tienda.idUsuario, e.target.checked)}
                                    disabled={loading}
                                />
                                <div className="small mt-1">
                                    <StatusBadge isActive={tienda.activo} />
                                </div>
                            </td>

                            <td className="text-center">
                                <Form.Check
                                    type="switch"
                                    checked={tienda.verificado}
                                    onChange={(e) => handleToggleVerificacion(tienda.idUsuario, e.target.checked)}
                                    disabled={loading}
                                />
                                <div className="small mt-1">
                                    <VerifiedStatus isVerified={tienda.verificado} />
                                </div>
                            </td>

                            <td className="text-center">
                                <Button
                                    onClick={() => viewDetails(tienda.idUsuario)}
                                    disabled={loading}
                                    variant="outline-primary"
                                    size="sm"
                                >
                                    <i className="fas fa-eye me-1"></i> Ver
                                </Button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </Table>
        </>
    );
}
