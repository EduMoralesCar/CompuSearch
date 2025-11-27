import { Container, Row, Col, Button, Card, Spinner } from "react-bootstrap";
import { FiFileText, FiArchive, FiBarChart2 } from "react-icons/fi";
import useReportesTiendas from "../../../hooks/useReportesTienda";

const ReportesTienda = ({ idTienda }) => {

    const { 
        obtenerCatalogo, 
        obtenerStockBajo, 
        obtenerMetricas, 
        isLoading, 
        error 
    } = useReportesTiendas();

    return (
        <Container className="py-4">
            
            {/* Encabezado estilo ObtenerPlan */}
            <Row className="mb-4">
                <Col>
                    <h2 className="fw-bold text-primary mb-1">Reportes de la Tienda</h2>
                    <p className="text-muted mb-0">
                        Descarga reportes detallados para analizar el rendimiento de tus productos.
                    </p>
                </Col>
            </Row>

            {/* Contenedor principal */}
            <Card className="shadow-sm border-0 p-3">
                {error && <p className="text-danger">{error}</p>}

                <Row className="g-3">
                    <Col md={4}>
                        <Button 
                            variant="primary" 
                            className="w-100 d-flex align-items-center justify-content-center gap-2"
                            onClick={() => obtenerCatalogo(idTienda)}
                            disabled={isLoading}
                        >
                            <FiFileText size={20} />
                            {isLoading ? (
                                <>
                                    <Spinner animation="border" size="sm" /> Generando…
                                </>
                            ) : (
                                "Catálogo de Productos"
                            )}
                        </Button>
                    </Col>

                    <Col md={4}>
                        <Button 
                            variant="warning" 
                            className="w-100 d-flex align-items-center justify-content-center gap-2"
                            onClick={() => obtenerStockBajo(idTienda)}
                            disabled={isLoading}
                        >
                            <FiArchive size={20} />
                            {isLoading ? (
                                <>
                                    <Spinner animation="border" size="sm" /> Generando…
                                </>
                            ) : (
                                "Productos Bajo Stock"
                            )}
                        </Button>
                    </Col>

                    <Col md={4}>
                        <Button 
                            variant="success" 
                            className="w-100 d-flex align-items-center justify-content-center gap-2"
                            onClick={() => obtenerMetricas(idTienda)}
                            disabled={isLoading}
                        >
                            <FiBarChart2 size={20} />
                            {isLoading ? (
                                <>
                                    <Spinner animation="border" size="sm" /> Generando…
                                </>
                            ) : (
                                "Métricas de Productos"
                            )}
                        </Button>
                    </Col>
                </Row>
            </Card>
        </Container>
    );
};

export default ReportesTienda;
