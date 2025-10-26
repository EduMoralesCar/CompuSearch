import React from "react";
import { Row, Col, Button, Container } from "react-bootstrap";
import { Link } from "react-router-dom";

const CTASection = () => (
    <Container className="text-center py-5">
        <div className="bg-primary d-inline-block p-3 rounded shadow mb-5">
            <h5 className="fw-bold text-white m-0">CON MEJORES PRECIOS DEL MERCADO</h5>
        </div>

        <Row className="justify-content-center align-items-center text-center mt-5">
            <Col xs={12} md="auto">
                <h4 className="fw-bold mb-3 mb-md-0">¿Armando una PC desde cero?</h4>
            </Col>
            <Col xs={12} md="auto">
                <Button as={Link} to="/builds" variant="success" className="fw-bold px-4 py-2">
                    Comienza a armar tu PC
                </Button>
            </Col>
        </Row>
    </Container>
);

export default CTASection;
