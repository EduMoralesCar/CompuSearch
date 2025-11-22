import React, { useState } from "react";
import { Link } from "react-router-dom";

const ProductoTiendaCard = ({ producto }) => {
  const placeholder = "https://via.placeholder.com/200x150?text=Sin+Imagen";
  const linkDestino = `/producto/${encodeURIComponent(producto.nombreProducto)}`;
  const [isHovered, setIsHovered] = useState(false);

  // Crear una descripción basada en los datos disponibles si no hay descripción
  const getDescripcion = () => {
    if (producto.descripcion) return producto.descripcion;

    return `${producto.nombreProducto} disponible en ${producto.nombreTienda}. ${producto.stock > 0
      ? `Stock disponible: ${producto.stock} unidades.`
      : 'Consultar disponibilidad.'
      }`;
  };

  return (
    <Link to={linkDestino} className="text-decoration-none text-dark h-100">
      <div
        className="card shadow-sm h-100 position-relative overflow-hidden"
        onMouseEnter={() => setIsHovered(true)}
        onMouseLeave={() => setIsHovered(false)}
        style={{ transition: 'transform 0.3s ease' }}
      >
        <img
          src={producto.urlImagen || placeholder}
          alt={producto.nombreProducto}
          className="card-img-top"
          style={{
            height: "200px",
            objectFit: "contain",
          }}
        />

        <div className="card-body d-flex flex-column">
          <h5 className="card-title">{producto.nombreProducto}</h5>
          <p className="card-text mb-1">
            <strong>Precio:</strong> S/. {producto.precio}
          </p>
          <p className="card-text mb-1">
            <strong>Tienda:</strong> {producto.nombreTienda}
          </p>
          <p className="card-text mb-2">
            <small className={producto.stock > 0 ? "text-success" : "text-danger"}>
              {producto.stock > 0 ? `Stock: ${producto.stock}` : "Sin stock"}
            </small>
          </p>

          <div className="btn btn-primary mt-auto text-center">Ver producto</div>
        </div>

        {/* Hover Overlay */}
        <div
          style={{
            position: 'absolute',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            background: 'linear-gradient(135deg, rgba(13, 110, 253, 0.95) 0%, rgba(13, 110, 253, 0.98) 100%)',
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'center',
            alignItems: 'center',
            padding: '1.5rem',
            opacity: isHovered ? 1 : 0,
            transform: isHovered ? 'translateY(0)' : 'translateY(10px)',
            transition: 'all 0.3s ease',
            pointerEvents: isHovered ? 'auto' : 'none',
            zIndex: 10,
          }}
        >
          <h5 style={{
            color: 'white',
            marginBottom: '1rem',
            textAlign: 'center',
            fontWeight: 'bold',
            textShadow: '0 2px 4px rgba(0,0,0,0.2)'
          }}>
            {producto.nombreProducto}
          </h5>
          <p style={{
            color: 'white',
            textAlign: 'center',
            fontSize: '0.95rem',
            lineHeight: '1.5',
            margin: 0,
            textShadow: '0 1px 2px rgba(0,0,0,0.1)'
          }}>
            {getDescripcion()}
          </p>
          <div
            className="btn btn-light mt-3"
            style={{
              fontWeight: 'bold',
              boxShadow: '0 4px 8px rgba(0,0,0,0.2)'
            }}
          >
            Ver detalles →
          </div>
        </div>
      </div>
    </Link>
  );
};

export default ProductoTiendaCard;
