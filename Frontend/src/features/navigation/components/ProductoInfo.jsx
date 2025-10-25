const ProductoInfo = ({ productoInfo }) => {
    if (!productoInfo) return null;
    
    const {
        nombreProducto,
        marca,
        modelo,
        descripcion,
        urlProducto,
        nombreTienda,
        atributos,
        precio
    } = productoInfo;

    return (
        <div>
            <h1 className="h3">{nombreProducto}</h1>

            <h2 className="text-primary my-3 fw-semibold">
                S/ {precio?.toFixed(2)}
            </h2>

            {marca && <p className="mb-1"><strong>Marca:</strong> {marca}</p>}
            {modelo && <p className="mb-1"><strong>Modelo:</strong> {modelo}</p>}
            {nombreTienda && <p className="mb-1"><strong>Tienda:</strong> {nombreTienda}</p>}

            {descripcion && (
                <p className="mt-3 text-muted" style={{ whiteSpace: "pre-line" }}>
                    {descripcion}
                </p>
            )}

            {atributos?.length > 0 && (
                <div className="mt-4">
                    <h5 className="mb-3">Características:</h5>
                    <ul className="list-group">
                        {atributos.map((attr, idx) => (
                            <li key={idx} className="list-group-item d-flex justify-content-between">
                                <span>{attr.nombreAtributo}</span>
                                <span className="text-muted">{attr.valor}</span>
                            </li>
                        ))}
                    </ul>
                </div>
            )}

            <div className="mt-4 d-flex flex-wrap gap-3">
                <a href="#tiendas" className="btn btn-dark btn-lg px-4 py-2">
                    Consultar Disponibilidad
                </a>
                {urlProducto && (
                    <a
                        href={urlProducto}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="btn btn-outline-primary btn-lg px-4 py-2"
                    >
                        Ver en tienda
                    </a>
                )}
            </div>
        </div>
    );
};

export default ProductoInfo;
