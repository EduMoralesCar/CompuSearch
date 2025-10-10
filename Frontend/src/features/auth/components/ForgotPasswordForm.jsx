import React from "react";
import { Form, Button, Alert, Spinner } from "react-bootstrap";

const ForgotPasswordForm = ({
    email,
    setEmail,
    errors,
    feedbackMessage,
    feedbackType,
    loading,
    handleSubmit
}) => {
    return (
        <Form onSubmit={handleSubmit}>
            {/* Campo de correo */}
            <Form.Group className="mb-3">
                <Form.Label>Correo electrónico</Form.Label>
                <Form.Control
                    type="text"
                    placeholder="ejemplo@correo.com"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    isInvalid={!!errors.email}
                    disabled={loading}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.email}
                </Form.Control.Feedback>
            </Form.Group>

            {/* Mensaje de éxito */}
            {feedbackMessage && (
                <Alert variant={feedbackType} className="py-2">
                    {feedbackMessage}
                </Alert>
            )}


            {/* Botón de envío */}
            <Button variant="primary" type="submit" className="w-100" disabled={loading}>
                {loading ? (
                    <>
                        <Spinner animation="border" size="sm" className="me-2" />
                        Enviando...
                    </>
                ) : (
                    "Enviar enlace"
                )}
            </Button>
        </Form>
    );
};

export default ForgotPasswordForm;
