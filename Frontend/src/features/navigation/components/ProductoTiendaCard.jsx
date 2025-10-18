import React from "react";

const ProductoTiendaCard = ({ producto }) => {
  const placeholder = "https://via.placeholder.com/200x150?text=Sin+Imagen";

  const handleVerProducto = () => {
    console.log("Ver producto:", producto.idProductoTienda);
    // Aquí puedes hacer una llamada al backend o navegar a otra ruta
    // Por ejemplo: navigate(`/producto/${producto.idProductoTienda}`)
  };

  return (
    <div className="card mb-3 shadow-sm">
      <div className="row g-0">
        <div className="col-md-4 d-flex align-items-center justify-content-center">
          <div
            style={{
              width: "200px",
              height: "150px",
              overflow: "hidden",
              borderRadius: "0.25rem"
            }}
          >
            <img
              src={producto.urlImagen || placeholder}
              alt={producto.nombreProducto}
              style={{
                width: "100%",
                height: "100%",
                objectFit: "cover"
              }}
            />
          </div>
        </div>
        <div className="col-md-8">
          <div className="card-body">
            <h5 className="card-title">{producto.nombreProducto}</h5>
            <p className="card-text mb-1">
              <strong>Precio:</strong> S/. {producto.precio}
            </p>
            <p className="card-text mb-1">
              <strong>Tienda:</strong> {producto.nombreTienda}
            </p>
            <p className="card-text">
              <small className={producto.stock > 0 ? "text-success" : "text-danger"}>
                {producto.stock > 0 ? `Stock: ${producto.stock}` : "Sin stock"}
              </small>
            </p>
            <button className="btn btn-outline-primary mt-2" onClick={handleVerProducto}>
              Ver producto
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductoTiendaCard;
