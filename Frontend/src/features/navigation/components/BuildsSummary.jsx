import React from "react";
import { Row, Col, Card, Button, Spinner } from "react-bootstrap";
import { FaDownload, FaTrash, FaSync, FaUpload } from "react-icons/fa";

const BuildsSummary = ({
  totalCosto,
  totalConsumo,
  resultadoCompat,
  buildPresente,
  armadoIncompleto,
  faltantes,
  componentesObligatorios,
  mensajeBuild,
  loadingBuild,
  onGuardarBuild,
  onDescargarBuild,
  onActualizarBuild,
  onEliminarBuild,
  setShowModalBuild,
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
                <Card bg={resultadoCompat.compatible ? "success" : "danger"} text="white" className="text-center">
                  <Card.Body>
                    <strong>COMPATIBILIDAD:</strong>{" "}
                    {resultadoCompat.compatible
                      ? "Todos los componentes son compatibles"
                      : "Hay incompatibilidades"}
                  </Card.Body>
                </Card>
              </Col>
              <Col xs={6}>
                <Card bg="dark" text="white" className="text-center">
                  <Card.Body>
                    <strong>CONSUMO:</strong> {totalConsumo} W
                  </Card.Body>
                </Card>
              </Col>
            </Row>

            {!resultadoCompat.compatible && resultadoCompat.errores?.length > 0 && (
              <Card className="mt-3 border-danger">
                <Card.Header className="bg-danger text-white fw-bold text-center">
                  Detalles de incompatibilidad
                </Card.Header>
                <Card.Body>
                  <ul className="text-danger fw-semibold ps-3">
                    {resultadoCompat.errores.map((error, index) => (
                      <li key={index}>{error}</li>
                    ))}
                  </ul>
                </Card.Body>
              </Card>
            )}
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
              <span className="fw-bold">S/ {totalCosto.toFixed(2)}</span>
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
                className={`text-center fw-semibold ${mensajeBuild.startsWith("¡") ? "text-success" : "text-danger"}`}
              >
                {mensajeBuild}
              </div>
            )}

            <Button
              variant="primary"
              onClick={onGuardarBuild}
              disabled={loadingBuild || armadoIncompleto || buildPresente}
            >
              {loadingBuild ? <Spinner size="sm" animation="border" className="me-2" /> : null}
              Guardar armado
            </Button>

            <Button
              variant="outline-success"
              onClick={onActualizarBuild}
              disabled={loadingBuild || armadoIncompleto}
            >
              <FaSync className="me-2" />
              Actualizar armado
            </Button>

            <Button
              variant="outline-danger"
              onClick={onEliminarBuild}
              disabled={loadingBuild || !buildPresente}
            >
              <FaTrash className="me-2" />
              Eliminar armado
            </Button>

            <Button
              variant="outline-secondary"
              onClick={() => setShowModalBuild(true)}
              disabled={loadingBuild}
            >
              <FaUpload className="me-2" />
              Cargar armado
            </Button>

            <Button
              variant="outline-primary"
              onClick={onDescargarBuild}
              disabled={loadingBuild || !buildPresente}
            >
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
