import React from "react";

const ProductoTiendaCard = ({ producto }) => {
  const placeholder = "https://via.placeholder.com/200x150?text=Sin+Imagen"; // 👈 placeholder

  return (
    <div className="card mb-3 shadow-sm">
      <div className="row g-0">
        <div className="col-md-4 d-flex align-items-center justify-content-center">
          <div
            style={{
              width: "200px",   // 👈 ancho fijo
              height: "150px",  // 👈 alto fijo
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
                objectFit: "cover" // 👈 recorta sin deformar
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
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductoTiendaCard;
