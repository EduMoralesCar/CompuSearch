const TablaTiendas = ({ tiendas }) => {
    return (
        <div className="row mt-5" id="tiendas">
            <div className="col-12">
                <h3 className="mb-4">
                    <span className="fw-bold">Encuéntralo también en estas tiendas</span>
                </h3>

                {tiendas && tiendas.length > 0 ? (
                    <div className="table-responsive">
                        <table className="table align-middle table-hover">
                            <thead className="table-light">
                                <tr>
                                    <th scope="col">Tienda</th>
                                    <th scope="col">Precio</th>
                                    <th scope="col">Stock</th>
                                    <th scope="col">Producto</th>
                                </tr>
                            </thead>
                            <tbody>
                                {tiendas.map((tienda, idx) => (
                                    <tr key={idx}>
                                        <td>
                                            <strong>{tienda.nombreTienda}</strong>
                                        </td>
                                        <td>
                                            <span className="text-dark fw-semibold">
                                                S/ {tienda.precio.toFixed(2)}
                                            </span>
                                        </td>
                                        <td>
                                            {tienda.stock > 0 ? (
                                                <span className="text-success">
                                                    En Stock ({tienda.stock})
                                                </span>
                                            ) : (
                                                <span className="text-danger">
                                                    Agotado
                                                </span>
                                            )}
                                        </td>
                                        <td>
                                            <a
                                                href={tienda.urlProducto}
                                                target="_blank"
                                                rel="noopener noreferrer"
                                                className="btn btn-outline-primary btn-sm"
                                                title={`Ver producto en ${tienda.nombreTienda}`}
                                                aria-label={`Ver producto en ${tienda.nombreTienda}`}
                                            >
                                                Ir al producto
                                            </a>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                ) : (
                    <div className="alert alert-warning text-center mt-4" role="alert">
                        No se encontraron otras tiendas que vendan este producto.
                    </div>
                )}
            </div>
        </div>
    );
};

export default TablaTiendas;
