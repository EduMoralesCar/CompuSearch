import React from "react";
import { Form, Button, Card } from "react-bootstrap";

const Seguridad = () => {
    return (
        <>
            <h3 className="mb-4">Seguridad de la Cuenta</h3>
            
            <Card className="mb-4">
                <Card.Body>
                    <Card.Title as="h5">Cambiar contraseña</Card.Title>
                    <Form>
                        <Form.Group className="mb-3" controlId="currentPassword">
                            <Form.Label>Contraseña actual</Form.Label>
                            <Form.Control type="password" placeholder="" />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="newPassword">
                            <Form.Label>Nueva contraseña</Form.Label>
                            <Form.Control type="password" placeholder="" />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="confirmPassword">
                            <Form.Label>Confirmar nueva contraseña</Form.Label>
                            <Form.Control type="password" placeholder="" />
                        </Form.Group>
                        <Button variant="primary" type="submit">
                            Actualizar contraseña
                        </Button>
                    </Form>
                </Card.Body>
            </Card>

            <Card className="mb-4">
                <Card.Body>
                    <div className="d-flex justify-content-between align-items-center">
                        <div>
                            <Card.Title as="h5">Autenticación de Dos Factores (2FA)</Card.Title>
                            <Card.Subtitle className="text-muted">Añade una capa extra de seguridad a tu cuenta.</Card.Subtitle>
                        </div>
                        <Form.Check
                            type="switch"
                            id="tfa-switch"
                            label="Desactivado"
                        />
                    </div>
                </Card.Body>
            </Card>

            <Card border="danger">
                <Card.Body>
                    <Card.Title as="h5" className="text-danger">Zona de Peligro</Card.Title>
                    <div className="d-flex justify-content-between align-items-center">
                        <div>
                            <strong>Eliminar esta cuenta</strong>
                            <p className="text-muted mb-0 small">
                                Una vez que elimines tu cuenta, no hay vuelta atrás. Por favor, asegúrate.
                            </p>
                        </div>
                        <Button variant="outline-danger">Eliminar cuenta</Button>
                    </div>
                </Card.Body>
            </Card>
        </>
    );
};

export default Seguridad;