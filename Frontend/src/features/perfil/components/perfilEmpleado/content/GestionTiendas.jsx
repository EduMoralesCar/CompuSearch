import { useState, useCallback, useEffect, useMemo } from 'react';
import { useTiendas } from "../../../hooks/useTiendas"
import {
    Container,
    Row,
    Col,
    Table,
    Button,
    Form,
    Modal,
    Spinner,
    Badge,
    Card,
    InputGroup,
    Pagination
} from 'react-bootstrap';

const ModalProductos = ({ show, onClose, productos }) => (
    <Modal show={show} onHide={onClose} size="lg" centered>
        <Modal.Header closeButton>
            <Modal.Title>
                <i className="fas fa-boxes me-2"></i>
                Productos de la Tienda
            </Modal.Title>
        </Modal.Header>

        <Modal.Body>
            {productos?.length > 0 ? (
                <ul className="list-group">
                    {productos.map((item, i) => (
                        <li key={i} className="list-group-item d-flex align-items-start">

                            {/* Imagen */}
                            <img
                                src={item.urlImagen}
                                alt={item.nombre}
                                className="me-3 rounded"
                                style={{ width: "70px", height: "70px", objectFit: "cover" }}
                            />

                            <div>
                                {/* Nombre */}
                                <strong className="d-block">{item.nombre}</strong>

                                {/* Categoría */}
                                <span className="text-muted d-block small">
                                    Categoría: {item.categoria}
                                </span>

                                {/* Precio + stock */}
                                <span className="text-primary fw-semibold d-block mt-1">
                                    Precio: S/. {item.precio} — Stock: {item.stock}
                                </span>

                                {/* Habilitado */}
                                <span className="small d-block mt-1">
                                    {item.habilitado ? (
                                        <Badge bg="success">Habilitado</Badge>
                                    ) : (
                                        <Badge bg="danger">Deshabilitado</Badge>
                                    )}
                                </span>
                            </div>
                        </li>
                    ))}
                </ul>
            ) : (
                <p className="text-muted">No hay productos.</p>
            )}
        </Modal.Body>

        <Modal.Footer>
            <Button variant="secondary" onClick={onClose}>
                Cerrar
            </Button>
        </Modal.Footer>
    </Modal>
);



function TiendaAPIDetails({ tiendaAPI }) {
    const [showSecrets, setShowSecrets] = useState(false);

    if (!tiendaAPI) {
        return (
            <Card className="bg-light border-0">
                <Card.Body className="py-3">
                    <Badge bg="warning" text="dark" className="p-2 w-100 text-center">
                        La tienda aún no tiene configuración API
                    </Badge>
                </Card.Body>
            </Card>
        );
    }

    return (
        <Card className="border-0 bg-light">
            <Card.Body>
                {/* Estado */}
                <div className="mb-2">
                    <Badge
                        bg={tiendaAPI.estadoAPI === "ACTIVA" ? "success" : "secondary"}
                        className="me-2"
                    >
                        {tiendaAPI.estadoAPI}
                    </Badge>

                    <Badge bg="info">ID API: {tiendaAPI.idTiendaApi}</Badge>
                </div>

                {/* URL Base */}
                <div className="mt-3">
                    <strong>URL Base:</strong>
                    <div className="text-muted">{tiendaAPI.urlBase}</div>
                </div>

                {/* Endpoints */}
                <div className="mt-3">
                    <strong>Endpoints:</strong>
                    <ul className="small text-muted ms-3">
                        <li>Catálogo: {tiendaAPI.endpointCatalogo || "—"}</li>
                        <li>Producto: {tiendaAPI.endpointProducto || "—"}</li>
                    </ul>
                </div>

                {/* Tipo de Autenticación */}
                <div className="mt-3">
                    <strong>Autenticación:</strong>
                    <Badge bg="dark" className="ms-2">
                        {tiendaAPI.tipoAutenticacion}
                    </Badge>
                </div>

                {/* Credenciales */}
                <div className="mt-3">
                    <strong>Credenciales:</strong>

                    <div className="mt-2 small">
                        {tiendaAPI.tipoAutenticacion === "API_KEY" && (
                            <div>
                                <strong>API Key:</strong>{" "}
                                {showSecrets ? tiendaAPI.apiKey : "••••••••••"}
                            </div>
                        )}

                        {tiendaAPI.tipoAutenticacion === "BEARER_TOKEN" && (
                            <div>
                                <strong>Bearer Token:</strong>{" "}
                                {showSecrets ? tiendaAPI.bearerToken : "••••••••••"}
                            </div>
                        )}

                        {tiendaAPI.tipoAutenticacion === "BASIC_AUTH" && (
                            <>
                                <div>
                                    <strong>Usuario:</strong>{" "}
                                    {showSecrets ? tiendaAPI.apiUsuario : "••••••••••"}
                                </div>
                                <div>
                                    <strong>Contraseña:</strong>{" "}
                                    {showSecrets ? tiendaAPI.apiContrasena : "••••••••••"}
                                </div>
                            </>
                        )}
                    </div>

                    <Button
                        variant="outline-secondary"
                        size="sm"
                        className="mt-2"
                        onClick={() => setShowSecrets(!showSecrets)}
                    >
                        {showSecrets ? "Ocultar" : "Mostrar"}
                    </Button>
                </div>
            </Card.Body>
        </Card>
    );
}


