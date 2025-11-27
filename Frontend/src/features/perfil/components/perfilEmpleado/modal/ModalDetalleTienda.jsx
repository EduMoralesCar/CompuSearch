import { useState, useMemo } from "react";
import { Modal, Button, Card, Badge, Container, Row, Col, Spinner } from "react-bootstrap";
import FormatDate from "../../../../../utils/FormatDate";
import ModalProductos from "./ModalProductos";
import { StatusBadge, VerifiedStatus } from "../table/TablaTiendas";

// Subcomponente para filas de detalle
const DetailRow = ({ icon, label, value }) => (
    <div className="d-flex align-items-start py-2">
        <i className={`fas ${icon} text-primary me-3 mt-1`}></i>
        <div>
            <p className="mb-0 fw-bold text-secondary small">{label}</p>
            <p className="mb-0 text-dark">{value || "—"}</p>
        </div>
    </div>
);

// Subcomponente para mostrar detalles de API
const TiendaAPIDetails = ({ tiendaAPI }) => {
    if (!tiendaAPI) return (
        <Card className="bg-light border-0 mb-3">
            <Card.Body className="py-3 text-center">
                <Badge bg="warning" text="dark" className="p-2 w-100">
                    La tienda aún no tiene configuración API
                </Badge>
            </Card.Body>
        </Card>
    );

    return (
        <Card className="border-0 bg-light mb-3">
            <Card.Body>
                <div className="mb-2">
                    <Badge bg={tiendaAPI.estadoAPI === "ACTIVA" ? "success" : "secondary"} className="me-2">
                        {tiendaAPI.estadoAPI}
                    </Badge>
                    <Badge bg="info">ID API: {tiendaAPI.idTiendaApi}</Badge>
                </div>
                <div className="mt-3">
                    <strong>URL Base:</strong>
                    <div className="text-muted">{tiendaAPI.urlBase}</div>
                </div>
            </Card.Body>
        </Card>
    );
};

// Subcomponente para listas de productos
const ProductosList = ({ productos, onVerTodos }) => {
    const productosVisibles = productos?.slice(0, 3) || [];

    return (
        <Card className="mb-3 shadow-sm">
            <Card.Header className="py-2 bg-light fw-semibold text-dark d-flex justify-content-between">
                <div><i className="fas fa-box me-2"></i> Productos ({productos?.length || 0})</div>
                {productos?.length > 3 && <Button size="sm" variant="outline-primary" onClick={onVerTodos}>Ver todos</Button>}
            </Card.Header>
            <Card.Body className="py-2">
                {productosVisibles.length ? (
                    productosVisibles.map((p, i) => (
                        <div key={i} className="mb-2">
                            <strong>{p.producto?.nombre || p.nombre}</strong>
                            <div className="text-muted small">S/. {p.precio}</div>
                        </div>
                    ))
                ) : (
                    <p className="text-muted small mb-0">Sin productos listados.</p>
                )}
            </Card.Body>
        </Card>
    );
};

// Subcomponente para mostrar etiquetas
const EtiquetasList = ({ etiquetas }) => (
    <Card className="mb-3 shadow-sm">
        <Card.Header className="py-2 bg-light fw-semibold text-dark">
            <i className="fas fa-tags me-2"></i> Etiquetas ({etiquetas?.length || 0})
        </Card.Header>
        <Card.Body className="py-2">
            {etiquetas?.length ? (
                etiquetas.map((e, i) => (
                    <span key={i} className="badge rounded-pill bg-primary text-white me-2 mb-2">
                        {e.nombre}
                    </span>
                ))
            ) : (
                <p className="text-muted small mb-0">Sin etiquetas.</p>
            )}
        </Card.Body>
    </Card>
);

const ModalDetalleTienda = ({ store, show, onClose, detailsLoading }) => {
    const [showModalProductos, setShowModalProductos] = useState(false);

    const ultimaSuscripcion = useMemo(() => store?.suscripcionActual || null, [store]);

    if (!store && !detailsLoading) return null;

    return (
        <>
            <ModalProductos
                show={showModalProductos}
                onClose={() => setShowModalProductos(false)}
                productos={store?.productos}
            />

            <Modal show={show} onHide={onClose} size="lg" centered>
                <Modal.Header closeButton className="bg-primary text-white">
                    <Modal.Title className="h5">
                        <i className="fas fa-store me-2"></i>
                        {store?.nombre || "Cargando..."}
                    </Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    {detailsLoading ? (
                        <div className="text-center py-5">
                            <Spinner animation="border" variant="primary" />
                            <p className="mt-2 text-muted">Cargando detalles...</p>
                        </div>
                    ) : (
                        <Container fluid>
                            <Row>
                                <Col md={6}>
                                    <h3 className="h6 text-primary border-bottom mb-3">Información General</h3>
                                    <Card className="mb-3 shadow-sm">
                                        <Card.Body className="p-3">
                                            <DetailRow icon="fa-id-card" label="ID Usuario" value={store.idUsuario} />
                                            <DetailRow icon="fa-envelope" label="Email" value={store.email} />
                                            <DetailRow icon="fa-phone" label="Teléfono" value={store.telefono} />
                                            <DetailRow icon="fa-map-marker-alt" label="Dirección" value={store.direccion} />
                                            <DetailRow icon="fa-globe" label="Página Web" value={store.pagina} />
                                            <DetailRow icon="fa-clock" label="Fecha de Afiliación" value={FormatDate(store.fechaAfiliacion)} />
                                        </Card.Body>
                                    </Card>

                                    <h3 className="h6 text-primary border-bottom mb-3">Suscripción Actual</h3>
                                    <Card className="mb-3 shadow-sm">
                                        <Card.Body>
                                            {ultimaSuscripcion ? (
                                                <>
                                                    <DetailRow icon="fa-calendar" label="Inicio" value={FormatDate(ultimaSuscripcion.fechaInicio)} />
                                                    <DetailRow icon="fa-calendar-check" label="Fin" value={ultimaSuscripcion.fechaFin ? FormatDate(ultimaSuscripcion.fechaFin) : "No finalizada"} />
                                                    <DetailRow icon="fa-check-circle" label="Estado" value={ultimaSuscripcion.estado} />
                                                    <DetailRow icon="fa-box-open" label="Plan" value={ultimaSuscripcion.nombrePlan} />
                                                </>
                                            ) : (
                                                <p className="text-muted mb-0">No tiene suscripción registrada.</p>
                                            )}
                                        </Card.Body>
                                    </Card>
                                </Col>

                                <Col md={6}>
                                    <h3 className="h6 text-primary border-bottom mb-3">Estado y Métricas</h3>
                                    <Card className="mb-3 shadow-sm">
                                        <Card.Body>
                                            <p className="mb-2"><span className="fw-semibold">Activo:</span> <StatusBadge isActive={store.activo} /></p>
                                            <p className="mb-2"><span className="fw-semibold">Verificado:</span> <VerifiedStatus isVerified={store.verificado} /></p>
                                        </Card.Body>
                                    </Card>

                                    <EtiquetasList etiquetas={store.etiquetas} />
                                    <ProductosList productos={store.productos} onVerTodos={() => setShowModalProductos(true)} />

                                    <h3 className="h6 text-primary border-bottom mb-2">Configuración API</h3>
                                    <TiendaAPIDetails tiendaAPI={store.tiendaAPI} />
                                </Col>
                            </Row>
                        </Container>
                    )}
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary" onClick={onClose}>Cerrar</Button>
                </Modal.Footer>
            </Modal>
        </>
    );
};

export default ModalDetalleTienda;
