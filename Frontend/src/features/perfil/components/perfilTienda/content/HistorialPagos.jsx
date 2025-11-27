import { Container, Row, Col } from "react-bootstrap";

const HistorialPagos = () => {
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
        </Container>
    );
};

export default HistorialPagos;
