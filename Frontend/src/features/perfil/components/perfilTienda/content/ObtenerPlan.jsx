import { Container, Row, Col } from "react-bootstrap";

const ObtenerPlan = () => {
    return (
        <Container className="py-4">
            <Row className="mb-4">
                <Col>
                    <h2 className="fw-bold text-primary mb-1">Comprar un Plan</h2>
                    <p className="text-muted mb-0">
                        Elige un plan para mejorar la visibilidad y funciones de tu tienda.
                    </p>
                </Col>
            </Row>
        </Container>
    );
};

export default ObtenerPlan;
