import { Modal, Row, Col, Button } from "react-bootstrap";

const ProductoModal = ({ producto, onClose }) => {
    if (!producto) return null;

    return (
        <Modal show={true} onHide={onClose} size="lg" centered>
            <Modal.Header closeButton>
                <Modal.Title>{producto.nombre}</Modal.Title>
            </Modal.Header>

            <Modal.Body>
                <Row>
                    <Col md={5}>
                        <img
                            src={producto.urlImagen}
                            alt="producto"
                            className="img-fluid rounded shadow-sm border border-secondary p-1"
                        />
                    </Col>

                    <Col md={7}>
                        <h5>Información</h5>
                        <p><strong>Marca:</strong> {producto.marca}</p>
                        <p><strong>Modelo:</strong> {producto.modelo}</p>
                        <p><strong>Categoría:</strong> {producto.categoria}</p>
                        <p><strong>Descripción:</strong> {producto.descripcion}</p>

                        <hr />

                        <p><strong>Precio:</strong> S/ {producto.precio}</p>
                        <p><strong>Stock:</strong> {producto.stock}</p>
                        <p><strong>URL Producto:</strong>
                            <a href={producto.urlProducto} target="_blank" rel="noreferrer"> Ver en tienda</a>
                        </p>

                        <hr />

                        <h5>Métricas</h5>
                        <p><strong>Visitas:</strong> {producto.visitas}</p>
                        <p><strong>Clicks:</strong> {producto.clicks}</p>
                        <p><strong>Builds:</strong> {producto.builds}</p>
                    </Col>
                </Row>
            </Modal.Body>

            <Modal.Footer>
                <Button variant="secondary" onClick={onClose}>
                    Cerrar
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ProductoModal;
