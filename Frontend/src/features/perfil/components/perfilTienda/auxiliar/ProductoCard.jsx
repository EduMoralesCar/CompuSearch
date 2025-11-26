import { Card, Button, Badge } from "react-bootstrap";

const ProductoCard = ({ prod, onSelect, onToggle }) => {
    return (
        <Card className="h-100 shadow-sm d-flex flex-column">
            
            <Card.Img
                variant="top"
                src={prod.urlImagen}
                style={{ height: "200px", objectFit: "cover" }}
            />

            <Card.Body className="d-flex flex-column" style={{ flex: 1 }}>
                
                <Card.Title
                    className="d-flex justify-content-between align-items-start mb-2"
                    style={{
                        whiteSpace: "nowrap",
                        overflow: "hidden",
                        textOverflow: "ellipsis",
                    }}
                    title={prod.nombre}
                >
                    {prod.nombre}
                    {prod.habilitado
                        ? <Badge bg="success">Habilitado</Badge>
                        : <Badge bg="secondary">Deshabilitado</Badge>
                    }
                </Card.Title>

                <Card.Text className="mb-3" style={{ flex: 1 }}>
                    <strong>Marca:</strong> {prod.marca} <br />
                    <strong>Precio:</strong> S/ {prod.precio} <br />
                    <strong>Stock:</strong> {prod.stock}
                </Card.Text>

                <div className="mt-auto d-flex gap-2">
                    <Button variant="primary" onClick={() => onSelect(prod)} className="w-100">
                        Ver detalles
                    </Button>

                    <Button
                        variant={prod.habilitado ? "danger" : "success"}
                        onClick={() => onToggle(prod)}
                        className="w-100"
                    >
                        {prod.habilitado ? "Deshabilitar" : "Habilitar"}
                    </Button>
                </div>
            </Card.Body>

        </Card>
    );
};

export default ProductoCard;
