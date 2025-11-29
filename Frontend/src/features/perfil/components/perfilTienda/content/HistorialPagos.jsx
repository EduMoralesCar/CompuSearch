import { useEffect } from "react";
import { Container, Row, Col, Table, Button, Spinner, Alert } from "react-bootstrap";
import { usePagos } from "../../../hooks/usePagos";

const HistorialPagos = ({ idTienda }) => {
    const {
        pagos,
        totalElements,
        page,
        size,
        loading,
        error,
        setPage,
        fetchHistorialPagos,
    } = usePagos();

    useEffect(() => {
        fetchHistorialPagos(idTienda, 0, 12);
        // eslint-disable-next-line
    }, [idTienda]);

    const handlePrev = () => {
        if (page > 0) {
            const newPage = page - 1;
            setPage(newPage);
            fetchHistorialPagos(idTienda, newPage, 12);
        }
    };

    const handleNext = () => {
        if ((page + 1) * size < totalElements) {
            const newPage = page + 1;
            setPage(newPage);
            fetchHistorialPagos(idTienda, newPage, 12);
        }
    };

    return (
        <Container className="py-4">
            <Row className="mb-4">
                <Col>
                    <h2 className="fw-bold text-primary mb-1">Historial de Pagos</h2>
                    <p className="text-muted mb-0">
                        Consulta los pagos realizados y el estado de tus facturas.
                    </p>
                </Col>
            </Row>

            {loading && (
                <div className="text-center py-4">
                    <Spinner animation="border" />
                    <p className="text-muted mt-2">Cargando historial...</p>
                </div>
            )}

            {error && <Alert variant="danger">{error}</Alert>}

            {!loading && !error && pagos.length === 0 && (
                <Alert variant="info">No hay pagos registrados aún.</Alert>
            )}

            {!loading && !error && pagos.length > 0 && (
                <>
                    <Table striped bordered hover responsive>
                        <thead className="table-primary">
                            <tr>
                                <th>#</th>
                                <th>Plan</th>
                                <th>Precio</th>
                                <th>Estado</th>
                                <th>Fecha Pago</th>
                                <th>Transacción</th>
                            </tr>
                        </thead>
                        <tbody>
                            {pagos.map((pago, idx) => (
                                <tr key={pago.id}>
                                    <td>{page * size + idx + 1}</td>
                                    <td>{pago.nombrePlan}</td>
                                    <td>S/ {pago.precio}</td>
                                    <td>
                                        <span
                                            className={`badge ${
                                                pago.estadoPago === "PAGADO" ? "bg-success" : "bg-warning"
                                            }`}
                                        >
                                            {pago.estadoPago}
                                        </span>
                                    </td>
                                    <td>{new Date(pago.fechaPago).toLocaleDateString()}</td>
                                    <td>{pago.transactionId}</td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>

                    {totalElements > size && (
                        <div className="d-flex justify-content-between my-3">
                            <Button
                                variant="outline-primary"
                                onClick={handlePrev}
                                disabled={page === 0}
                            >
                                Anterior
                            </Button>
                            <span className="align-self-center">
                                Página {page + 1} de {Math.ceil(totalElements / size)}
                            </span>
                            <Button
                                variant="outline-primary"
                                onClick={handleNext}
                                disabled={(page + 1) * size >= totalElements}
                            >
                                Siguiente
                            </Button>
                        </div>
                    )}
                </>
            )}
        </Container>
    );
};

export default HistorialPagos;
