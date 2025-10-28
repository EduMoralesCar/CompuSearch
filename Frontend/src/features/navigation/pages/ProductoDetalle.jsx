import ProductoImagen from "../components/ProductoImagen";
import ProductoInfo from "../components/ProductoInfo";
import TablaTiendas from "../components/TablaTiendas";
import { useDatosProductos } from "../hooks/useDatosProductos";
import { Spinner } from "react-bootstrap";

const ProductoDetalle = () => {
    const { productoInfo, tiendas, imagenPrincipal, loading, error } = useDatosProductos();

    if (loading) {
        return (
            <div className="container text-center py-5">
                <Spinner animation="border" variant="primary" />
                <p className="mt-3">Cargando información del producto...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div className="container text-center py-5">
                <h2 className="text-danger fw-bold">Error al cargar</h2>
                <p className="text-muted">{error}</p>
            </div>
        );
    }

    if (!productoInfo) {
        return (
            <div className="container text-center py-5">
                <h2 className="fw-bold">Producto no encontrado</h2>
                <p className="text-muted">Verifica el enlace o intenta nuevamente.</p>
            </div>
        );
    }

    return (
        <div className="container py-5">
            <div className="row g-4 align-items-start">
                <div className="col-md-5">
                    <ProductoImagen
                        imagenUrl={imagenPrincipal}
                        nombre={productoInfo.nombreProducto}
                    />
                </div>

                <div className="col-md-7">
                    <ProductoInfo productoInfo={productoInfo} />
                </div>
            </div>

            <div className="mt-5">
                <h4 className="fw-bold mb-3">Disponibilidad en Tiendas</h4>
                <TablaTiendas tiendas={tiendas} />
            </div>
        </div>
    );
};

export default ProductoDetalle;
