import { Card, Form, Button, Alert, Spinner } from "react-bootstrap";

const TiendaFormulario = ({
    formDatos,
    handleInputChange,
    handleSubmit,
    loading,
    updateStatus,
}) => {
    return (
        <Card className="shadow-sm border-0">
            <Card.Body>
                <h5 className="fw-semibold text-secondary mb-3">
                    Editar Información de la Tienda
                </h5>

                {updateStatus && (
                    <Alert variant={updateStatus.type === "success" ? "success" : "danger"}>
                        {updateStatus.message}
                    </Alert>
                )}

                <Form onSubmit={handleSubmit}>
                    <Form.Group className="mb-3">
                        <Form.Label className="fw-medium">Nombre</Form.Label>
                        <Form.Control
                            type="text"
                            name="nombre"
                            value={formDatos.nombre}
                            onChange={handleInputChange}
                            disabled={loading}
                            placeholder="Ingrese el nombre de la tienda"
                            required
                        />
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label className="fw-medium">Descripción</Form.Label>
                        <Form.Control
                            as="textarea"
                            rows={3}
                            name="descripcion"
                            value={formDatos.descripcion}
                            onChange={handleInputChange}
                            disabled={loading}
                            placeholder="Breve descripción de la tienda"
                        />
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label className="fw-medium">Dirección</Form.Label>
                        <Form.Control
                            type="text"
                            name="direccion"
                            value={formDatos.direccion}
                            onChange={handleInputChange}
                            disabled={loading}
                            placeholder="Calle, número, ciudad..."
                        />
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label className="fw-medium">Teléfono</Form.Label>
                        <Form.Control
                            type="tel"
                            name="telefono"
                            value={formDatos.telefono}
                            onChange={handleInputChange}
                            disabled={loading}
                            placeholder="999 999 999"
                        />
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label className="fw-medium">Página Web</Form.Label>
                        <Form.Control
                            type="url"
                            name="urlPagina"
                            value={formDatos.urlPagina}
                            onChange={handleInputChange}
                            disabled={loading}
                            placeholder="https://www.tienda.com"
                        />
                    </Form.Group>

                    <Button type="submit" variant="primary" className="w-100" disabled={loading}>
                        {loading ? (
                            <>
                                <Spinner animation="border" size="sm" className="me-2" />
                                Guardando...
                            </>
                        ) : (
                            "Guardar Cambios"
                        )}
                    </Button>
                </Form>
            </Card.Body>
        </Card>
    );
};

export default TiendaFormulario;
