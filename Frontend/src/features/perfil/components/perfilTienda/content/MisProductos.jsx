import { useEffect, useState, useCallback } from "react";
import { Spinner, Alert, Row, Col, Container, Button } from "react-bootstrap";

import { useTiendas } from "../../../hooks/useTiendas";
import { useCategorias } from "../../../../navigation/hooks/useCategorias";

import FiltroProductos from "../auxiliar/FiltroProductos";
import ProductoCard from "../auxiliar/ProductoCard";
import ProductoModal from "../modal/ProductoModal";
import PaginacionProductos from "../auxiliar/PaginacionProductos";

const MisProductos = ({ idTienda }) => {
    const { obtenerProductosAdmin, cambiarHabilitadoProducto, obtenerProductosDesdeApi, loading, error } = useTiendas();
    const { obtenerCategorias } = useCategorias();

    const [productosPage, setProductosPage] = useState(null);
    const [categorias, setCategorias] = useState([]);
    const [selected, setSelected] = useState(null);

    const [page, setPage] = useState(0);
    const [size] = useState(12);
    const [categoria, setCategoria] = useState("");
    const [sort, setSort] = useState("precio,asc");
    const [loadingApi, setLoadingApi] = useState(false);

    // Cargar categorías
    useEffect(() => {
        const fetchCategorias = async () => {
            const resp = await obtenerCategorias();
            if (resp.success) setCategorias(resp.response);
        };
        fetchCategorias();
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    // Cargar productos según filtros/paginación
    const fetchProductos = useCallback(async () => {
        const resp = await obtenerProductosAdmin(idTienda, page, size, categoria || null, sort);
        if (resp.success) setProductosPage(resp.data);
    }, [idTienda, page, size, categoria, sort, obtenerProductosAdmin]);

    useEffect(() => { fetchProductos(); }, [fetchProductos]);

    // Toggle habilitado
    const handleToggleHabilitado = async (producto) => {
        const resp = await cambiarHabilitadoProducto(producto.idProductoTienda, !producto.habilitado);
        if (resp.success) {
            setProductosPage(prev => ({
                ...prev,
                content: prev.content.map(p =>
                    p.idProductoTienda === producto.idProductoTienda ? { ...p, habilitado: !p.habilitado } : p
                )
            }));
        }
    };

    const limpiarFiltros = () => {
        setCategoria("");
        setSort("precio,asc");
        setPage(0);
    };

    // Actualizar desde API externa
    const handleActualizarApi = async () => {
        setLoadingApi(true);
        const resp = await obtenerProductosDesdeApi(idTienda);
        if (resp.success) await fetchProductos();
        setLoadingApi(false);
    };

    const productos = productosPage?.content ?? [];

    return (
        <Container className="py-4">

            {/* Encabezado y botón actualizar */}
            <Row className="mb-4 align-items-center">
                <Col>
                    <h2 className="fw-bold text-primary mb-1">Mis productos</h2>
                    <p className="text-muted mb-0">Administra, organiza y controla los productos de tu tienda.</p>
                </Col>
                <Col xs="auto">
                    <Button variant="dark" onClick={handleActualizarApi} disabled={loadingApi}>
                        {loadingApi ? (
                            <>
                                <Spinner size="sm" animation="border" className="me-2" />
                                Actualizando...
                            </>
                        ) : "Actualizar desde API"}
                    </Button>
                </Col>
            </Row>

            {/* Filtros */}
            <FiltroProductos
                categorias={categorias}
                categoria={categoria}
                setCategoria={setCategoria}
                sort={sort}
                setSort={setSort}
                limpiarFiltros={limpiarFiltros}
            />

            {/* Carga / error / vacío */}
            {loading && <div className="text-center"><Spinner animation="border" /></div>}
            {error && <Alert variant="danger" className="text-center">{error}</Alert>}
            {!loading && productos.length === 0 && (
                <Alert variant="info" className="text-center py-4">
                    <h4 className="fw-bold">No se encontraron productos</h4>
                    <p>Ajusta los filtros o agrega nuevos productos.</p>
                </Alert>
            )}

            {/* Lista de productos */}
            <Row xs={1} md={2} lg={3} className="g-4">
                {productos.map(prod => (
                    <Col key={prod.idProductoTienda}>
                        <ProductoCard prod={prod} onSelect={setSelected} onToggle={handleToggleHabilitado} />
                    </Col>
                ))}
            </Row>

            {/* Paginación */}
            <PaginacionProductos page={page} totalPages={productosPage?.totalPages} setPage={setPage} />

            {/* Modal */}
            <ProductoModal producto={selected} onClose={() => setSelected(null)} />
        </Container>
    );
};

export default MisProductos;