const formatDate = (localDateTimeString) => {
    if (!localDateTimeString) return 'N/A';
    const date = new Date(localDateTimeString);
    if (isNaN(date)) return localDateTimeString;
    return new Intl.DateTimeFormat('es-ES', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
    }).format(date);
};

const StatusBadge = ({ isActive }) => (
    <Badge bg={isActive ? 'success' : 'danger'} className="text-uppercase">
        {isActive ? 'Activa' : 'Inactiva'}
    </Badge>
);

const VerifiedStatus = ({ isVerified }) => (
    <span className={isVerified ? 'text-success' : 'text-warning'}>
        <i className={`fas fa-certificate me-1 ${isVerified ? 'text-success' : 'text-warning'}`}></i>
        {isVerified ? 'Verificada' : 'Sin Verificar'}
    </span>
);


const StoreDetailsModal = ({ store, show, onClose, detailsLoading }) => {
    const [showModalProductos, setShowModalProductos] = useState(false);

    if (!store && !detailsLoading) return null;

    const ultimaSuscripcion = store?.suscripcionActual || null;

    const DetailRow = ({ icon, label, value }) => (
        <div className="d-flex align-items-start py-2">
            <i className={`fas ${icon} text-primary me-3 mt-1`}></i>
            <div>
                <p className="mb-0 fw-bold text-secondary small">{label}</p>
                <p className="mb-0 text-dark">{value || "—"}</p>
            </div>
        </div>
    );

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
                                    {/* Información General */}
                                    <h3 className="h6 text-primary border-bottom mb-3">
                                        Información General
                                    </h3>

                                    <Card className="mb-3 shadow-sm">
                                        <Card.Body className="p-3">
                                            <DetailRow icon="fa-id-card" label="ID Usuario" value={store.idUsuario} />
                                            <DetailRow icon="fa-envelope" label="Email" value={store.email} />
                                            <DetailRow icon="fa-phone" label="Teléfono" value={store.telefono} />
                                            <DetailRow icon="fa-map-marker-alt" label="Dirección" value={store.direccion} />
                                            <DetailRow icon="fa-globe" label="Página Web" value={store.pagina} />
                                            <DetailRow icon="fa-clock" label="Fecha de Afiliación" value={formatDate(store.fechaAfiliacion)} />
                                        </Card.Body>
                                    </Card>

                                    {/* Última Suscripción */}
                                    <h3 className="h6 text-primary border-bottom mb-3">
                                        Suscripción Actual
                                    </h3>

                                    <Card className="mb-3 shadow-sm">
                                        <Card.Body>
                                            {ultimaSuscripcion ? (
                                                <>
                                                    <DetailRow
                                                        icon="fa-calendar"
                                                        label="Inicio"
                                                        value={formatDate(ultimaSuscripcion.fechaInicio)}
                                                    />
                                                    <DetailRow
                                                        icon="fa-calendar-check"
                                                        label="Fin"
                                                        value={
                                                            ultimaSuscripcion.fechaFin
                                                                ? formatDate(ultimaSuscripcion.fechaFin)
                                                                : "No finalizada"
                                                        }
                                                    />
                                                    <DetailRow
                                                        icon="fa-check-circle"
                                                        label="Estado"
                                                        value={ultimaSuscripcion.estado}
                                                    />
                                                    <DetailRow
                                                        icon="fa-box-open"
                                                        label="Plan"
                                                        value={ultimaSuscripcion.nombrePlan}
                                                    />
                                                </>
                                            ) : (
                                                <p className="text-muted mb-0">No tiene suscripción registrada.</p>
                                            )}
                                        </Card.Body>
                                    </Card>
                                </Col>

                                <Col md={6}>
                                    {/* Estado y Métricas */}
                                    <h3 className="h6 text-primary border-bottom mb-3">Estado y Métricas</h3>

                                    <Card className="mb-3 shadow-sm">
                                        <Card.Body>
                                            <p className="mb-2">
                                                <span className="fw-semibold">Activo:</span>{" "}
                                                <StatusBadge isActive={store.activo} />
                                            </p>
                                            <p className="mb-2">
                                                <span className="fw-semibold">Verificado:</span>{" "}
                                                <VerifiedStatus isVerified={store.verificado} />
                                            </p>
                                        </Card.Body>
                                    </Card>

                                    {/* Etiquetas */}
                                    <Card className="mb-3 shadow-sm">
                                        <Card.Header className="py-2 bg-light fw-semibold text-dark">
                                            <i className="fas fa-tags me-2"></i>
                                            Etiquetas ({store.etiquetas?.length || 0})
                                        </Card.Header>
                                        <Card.Body className="py-2">
                                            {store.etiquetas?.length ? (
                                                store.etiquetas.map((e, i) => (
                                                    <span
                                                        key={i}
                                                        className="badge rounded-pill bg-primary text-white me-2 mb-2"
                                                    >
                                                        {e.nombre}
                                                    </span>
                                                ))
                                            ) : (
                                                <p className="text-muted small mb-0">Sin etiquetas.</p>
                                            )}
                                        </Card.Body>
                                    </Card>

                                    {/* Productos */}
                                    <Card className="mb-3 shadow-sm">
                                        <Card.Header className="py-2 bg-light fw-semibold text-dark d-flex justify-content-between">
                                            <div>
                                                <i className="fas fa-box me-2"></i>
                                                Productos ({store.productos?.length || 0})
                                            </div>
                                            {store.productos?.length > 3 && (
                                                <Button
                                                    size="sm"
                                                    variant="outline-primary"
                                                    onClick={() => setShowModalProductos(true)}
                                                >
                                                    Ver todos
                                                </Button>
                                            )}
                                        </Card.Header>

                                        <Card.Body className="py-2">
                                            {store.productos?.length ? (
                                                store.productos.slice(0, 3).map((p, i) => (
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

                                    {/* API */}
                                    <h3 className="h6 text-primary border-bottom mb-2">Configuración API</h3>
                                    <TiendaAPIDetails tiendaAPI={store.tiendaAPI} />
                                </Col>
                            </Row>
                        </Container>
                    )}
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary" onClick={onClose}>
                        Cerrar
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
};

const GestionTiendas = () => {
    const {
        obtenerTiendasPaginadas,
        obtenerTiendaPorId,
        actualizarEstado,
        actualizarVerificacion,
        loading,
        error
    } = useTiendas();

    const [tiendasData, setTiendasData] = useState({ content: [], totalPages: 0, number: 0 });
    const [page, setPage] = useState(0);
    const [filterName, setFilterName] = useState('');
    const [currentFilter, setCurrentFilter] = useState('');
    const [modalStore, setModalStore] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [detailsLoading, setDetailsLoading] = useState(false);

    const pageSize = 10;

    // Función principal para cargar los datos
    const loadTiendas = useCallback(async (p, filters) => {
        const result = await obtenerTiendasPaginadas(p, pageSize, filters);
        if (result.success) {
            setTiendasData(result.data);
            setPage(result.data.number);
        } else {
            // Manejo de error ya se hace en el hook, solo mostramos en consola
            console.error("Fallo al cargar tiendas:", result.error);
        }
    }, [obtenerTiendasPaginadas]);

    // Carga inicial y al cambiar de página/filtros
    useEffect(() => {
        loadTiendas(page, { nombre: currentFilter });
    }, [page, currentFilter, loadTiendas]);

    // Manejar el cambio de filtros (se activa al presionar Buscar)
    const handleFilterChange = (e) => {
        e.preventDefault();
        setCurrentFilter(filterName);
        setPage(0); // Reiniciar a la primera página al filtrar
    };

    // Función para manejar el cambio de estado (Activo/Inactivo)
    const handleToggleEstado = async (idUsuario, nuevoEstado) => {
        const result = await actualizarEstado(idUsuario, nuevoEstado);
        if (result.success) {
            // Actualizar el estado en la lista local
            setTiendasData(prev => ({
                ...prev,
                content: prev.content.map(t =>
                    t.idUsuario === idUsuario ? { ...t, activo: nuevoEstado } : t
                )
            }));
        } else {
            alert(`Fallo al actualizar el estado: ${result.error}`);
        }
    };

    // Función para manejar el cambio de verificación
    const handleToggleVerificacion = async (idUsuario, nuevoEstado) => {
        const result = await actualizarVerificacion(idUsuario, nuevoEstado);
        if (result.success) {
            // Actualizar el estado en la lista local
            setTiendasData(prev => ({
                ...prev,
                content: prev.content.map(t =>
                    t.idUsuario === idUsuario ? { ...t, verificado: nuevoEstado } : t
                )
            }));
        } else {
            alert(`Fallo al actualizar la verificación: ${result.error}`);
        }
    };

    // Cargar detalles y abrir modal
    const viewDetails = async (idUsuario) => {
        setShowModal(true);
        setDetailsLoading(true);
        setModalStore(null); // Limpiar datos anteriores

        const result = await obtenerTiendaPorId(idUsuario);
        if (result.success) {
            setModalStore(result.data);
        } else {
            alert(`Fallo al cargar detalles: ${result.error}`);
            setShowModal(false);
        }
        setDetailsLoading(false);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setModalStore(null);
    };

    const totalPages = tiendasData.totalPages || 1;

    // Componente de Paginación
    const renderPagination = useMemo(() => {
        let items = [];
        const maxPagesToShow = 5;
        let startPage, endPage;

        if (totalPages <= maxPagesToShow) {
            startPage = 1;
            endPage = totalPages;
        } else {
            if (page <= Math.floor(maxPagesToShow / 2)) {
                startPage = 1;
                endPage = maxPagesToShow;
            } else if (page + Math.floor(maxPagesToShow / 2) >= totalPages) {
                startPage = totalPages - maxPagesToShow + 1;
                endPage = totalPages;
            } else {
                startPage = page - Math.floor(maxPagesToShow / 2) + 1;
                endPage = page + Math.ceil(maxPagesToShow / 2);
            }
        }

        for (let number = startPage; number <= endPage; number++) {
            items.push(
                <Pagination.Item
                    key={number}
                    active={number === page + 1}
                    onClick={() => setPage(number - 1)}
                    disabled={loading}
                >
                    {number}
                </Pagination.Item>,
            );
        }

        return (
            <div className="d-flex justify-content-center">
                <Pagination>
                    <Pagination.Prev
                        onClick={() => setPage(p => Math.max(0, p - 1))}
                        disabled={page === 0 || loading}
                    />
                    {items}
                    <Pagination.Next
                        onClick={() => setPage(p => Math.min(totalPages - 1, p + 1))}
                        disabled={page >= totalPages - 1 || loading}
                    />
                </Pagination>
            </div>
        );
    }, [page, totalPages, loading]);


    return (
        <Container fluid className="py-4">
            <header className="mb-4">
                <h1 className="display-6 fw-bold text-dark">
                    <i className="fas fa-cogs text-primary me-2"></i>
                    Panel de Administración de Tiendas
                </h1>
                <p className="text-muted">Gestión del estado y verificación de las tiendas afiliadas.</p>
            </header>

            {/* FILTROS Y ACCIONES */}
            <Card className="shadow-sm mb-4">
                <Card.Body>
                    <Form onSubmit={handleFilterChange}>
                        <Row className="align-items-center">
                            <Col md={10}>
                                <InputGroup>
                                    <Form.Control
                                        type="text"
                                        placeholder="Buscar por Nombre de Tienda..."
                                        value={filterName}
                                        onChange={(e) => setFilterName(e.target.value)}
                                        disabled={loading}
                                    />
                                    <Button
                                        type="submit"
                                        variant="primary"
                                        disabled={loading}
                                    >
                                        <i className="fas fa-search me-1"></i> Buscar
                                    </Button>
                                </InputGroup>
                            </Col>
                            <Col md={2} className="text-end mt-2 mt-md-0">
                                <Button
                                    onClick={() => loadTiendas(page, { nombre: currentFilter })}
                                    disabled={loading}
                                    variant="outline-secondary"
                                    title="Refrescar tabla"
                                    className="w-100 w-md-auto"
                                >
                                    <i className={`bi bi-arrow-repeat ${loading ? "spin" : ""}`}></i>

                                </Button>
                            </Col>
                        </Row>
                    </Form>
                </Card.Body>
            </Card>

            {/* MENSAJES DE ESTADO */}
            {error && (
                <div className="alert alert-danger" role="alert">
                    <i className="fas fa-exclamation-triangle me-2"></i>
                    <span className="fw-medium">Error:</span> {error}
                </div>
            )}


            {/* TABLA DE TIENDAS */}
            <Card className="shadow-lg">
                <Card.Body>
                    <Table responsive striped bordered hover className="mb-0">
                        <thead className="table-light">
                            <tr>
                                <th>ID / Nombre</th>
                                <th>Afiliación</th>
                                <th>Métricas</th>
                                <th className="text-center">Activa</th>
                                <th className="text-center">Verificada</th>
                                <th className="text-center">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            {loading && tiendasData.content.length === 0 && (
                                <tr>
                                    <td colSpan="6" className="text-center py-5">
                                        <Spinner animation="border" size="sm" variant="primary" className="me-2" />
                                        Cargando datos...
                                    </td>
                                </tr>
                            )}
                            {!loading && tiendasData.content.length === 0 && (
                                <tr>
                                    <td colSpan="6" className="text-center py-4 text-muted">
                                        No se encontraron tiendas.
                                    </td>
                                </tr>
                            )}
                            {!loading && tiendasData.content.map((tienda) => (
                                <tr key={tienda.idUsuario}>
                                    <td>
                                        <div className="fw-semibold">{tienda.nombre}</div>
                                        <div className="small text-muted">ID: {tienda.idUsuario} | {tienda.email}</div>
                                    </td>
                                    <td>
                                        <i className="far fa-calendar-alt me-1 text-secondary"></i>
                                        {formatDate(tienda.fechaAfiliacion)}
                                    </td>
                                    <td>
                                        <Badge bg="info" className="me-2">Prod: {tienda.productos}</Badge>
                                        <Badge bg="warning">Subs: {tienda.suscripciones}</Badge>
                                    </td>

                                    {/* TOGGLE ACTIVO */}
                                    <td className="text-center">
                                        <Form.Check
                                            type="switch"
                                            id={`activo-switch-${tienda.idUsuario}`}
                                            checked={tienda.activo}
                                            onChange={(e) => handleToggleEstado(tienda.idUsuario, e.target.checked)}
                                            disabled={loading}
                                            className="d-inline-block"
                                        />
                                        <div className="small mt-1"><StatusBadge isActive={tienda.activo} /></div>
                                    </td>

                                    {/* TOGGLE VERIFICADO */}
                                    <td className="text-center">
                                        <Form.Check
                                            type="switch"
                                            id={`verif-switch-${tienda.idUsuario}`}
                                            checked={tienda.verificado}
                                            onChange={(e) => handleToggleVerificacion(tienda.idUsuario, e.target.checked)}
                                            disabled={loading}
                                            className="d-inline-block"
                                        />
                                        <div className="small mt-1"><VerifiedStatus isVerified={tienda.verificado} /></div>
                                    </td>

                                    <td className="text-center">
                                        <Button
                                            onClick={() => viewDetails(tienda.idUsuario)}
                                            disabled={loading}
                                            variant="outline-primary"
                                            size="sm"
                                        >
                                            <i className="fas fa-eye me-1"></i> Ver
                                        </Button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>
                </Card.Body>
                <Card.Footer>
                    <Row className="align-items-center">
                        <Col md={6}>
                            <div className="small text-muted">
                                Total de páginas: {totalPages}. Mostrando {tiendasData.content.length} resultados.
                            </div>
                        </Col>
                        <Col md={6} className="text-md-end">
                            {tiendasData.content.length > 0 && renderPagination}
                        </Col>
                    </Row>
                </Card.Footer>
            </Card>

            {/* MODAL DE DETALLES */}
            <StoreDetailsModal
                store={modalStore}
                show={showModal}
                detailsLoading={detailsLoading}
                onClose={handleCloseModal}
            />
        </Container>
    );
};

export default GestionTiendas;