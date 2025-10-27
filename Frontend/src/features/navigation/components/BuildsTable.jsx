import React from "react";
import { Card, ListGroup, Row, Col, Button, Form } from "react-bootstrap";
import { BsTrash, BsArrowClockwise, BsLink45Deg } from "react-icons/bs";

const BuildsTable = ({
    categorias,
    armado,
    onSelectCategoria,
    onReiniciarCategoria,
    nombreBuild,
    onCambiarNombreBuild,
    onResetArmado,
    onRestablecerTabla
}) => {
    const components = categorias.map((nombre) => ({
        name: nombre,
        placeholder: `Elegir ${nombre}`,
        producto: armado[nombre] || null
    }));

    return (
        <Card className="shadow-sm">
            <Card.Header className="bg-primary text-white">
                <Row className="align-items-center">
                    <Col xs={12} md={4}>
                        <strong>Nombre del armado:</strong>
                    </Col>
                    <Col xs={12} md={8} className="d-flex gap-2">
                        <Form.Control
                            type="text"
                            placeholder="Ej: Mi PC Gamer"
                            value={nombreBuild}
                            onChange={(e) => onCambiarNombreBuild(e.target.value)}
                        />
                        <Button variant="outline-light" onClick={onResetArmado}>
                            <BsArrowClockwise />

                        </Button>
                        <Button variant="outline-warning" onClick={onRestablecerTabla}>
                            <BsTrash />
                        </Button>
                    </Col>
                </Row>
            </Card.Header>

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
                        <Col md={1}>Tienda</Col>
                        <Col md={1}>Enlace</Col>
                    </Row>
                </ListGroup.Item>

                {components.map((component, index) => (
                    <ListGroup.Item key={index}>
                        <Row className="align-items-center gy-3">
                            <Col xs={12} md={2}>
                                <Button
                                    variant="outline-primary"
                                    className="w-100 rounded-pill"
                                    onClick={() => onSelectCategoria(component.name)}
                                >
                                    + {component.placeholder}
                                </Button>
                            </Col>

                            <Col xs={12} md={3}>
                                {component.producto ? (
                                    <div className="d-flex justify-content-between align-items-center">
                                        <span className="text-success fw-semibold">
                                            {component.producto.nombreProducto}
                                        </span>
                                        <Button
                                            variant="outline-danger"
                                            size="sm"
                                            onClick={() => onReiniciarCategoria(component.name)}
                                        >
                                            <BsTrash />
                                        </Button>
                                    </div>
                                ) : (
                                    <span className="text-muted">---</span>
                                )}
                            </Col>

                            <Col xs={4} md={1} className="text-center text-md-start">
                                {component.producto ? `S/. ${component.producto.precio}` : "---"}
                            </Col>

                            <Col xs={4} md={1} className="text-center text-md-start">
                                {component.producto ? component.producto.cantidad : "---"}
                            </Col>

                            <Col xs={4} md={2} className="text-center text-md-start">
                                {component.producto ? `S/. ${component.producto.subtotal}` : "---"}
                            </Col>

                            <Col xs={6} md={1} className="text-center text-md-start">
                                {component.producto ? (
                                    component.producto.stock > 0 ? (
                                        <span className="text-success fw-semibold">{component.producto.stock}</span>
                                    ) : (
                                        <span className="text-danger fw-semibold">No disponible</span>
                                    )
                                ) : "---"}
                            </Col>

                            <Col xs={6} md={1} className="text-center text-md-start">
                                {component.producto ? component.producto.nombreTienda : "---"}
                            </Col>

                            <Col xs={12} md={1} className="text-center">
                                {component.producto?.urlProducto ? (
                                    <a
                                        href={component.producto.urlProducto}
                                        target="_blank"
                                        rel="noopener noreferrer"
                                        className="btn btn-sm btn-outline-secondary"
                                    >
                                        <BsLink45Deg />
                                    </a>
                                ) : (
                                    "---"
                                )}
                            </Col>
                        </Row>
                    </ListGroup.Item>
                ))}
            </ListGroup>
        </Card>
    );
};

export default BuildsTable;
