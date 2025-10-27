export default function TiendaFilters({
    searchTerm,
    setSearchTerm,
    etiquetaFilter,
    setEtiquetaFilter,
    etiquetasDisponibles = [],
}) {
    return (
        <div className="border border-2 border-secondary-subtle rounded-4 p-4 mb-5 bg-light shadow-sm">
            <div className="row g-3 align-items-end">
                <div className="col-12 col-md-6">
                    <label className="form-label small fw-semibold text-secondary mb-1">Buscar por nombre</label>
                    <div className="input-group input-group-sm">
                        <span className="input-group-text bg-white">
                            <i className="bi bi-search text-primary"></i>
                        </span>
                        <input
                            type="text"
                            className="form-control"
                            placeholder="Nombre de la tienda..."
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                    </div>
                </div>

                <div className="col-12 col-md-6">
                    <label className="form-label small fw-semibold text-secondary mb-1">Filtrar por etiqueta</label>
                    <select
                        className="form-select form-select-sm"
                        value={etiquetaFilter}
                        onChange={(e) => setEtiquetaFilter(e.target.value)}
                    >
                        <option value="">Todas las etiquetas</option>
                        {etiquetasDisponibles.map((etiqueta) => (
                            <option key={etiqueta.idEtiqueta} value={etiqueta.nombre}>
                                {etiqueta.nombre}
                            </option>
                        ))}
                    </select>
                </div>
            </div>
        </div>
    );
}
