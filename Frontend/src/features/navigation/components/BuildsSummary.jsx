import React from "react";
import { Row, Col, Card, Button } from "react-bootstrap";
import { FaDownload } from "react-icons/fa";

const BuildsSummary = ({
  totalArmado,
  armadoIncompleto,
  faltantes,
  componentesObligatorios,
  mensajeBuild,
  loadingBuild,
  onGuardarBuild,
  onDescargarBuild
}) => {
  return (
    <Row>
      <Col lg={8} className="d-flex flex-column gap-3 mb-3 mb-lg-0">
        <h2 className="fw-bold text-lg-start text-center">
          SELECCIONE TUS COMPONENTES
        </h2>
        <p className="text-muted h5 text-lg-start text-center">
          Selecciona componentes compatibles para armar tu computadora ideal.
        </p>

        <Row className="justify-content-center justify-content-lg-start">
          <Col md={10} lg={8}>
            <Row className="g-3">
              <Col xs={6}>
                <Card bg="dark" text="white" className="text-center">
                  <Card.Body>
                    <strong>COMPATIBILIDAD:</strong> 0%
                  </Card.Body>
                </Card>
              </Col>
              <Col xs={6}>
                <Card bg="dark" text="white" className="text-center">
                  <Card.Body>
                    <strong>CONSUMO:</strong> 0 W
                  </Card.Body>
                </Card>
              </Col>
            </Row>
          </Col>
        </Row>
      </Col>

      <Col lg={4}>
        <Card className="bg-light shadow-sm h-100">
          <Card.Header>
            <h4 className="fw-bold text-primary text-center mb-3">
              Tu construcción
            </h4>
            <div className="d-flex justify-content-between">
              <span className="fw-bold">Total:</span>
              <span className="fw-bold">S/ {totalArmado.toFixed(2)}</span>
            </div>
          </Card.Header>
          <Card.Body className="d-grid gap-2">
            {armadoIncompleto && (
              <div className="text-danger fw-semibold text-center">
                {faltantes.length === componentesObligatorios.length
                  ? "Tu armado está vacío. Selecciona componentes esenciales para continuar."
                  : `Faltan componentes esenciales: ${faltantes.join(", ")}`}
              </div>
            )}
            {mensajeBuild && (
              <div
                className={`text-center fw-semibold ${mensajeBuild.startsWith("¡") ? "text-success" : "text-danger"
                  }`}
              >
                {mensajeBuild}
              </div>
            )}

            <Button
              variant="primary"
              onClick={onGuardarBuild}
              disabled={loadingBuild}
            >
              Guardar armado
            </Button>

            <Button variant="outline-primary" onClick={onDescargarBuild}>
              <FaDownload className="me-2" />
              Descargar armado
            </Button>
          </Card.Body>
        </Card>
      </Col>
    </Row>
  );
};

export default BuildsSummary;
