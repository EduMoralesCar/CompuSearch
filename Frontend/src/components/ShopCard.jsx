import React from "react";
import { Link } from "react-router-dom";

/**
 * src/components/ShopCard.jsx
 *
 * Componente reutilizable para mostrar una tarjeta de tienda.
 * - Usar utilidades de Bootstrap y variables globales (definidas en main.jsx).
 * - No usar estilos inline.
 *
 * Props:
 *  - id: string
 *  - name: string
 *  - description: string
 *  - rating: number (0-5)
 *  - tags: string[] (etiquetas/beneficios)
 *  - shipping: string (texto de envío)
 *  - verified: boolean
 *  - img: string (ruta pública o URL)
 *  - url: string (opcional, enlace externo a la tienda)
 */
export default function ShopCard({
    id,
    name,
    description,
    rating = 0,
    tags = [],
    shipping = "",
    verified = false,
    img,
    url,
}) {
    // Render de estrellas según rating (0-5)
    const stars = Array.from({ length: 5 }).map((_, i) => {
        return (
            <i
                key={i}
                className={
                    i < Math.round(rating)
                        ? "bi bi-star-fill text-warning me-1"
                        : "bi bi-star text-warning me-1"
                }
                aria-hidden="true"
            />
        );
    });

    return (
        <article className="card h-100 rounded-3 border shadow-sm">
            <div className="p-3">
                <div className="ratio ratio-16x9">
                    <img src={img} alt={name} className="img-fluid object-fit-cover rounded" />
                </div>
            </div>

            <div className="card-body d-flex flex-column">
                <h5 className="card-title text-uppercase text-center mb-2 h6 fw-semibold">{name}</h5>

                <div className="text-center mb-2" aria-hidden="true">
                    {stars}
                </div>

                <p className="card-text text-muted small mb-2">{description}</p>

                {tags && tags.length > 0 && (
                    <div className="mb-3">
                        {tags.map((t, idx) => (
                            <span key={idx} className="badge bg-primary rounded-pill me-1 mb-1">
                                {t}
                            </span>
                        ))}
                    </div>
                )}

                <ul className="list-unstyled small text-muted mb-3">
                    {shipping && <li>Envío: {shipping}</li>}
                    <li>{verified ? "Producto verificado" : "Producto sin verificar"}</li>
                </ul>

                <div className="mt-auto d-flex justify-content-center">
                    {url ? (
                        <a
                            href={url}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="btn btn-primary btn-sm"
                            aria-label={`Abrir ${name}`}
                        >
                            <i className="bi bi-box-arrow-up-right me-1" /> Visitar tienda
                        </a>
                    ) : (
                        <Link to={`/tiendas/${id}`} className="btn btn-primary btn-sm" aria-label={`Ver ${name}`}>
                            <i className="bi bi-arrow-right-circle me-1" /> Ver tienda
                        </Link>
                    )}
                </div>
            </div>
        </article>
    );
}