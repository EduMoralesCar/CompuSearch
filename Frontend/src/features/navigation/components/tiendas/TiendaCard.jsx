import React from 'react';

import Tienda_Sin_Logo from "../../../../assets/logo/Tienda_Sin_Logo.jpg";

export default function TiendaCard({
    nombre,
    descripcion,
    telefono,
    direccion,
    logo,
    urlPagina,
    etiquetas = [],
}) {
    const imgSrc = (logo && logo.trim() !== "") 
        ? `data:image/png;base64,${logo}`
        : Tienda_Sin_Logo;

    return (
        <article className="card h-100 rounded-3 border shadow-sm">
            <div className="p-3">
                <div className="ratio ratio-16x9">
                    <img
                        src={imgSrc}
                        alt={nombre}
                        className="img-fluid object-fit-cover rounded"
                        onError={(e) => {
                            e.target.onerror = null;
                            e.target.src = Tienda_Sin_Logo; 
                        }}
                    />
                </div>
            </div>

            <div className="card-body d-flex flex-column">
                <h5 className="card-title text-uppercase text-center mb-2 h6 fw-semibold">{nombre}</h5>
                <p className="card-text text-muted small mb-2">{descripcion}</p>

                {etiquetas.length > 0 && (
                    <div className="mb-3 text-center">
                        {etiquetas.map((etiqueta) => (
                            <span key={etiqueta.idEtiqueta} className="badge bg-primary rounded-pill me-1 mb-1">
                                {etiqueta.nombre}
                            </span>
                        ))}
                    </div>
                )}

                <ul className="list-unstyled small text-muted mb-3">
                    <li>📍 {direccion}</li>
                    <li>📞 {telefono}</li>
                </ul>

                <div className="mt-auto d-flex justify-content-center">
                    {urlPagina ? (
                        <a
                            href={urlPagina}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="btn btn-primary btn-sm"
                            aria-label={`Abrir ${nombre}`}
                        >
                            <i className="bi bi-box-arrow-up-right me-1" /> Visitar tienda
                        </a>
                    ) : (
                        <span className="text-muted small">Sin página web</span>
                    )}
                </div>
            </div>
        </article>
    );
}