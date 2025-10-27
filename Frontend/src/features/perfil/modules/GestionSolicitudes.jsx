import { Card, Button, Stack, Row, Col } from "react-bootstrap";

// Datos de Ejemplo (Jesus borra esta webada) 
const mockSolicitudes = [
    { 
        id: 401, 
        tienda: "PC Componentes Express", 
        contacto: "contacto@pcexpress.com",
        fecha: "2025-10-24",
        descripcion: "Solicitud para registrar nuestra tienda en la plataforma. Adjuntamos documentos de registro fiscal." 
    },
    { 
        id: 402, 
        tienda: "CyberTech Perú", 
        contacto: "ventas@cybertech.pe",
        fecha: "2025-10-23",
        descripcion: "Solicitamos la aprobación para listar nuestros nuevos productos de la marca 'OmegaTech'." 
    },
];

const GestionSolicitudes = () => {

    // Funciones de acción (solo visuales - Jesus Chambea)
    const handleAceptar = (id) => {
        alert(`(Visual) Solicitud ID ${id} ACEPTADA`);
    };

    const handleRechazar = (id) => {
        alert(`(Visual) Solicitud ID ${id} RECHAZADA`);
    };

    return (
        <Card>
            <Card.Header as="h5">Gestión de Solicitudes</Card.Header>
            <Card.Body>
                {/* Usamos Row/Col para un layout responsive de las tarjetas */}
                <Row xs={1} md={2} className="g-4">
                    {mockSolicitudes.map((solicitud) => (
                        <Col key={solicitud.id}>
                            <Card className="h-100">
                                <Card.Header>
                                    <strong>Tienda: {solicitud.tienda}</strong>
                                </Card.Header>
                                <Card.Body className="d-flex flex-column">
                                    <Card.Text>
                                        <strong>Contacto:</strong> {solicitud.contacto} <br />
                                        <strong>Fecha:</strong> {solicitud.fecha}
                                    </Card.Text>
                                    <Card.Text className="mt-2">
                                        {solicitud.descripcion}
                                    </Card.Text>
                                    
                                    {/* Botones al final de la tarjeta */}
                                    <Stack direction="horizontal" gap={2} className="mt-auto">
                                        <Button 
                                            variant="success" 
                                            onClick={() => handleAceptar(solicitud.id)}
                                        >
                                            Aceptar
                                        </Button>
                                        <Button 
                                            variant="danger" 
                                            onClick={() => handleRechazar(solicitud.id)}
                                        >
                                            Rechazar
                                        </Button>
                                    </Stack>
                                </Card.Body>
                            </Card>
                        </Col>
                    ))}

                    {/* Mensaje si no hay solicitudes */}
                    {mockSolicitudes.length === 0 && (
                        <Col>
                            <p className="text-muted">No hay solicitudes pendientes.</p>
                        </Col>
                    )}
                </Row>
            </Card.Body>
        </Card>
    );
};

export default GestionSolicitudes;