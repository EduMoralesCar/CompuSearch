import ProductoImagen from "../components/productoDetalle/ProductoImagen";
import ProductoInfo from "../components/productoDetalle/ProductoInfo";
import TablaTiendas from "../components/productoDetalle/TablaTiendas";
import { useDatosProductos } from "../hooks/useDatosProductos";

const ProductoDetalle = () => {
    const { productoInfo, tiendas, imagenPrincipal, loading, error } = useDatosProductos();

    if (loading) return <div className="container mt-5"><p>Cargando...</p></div>;
    if (error) return (
        <div className="container mt-5 text-center">
            <h2 className="text-danger">Error</h2>
            <p>{error}</p>
        </div>
    );
    if (!productoInfo) return (
        <div className="container mt-5 text-center">
            <h2>Producto no encontrado</h2>
        </div>
    );

    return (
        <div className="container mt-5 mb-5">
            <div className="row">
                <div className="col-md-5">
                    <ProductoImagen imagenUrl={imagenPrincipal} nombre={productoInfo.nombreProducto} />
                </div>
                <div className="col-md-7">
                    <ProductoInfo productoInfo={productoInfo} />
                </div>
            </div>
            <TablaTiendas tiendas={tiendas} />
        </div>
    );
};

export default ProductoDetalle;
