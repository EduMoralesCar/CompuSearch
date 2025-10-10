/*
const Builds = () => {
    return <div>Página de Builds</div>;
};

export default Builds;
*/

import React from "react";
import { Container, Row, Col, Card, Button, ListGroup } from "react-bootstrap";
import { FaShareAlt } from "react-icons/fa";
import bannerImg from "../../../assets/banners/banner1.webp"; // Usa tu imagen existente
import "./Home.css"; // Aquí van los estilos del banner

const Builds = () => {
    const components = [
        { name: "Procesador (CPU)", placeholder: "Elegir CPU" },
        { name: "Disipador CPU", placeholder: "Elegir Disipador" },
        { name: "Placa Madre", placeholder: "Elegir Motherboard" },
        { name: "Memoria RAM", placeholder: "Elegir RAM" },
        { name: "Almacenamiento", placeholder: "Elegir Storage" },
        { name: "Case/Gabinete", placeholder: "Elegir Case" },
        { name: "Fuente de poder", placeholder: "Elegir Fuente" },
        { name: "Tarjeta Gráfica", placeholder: "Elegir GPU" },
        { name: "Otros", placeholder: "Elige otros productos" },
    ];

    return (
        <>
            {/* 🔹 Banner superior */}
            <div className="builds-banner d-flex align-items-center justify-content-center text-center">
                <div className="text-white banner-text">
                    <h1 className="display-5 fw-bold">Arma tu computadora</h1>
                    <p className="lead">
                        Crea tu PC personalizada con los mejores componentes.
                    </p>
                </div>
            </div>

            {/* 🔹 Contenido principal */}
            <Container className="my-5">
                <Row className="mb-4">
                    <Col xs={12} lg={8} className="d-flex flex-column gap-3 mb-3 mb-lg-0">
                        <h1 className="fw-bold m-0 text-center text-lg-start">
                            SELECCIONE TUS COMPONENTES
                        </h1>
                        <p className="text-muted h5 mt-0 text-center text-lg-start">
                            Selecciona componentes compatibles para armar tu computadora ideal.
                        </p>
                        <Row className="w-100 g-0 justify-content-center justify-content-lg-start">
                            <Col xs={12} md={10} lg={8} className="d-flex gap-3 gap-lg-5">
                                <div className="bg-dark text-white rounded-4 py-2 px-4 flex-fill text-center">
                                    <strong>COMPATIBILIDAD:</strong> 0%
                                </div>
                                <div className="bg-dark text-white rounded-4 py-2 px-4 flex-fill text-center">
                                    <strong>CONSUMO:</strong> 0 W
                                </div>
                            </Col>
                        </Row>
                    </Col>

                    <Col xs={12} lg={4}>
                        <Card className="bg-light shadow-sm h-100">
                            <Card.Header className="d-flex flex-column justify-content-between align-items-center">
                                <strong className="h4 fw-bold text-primary mb-3">
                                    Tu construcción
                                </strong>
                                <div className="d-flex flex-row justify-content-between w-100">
                                    <span className="fw-bold">Total:</span>
                                    <span className="fw-bold">S/ 0.00</span>
                                </div>
                            </Card.Header>
                            <Card.Body className="d-grid gap-2">
                                <Button variant="primary">Guardar Componentes</Button>
                                <Button variant="outline-primary">
                                    <FaShareAlt className="me-2" />
                                    Compartir
                                </Button>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>

                <Card className="shadow-sm">
                    <ListGroup variant="flush">
                        <ListGroup.Item className="d-none d-md-flex fw-bold text-uppercase bg-light">
                            <Row className="w-100">
                                <Col md={2}>COMPONENTE</Col>
                                <Col md={3}>SELECCIÓN</Col>
                                <Col md={2}>PRECIO</Col>
                                <Col md={2}>DISPONIBILIDAD</Col>
                                <Col md={3}>TIENDA</Col>
                            </Row>
                        </ListGroup.Item>

                        {components.map((component, index) => (
                            <ListGroup.Item key={index}>
                                <Row className="align-items-center gy-2">
                                    <Col xs={12} md={2} className="fw-bold">
                                        {component.name}
                                    </Col>
                                    <Col xs={12} md={3}>
                                        <Button variant="outline-primary" className="w-100 rounded-5">
                                            + {component.placeholder}
                                        </Button>
                                    </Col>
                                    <Col xs={4} md={2} className="text-muted text-center text-md-start">
                                        ---
                                    </Col>
                                    <Col xs={4} md={2} className="text-muted text-center text-md-start">
                                        ---
                                    </Col>
                                    <Col xs={4} md={3} className="text-muted text-center text-md-start">
                                        ---
                                    </Col>
                                </Row>
                            </ListGroup.Item>
                        ))}
                    </ListGroup>
                </Card>
            </Container>
        </>
    );
};

export default Builds;
