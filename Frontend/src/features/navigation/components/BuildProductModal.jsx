import React from "react";
import { Modal, Button } from "react-bootstrap";

const ProductoModal = ({
    show,
    onClose,
    categoria,
    productos,
    totalElements,
    loading,
    error,
    onSelect,
    page,
    setPage
}) => {
    return (
        <Modal show={show} onHide={onClose} centered size="lg">
            <Modal.Header closeButton>
                <Modal.Title>Seleccionar {categoria}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p>
                    Mostrando productos para: <strong>{categoria}</strong>
                    <br />
                    <small className="text-muted">Total encontrados: {totalElements}</small>
                </p>

                {loading && <p className="text-muted">Cargando productos...</p>}
                {error && <p className="text-danger">Error al cargar productos.</p>}
                {!loading && productos.length === 0 && (
                    <p className="text-muted">No hay productos disponibles para esta categoría.</p>
                )}

                {!loading && productos.length > 0 && (
                    <ul className="list-group">
                        {productos.map((p) => (
                            <li
                                key={p.idProductoTienda}
                                className="list-group-item d-flex justify-content-between align-items-center"
                            >
                                <div>
                                    <strong>{p.nombreProducto}</strong>
                                    <br />
                                    <small className="text-muted">
                                        S/. {p.precio} — {p.nombreTienda} — Stock: {p.stock}
                                    </small>
                                </div>
                                <Button variant="outline-success" size="sm" onClick={() => onSelect(p)}>
                                    Seleccionar
                                </Button>
                            </li>
                        ))}
                    </ul>
                )}
            </Modal.Body>
            <Modal.Footer>
                <Button variant="outline-secondary" disabled={page === 0} onClick={() => setPage(page - 1)}>
                    ← Anterior
                </Button>
                <Button
                    variant="outline-secondary"
                    disabled={productos.length < 8}
                    onClick={() => setPage(page + 1)}
                >
                    Siguiente →
                </Button>
                <Button variant="secondary" onClick={onClose}>
                    Cerrar
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ProductoModal;
