import React from "react";
import { Link } from "react-router-dom";

export default function CategoriaCard({ nombre, descripcion, nombreImagen }) {
    // Detectar si es una URL completa o un nombre de archivo local
    const isExternalUrl = nombreImagen && (nombreImagen.startsWith('http://') || nombreImagen.startsWith('https://'));
    const imagePath = isExternalUrl ? nombreImagen : `/assets/categorias/${nombreImagen}`;

    return (
        <article className="card h-100 border border-2 border-secondary-subtle rounded-5 shadow-sm overflow-hidden">
            <div className="ratio ratio-16x9">
                <img
                    src={imagePath}
                    alt={nombre}
                    className="img-fluid object-fit-cover rounded-top-5"
                />
            </div>

            <div className="card-body d-flex flex-column text-center p-4">
                <h3 className="card-title fw-bold mb-3">{nombre}</h3>

                <p className="card-text text-muted small mb-3">{descripcion}</p>

                <div className="mt-auto">
                    <Link
                        to={`/componentes?categoria=${encodeURIComponent(nombre)}`}
                        className="btn btn-primary btn-sm fw-semibold rounded-pill px-3"
                        aria-label={`Ver ${nombre}`}
                    >
                        <i className="bi bi-arrow-right-circle me-2"></i>
                        Ver {nombre}
                    </Link>
                </div>
            </div>
        </article>
    );
}
