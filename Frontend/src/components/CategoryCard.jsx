import React from "react";
import { Link } from "react-router-dom";

/**
 * src/components/CategoryCard.jsx
 * Versión redondeada con borde y diseño moderno.
 * Solo usa clases Bootstrap (sin estilos ni CSS adicional).
 */



export default function CategoryCard({ id, title, description, subcategories = [], img }) {
  return (
    <article className="card h-100 border border-2 border-secondary-subtle rounded-5 shadow-sm overflow-hidden">
        
      <div className="ratio ratio-16x9">
        <img
          src={img}
          alt={title}
          className="img-fluid object-fit-cover rounded-top-5"
        />
      </div>

      <div className="card-body d-flex flex-column text-center p-4">
        <h3 className="card-title fw-bold mb-3">
          {title}
        </h3>

        <p className="card-text text-muted small mb-3">
          {description}
        </p>

        {subcategories && subcategories.length > 0 && (
          <div className="mb-4">
            <strong className="small d-block text-dark mb-1">Subcategorías:</strong>
            <ul className="list-unstyled text-muted small mb-0">
              {subcategories.map((s, i) => (
                <li key={i}>{s}</li>
              ))}
            </ul>
          </div>
        )}

        <div className="mt-auto">
          <Link
            to={`/categorias/${id}`}
            className="btn btn-primary btn-sm fw-semibold rounded-pill px-3"
            aria-label={`Ver ${title}`}
          >
            <i className="bi bi-arrow-right-circle me-2"></i>
            Ver {title}
          </Link>
        </div>
      </div>
    </article>
  );
}
