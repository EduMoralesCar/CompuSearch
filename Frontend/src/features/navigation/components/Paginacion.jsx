import React from "react";

const Paginacion = ({ page, totalPages, onPageChange }) => {
  if (totalPages === 0) {
    return null;
  }

  const hayAnterior = page > 0;
  const haySiguiente = page + 1 < totalPages;

  return (
    <div className="d-flex justify-content-between align-items-center mt-3 mb-5">
      
      {/* --- Botón Anterior --- */}
      <button
        className="btn btn-outline-primary"
        style={{ visibility: hayAnterior ? "visible" : "hidden" }}
        onClick={() => onPageChange(page - 1)}
        disabled={!hayAnterior}
      >
        Anterior
      </button>

      {/* --- Texto de Página --- */}
      <span>
        Página {page + 1} de {totalPages}
      </span>

      {/* --- Botón Siguiente --- */}
      <button
        className="btn btn-outline-primary"
        style={{ visibility: haySiguiente ? "visible" : "hidden" }}
        onClick={() => onPageChange(page + 1)}
        disabled={!haySiguiente}
      >
        Siguiente
      </button>
    </div>
  );
};

export default Paginacion;