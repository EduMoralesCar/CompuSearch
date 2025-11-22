import { Modal, Button, Badge } from "react-bootstrap"

const ModalProductos = ({ show, onClose, productos }) => (
    <Modal show={show} onHide={onClose} size="lg" centered>
        <Modal.Header closeButton>
            <Modal.Title>
                <i className="fas fa-boxes me-2"></i>
                Productos de la Tienda
            </Modal.Title>
        </Modal.Header>

        <Modal.Body>
            {productos?.length > 0 ? (
                <ul className="list-group">
                    {productos.map((item, i) => (
                        <li key={i} className="list-group-item d-flex align-items-start">

                            <img
                                src={item.urlImagen}
                                alt={item.nombre}
                                className="me-3 rounded"
                                style={{ width: "70px", height: "70px", objectFit: "cover" }}
                            />

                            <div>
                                <strong className="d-block">{item.nombre}</strong>

                                <span className="text-muted d-block small">
                                    Categoría: {item.categoria}
                                </span>

                                <span className="text-primary fw-semibold d-block mt-1">
                                    Precio: S/. {item.precio} — Stock: {item.stock}
                                </span>

                                <span className="small d-block mt-1">
                                    {item.habilitado ? (
                                        <Badge bg="success">Habilitado</Badge>
                                    ) : (
                                        <Badge bg="danger">Deshabilitado</Badge>
                                    )}
                                </span>
                            </div>
                        </li>
                    ))}
                </ul>
            ) : (
                <p className="text-muted">No hay productos.</p>
            )}
        </Modal.Body>

        <Modal.Footer>
            <Button variant="secondary" onClick={onClose}>
                Cerrar
            </Button>
        </Modal.Footer>
    </Modal>
);

export default ModalProductos;