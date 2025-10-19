import React from "react";
import { Link } from "react-router-dom";

const ProductoTiendaCard = ({ producto }) => {
  const placeholder = "https://via.placeholder.com/200x150?text=Sin+Imagen";

  const linkDestino = `/producto/${encodeURIComponent(producto.nombreProducto)}`;

  return (
    <Link to={linkDestino} className="text-decoration-none text-dark h-100">
      <div className="card shadow-sm h-100">
        <img
          src={producto.urlImagen || placeholder}
          alt={producto.nombreProducto}
          className="card-img-top"
          style={{
            height: "200px",
            objectFit: "contain",
          }}
        />

        <div className="card-body">
          <h5 className="card-title">{producto.nombreProducto}</h5>
          <p className="card-text mb-1">
            <strong>Precio:</strong> S/. {producto.precio}
          </p>
          <p className="card-text mb-1">
            <strong>Tienda:</strong> {producto.nombreTienda}
          </p>
          <p className="card-text">
            <small
              className={producto.stock > 0 ? "text-success" : "text-danger"}
            >
              {producto.stock > 0 ? `Stock: ${producto.stock}` : "Sin stock"}
            </small>
          </p>
        </div>
      </div>
    </Link>
  );
};

export default ProductoTiendaCard;