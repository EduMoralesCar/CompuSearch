import React from "react";

const Paginacion = ({ page, totalPages, onPageChange }) => {
    return (
        <div className="d-flex justify-content-between mt-3 mb-5">
            <button
                className="btn btn-outline-primary"
                disabled={page === 0}
                onClick={() => onPageChange(page - 1)}
            >
                Anterior
            </button>
            <span>
                Página {page + 1} de {totalPages}
            </span>
            <button
                className="btn btn-outline-primary"
                disabled={page + 1 >= totalPages}
                onClick={() => onPageChange(page + 1)}
            >
                Siguiente
            </button>
        </div>
    );
};

export default Paginacion;
