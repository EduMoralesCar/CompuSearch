import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";

const ProductoDetalle = () => {
  // Obtener el 'slug' (nombre codificado) de la URL
  const { slug } = useParams();

  // Estados
  const [productoInfo, setProductoInfo] = useState(null);
  const [tiendas, setTiendas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  // Estado para la imagen seleccionada (aunque ahora solo hay 1)
  const [imagenPrincipal, setImagenPrincipal] = useState(null);

  useEffect(() => {
    // Decodificar el nombre del producto desde el slug
    const nombreProducto = decodeURIComponent(slug);

    if (!nombreProducto) {
      setError("Nombre de producto no válido.");
      setLoading(false);
      return;
    }

    const fetchDatosProducto = async () => {
      setLoading(true);
      setError(null);
      try {
        const resTiendas = await fetch(
          `http://localhost:8080/componentes/buscar?nombre=${encodeURIComponent(
            nombreProducto
          )}&size=20`
        );
        
        if (!resTiendas.ok) {
          throw new Error("Error al buscar el producto y sus tiendas.");
        }

        const dataTiendas = await resTiendas.json();
        
        if (dataTiendas.content.length === 0) {
          throw new Error("Producto no encontrado.");
        }

        // Ordenamos por precio de menor a mayor
        const tiendasOrdenadas = dataTiendas.content.sort((a, b) => a.precio - b.precio);
        setTiendas(tiendasOrdenadas);

        // CONSTRUIMOS LA INFO DEL PRODUCTO
        // Usamos la data del primer item (el más barato) para la info general
        const infoBase = tiendasOrdenadas[0];
        setProductoInfo(infoBase);
        setImagenPrincipal(infoBase.urlImagen);

      } catch (err) {
        console.error(err);
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchDatosProducto();
  }, [slug]); // Se ejecuta cada vez que el 'slug' cambia

  // Lógica de renderizado

  if (loading) {
    return <div className="container mt-5"><p>Cargando...</p></div>
  }

  if (error) {
    return (
      <div className="container mt-5 text-center">
        <h2 className="text-danger">Error</h2>
        <p>{error}</p>
      </div>
    );
  }

  if (!productoInfo) {
    return (
      <div className="container mt-5 text-center">
        <h2>Producto no encontrado</h2>
      </div>
    );
  }
  
  // Función para obtener el precio más bajo 
  const getPrecioMasBajo = () => {
    return productoInfo.precio.toFixed(2);
  };

  return (
    <div className="container mt-5 mb-5">
      <div className="row">
        {/* COLUMNA IZQUIERDA: IMÁGENES */}
        <div className="col-md-5">
          <div className="mb-3">
            <img
              src={imagenPrincipal || "https://via.placeholder.com/400?text=Sin+Imagen"}
              alt={productoInfo.nombreProducto}
              className="img-fluid rounded border"
              style={{ maxHeight: '450px', width: '100%', objectFit: 'contain' }}
            />
          </div>
        </div>

        {/* COLUMNA DERECHA: INFO PRODUCTO */}
        <div className="col-md-7">
          <h1 className="h2">{productoInfo.nombreProducto}</h1>
          
          <h2 className="text-primary my-3" style={{fontWeight: 600}}>
            S/ {getPrecioMasBajo()}
          </h2>
          
          <a href="#tiendas" className="btn btn-dark btn-lg mt-3 px-5 py-2">
            Consultar Disponibilidad
          </a>
        </div>
      </div>

      {/* SECCIÓN TIENDAS (TABLA) */}
      <div className="row mt-5" id="tiendas">
        <div className="col-12">
          <h3 className="mb-3">Disponibilidad en Tiendas</h3>
          <div className="table-responsive">
            <table className="table align-middle table-hover">
              <thead className="table-light">
                <tr>
                  <th scope="col">Tiendas</th>
                  <th scope="col">Precio</th>
                  <th scope="col">Stock</th>
                  <th scope="col"></th>
                </tr>
              </thead>
              <tbody>
                {tiendas.map((tienda) => (
                  <tr key={tienda.idProductoTienda}> 
                    <td><strong>{tienda.nombreTienda}</strong></td>
                    <td><strong>S/ {tienda.precio.toFixed(2)}</strong></td>
                    <td>
                      <span className={tienda.stock > 0 ? "text-success" : "text-danger"}>
                        {tienda.stock > 0 ? `En Stock (${tienda.stock})` : "Agotado"}
                      </span>
                    </td>
                    <td>
                      <a 
                        href={tienda.urlProducto} // Reemplaza con el campo correcto de la API(JESUS)
                        className="btn btn-outline-primary btn-sm"
                        target="_blank" 
                        rel="noopener noreferrer"
                      >
                        Consultar
                      </a>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
      
    </div>
  );
};

export default ProductoDetalle;