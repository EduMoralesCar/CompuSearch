import React from 'react';
import { Form, Card, Row, Col } from 'react-bootstrap';

const NotificationSetting = ({ title, description, id, defaultChecked = true }) => (
    <Row className="py-3 border-bottom mx-0">
        <Col xs={8} md={9}>
            <Form.Label htmlFor={id} className="mb-0 fw-bold" style={{ cursor: 'pointer' }}>
                {title}
            </Form.Label>
            <p className="text-muted small mb-0">{description}</p>
        </Col>
        <Col xs={4} md={3} className="d-flex justify-content-end align-items-center">
            <Form.Check
                type="switch"
                id={id}
                defaultChecked={defaultChecked}
            />
        </Col>
    </Row>
);

const AlertasDePrecio = () => {
    return (
        <>
            <h3 className="mb-4">Preferencias de notificación</h3>

            <Card>
                <Card.Body className="p-0">
                    <NotificationSetting
                        id="alertas-precio"
                        title="Alertas de precio"
                        description="Recibir notificaciones cuando los productos en tu lista de deseos bajen de precio."
                    />
                    <NotificationSetting
                        id="notificaciones-correo"
                        title="Notificaciones por correo"
                        description="Recibir notificaciones por correo electrónico."
                    />
                    <NotificationSetting
                        id="novedades-ofertas"
                        title="Novedades y ofertas"
                        description="Recibir información sobre nuevos productos y ofertas especiales."
                    />
                </Card.Body>
            </Card>
        </>
    );
};

export default AlertasDePrecio;