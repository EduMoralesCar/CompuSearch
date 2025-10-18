import React from "react";
import { Card, ListGroup, Row, Col, Button } from "react-bootstrap";

const BuildsTable = ({ categorias, onSelectCategoria, armado, onReiniciarCategoria }) => {
    const components = categorias.map((nombre) => ({
        name: nombre,
        placeholder: `Elegir ${nombre}`,
        producto: armado[nombre] || null
    }));

    return (
        <Card className="shadow-sm">
            <ListGroup variant="flush">
                {/* Encabezado solo visible en desktop */}
                <ListGroup.Item className="d-none d-md-flex fw-bold text-uppercase bg-light">
                    <Row className="w-100">
                        <Col md={2}>Selección</Col>
                        <Col md={3}>Componente</Col>
                        <Col md={1}>Precio</Col>
                        <Col md={1}>Cant.</Col>
                        <Col md={2}>Subtotal</Col>
                        <Col md={1}>Stock</Col>
                        <Col md={2}>Tienda</Col>
                    </Row>
                </ListGroup.Item>

                {components.map((component, index) => (
                    <ListGroup.Item key={index}>
                        <Row className="align-items-center gy-3">
                            {/* Selección */}
                            <Col xs={12} md={2}>
                                <Button
                                    variant="outline-primary"
                                    className="w-100 rounded-pill"
                                    onClick={() => onSelectCategoria(component.name)}
                                >
                                    + {component.placeholder}
                                </Button>
                            </Col>

                            {/* Componente */}
                            <Col xs={12} md={3}>
                                {component.producto ? (
                                    <div className="d-flex justify-content-between align-items-center">
                                        <span className="text-success fw-semibold">{component.producto.nombreProducto}</span>
                                        <Button
                                            variant="outline-danger"
                                            size="sm"
                                            onClick={() => onReiniciarCategoria(component.name)}
                                        >
                                            ❌
                                        </Button>
                                    </div>
                                ) : (
                                    <span className="text-muted">---</span>
                                )}
                            </Col>

                            {/* Precio */}
                            <Col xs={4} md={1} className="text-center text-md-start">
                                {component.producto ? `S/. ${component.producto.precio}` : "---"}
                            </Col>

                            {/* Cantidad */}
                            <Col xs={4} md={1} className="text-center text-md-start">
                                {component.producto ? component.producto.cantidad : "---"}
                            </Col>

                            {/* Subtotal */}
                            <Col xs={4} md={2} className="text-center text-md-start">
                                {component.producto ? `S/. ${component.producto.subtotal}` : "---"}
                            </Col>

                            {/* Stock */}
                            <Col xs={6} md={1} className="text-center text-md-start">
                                {component.producto ? (
                                    component.producto.stock > 0 ? (
                                        <span className="text-success fw-semibold">{component.producto.stock}</span>
                                    ) : (
                                        <span className="text-danger fw-semibold">No disponible</span>
                                    )
                                ) : "---"}
                            </Col>

                            {/* Tienda */}
                            <Col xs={6} md={2} className="text-center text-md-start">
                                {component.producto ? component.producto.nombreTienda : "---"}
                            </Col>
                        </Row>
                    </ListGroup.Item>
                ))}
            </ListGroup>
        </Card>
    );
};

export default BuildsTable;
