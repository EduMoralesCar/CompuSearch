import React from "react";
import { Container, Row, Col, Card, Button, ListGroup } from "react-bootstrap";
import { FaShareAlt, FaHeart } from "react-icons/fa";

const Builds = () => {
  const components = [
    { name: "Procesador (CPU)", placeholder: "Elegir CPU" },
    { name: "Disipador CPU", placeholder: "Elegir Disipador" },
    { name: "Placa Madre", placeholder: "Elegir Motherboard" },
    { name: "Memoria RAM", placeholder: "Elegir RAM" },
    { name: "Almacenamiento", placeholder: "Elegir Storage" },
    { name: "Case/Gabinete", placeholder: "Elegir GPU" },
    { name: "Fuente de poder", placeholder: "Elegir Case" },
    { name: "Tarjeta Gráfica", placeholder: "Elegir Fuente de poder" },
    { name: "Otros", placeholder: "Elige otros productos" },
  ];

  return (
    <Container className="my-4">
      <Row className="mb-4">
        {/* Título, Compatibilidad y Consumo */}
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

        {/* Card de "Tu construcción" */}
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

      {/* Tabla de componentes */}
      <Card className="shadow-sm">
        <ListGroup variant="flush">
          {/* Encabezados solo visibles en md+ */}
          <ListGroup.Item className="d-none d-md-flex fw-bold text-uppercase bg-light">
            <Row className="w-100">
              <Col md={2}>COMPONENTE</Col>
              <Col md={3}>SELECCIÓN</Col>
              <Col md={2}>PRECIO</Col>
              <Col md={2}>DISPONIBILIDAD</Col>
              <Col md={3}>TIENDA</Col>
            </Row>
          </ListGroup.Item>

          {/* Lista de componentes */}
          {components.map((component, index) => (
            <ListGroup.Item key={index}>
              <Row className="align-items-center gy-2">
                {/* Nombre */}
                <Col xs={12} md={2} className="fw-bold">
                  {component.name}
                </Col>

                {/* Botón selección */}
                <Col xs={12} md={3}>
                  <Button variant="outline-primary" className="w-100 rounded-5">
                    + {component.placeholder}
                  </Button>
                </Col>

                {/* Precio */}
                <Col
                  xs={4}
                  md={2}
                  className="text-muted text-center text-md-start"
                >
                  ---
                </Col>

                {/* Disponibilidad */}
                <Col
                  xs={4}
                  md={2}
                  className="text-muted text-center text-md-start"
                >
                  ---
                </Col>

                {/* Tienda */}
                <Col
                  xs={4}
                  md={3}
                  className="text-muted text-center text-md-start"
                >
                  ---
                </Col>
              </Row>
            </ListGroup.Item>
          ))}
        </ListGroup>
      </Card>
    </Container>
  );
};

export default Builds;
