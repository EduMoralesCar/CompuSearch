import React from "react";
import { Form, Button, Alert, Spinner } from "react-bootstrap";

const ResetPasswordForm = ({
    password,
    setPassword,
    confirmPassword,
    setConfirmPassword,
    showPassword,
    setShowPassword,
    showConfirmPassword,
    setShowConfirmPassword,
    errors,
    feedbackMessage,
    feedbackType,
    loading,
    handleSubmit
}) => (
    <Form onSubmit={handleSubmit}>
        {/* Nueva contraseña */}
        <Form.Group className="mb-3">
            <Form.Label>Nueva contraseña</Form.Label>
            <div className="input-group">
                <Form.Control
                    type={showPassword ? "text" : "password"}
                    placeholder="••••••••"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    isInvalid={!!errors.password}
                    disabled={loading}
                />
                <Button
                    variant="outline-secondary"
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    disabled={loading}
                >
                    <i className={`bi ${showPassword ? "bi-eye-slash" : "bi-eye"}`} />
                </Button>
            </div>
            <Form.Control.Feedback type="invalid">
                {errors.password}
            </Form.Control.Feedback>
        </Form.Group>

        {/* Confirmar contraseña */}
        <Form.Group className="mb-3">
            <Form.Label>Confirmar contraseña</Form.Label>
            <div className="input-group">
                <Form.Control
                    type={showConfirmPassword ? "text" : "password"}
                    placeholder="Repite la nueva contraseña"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    isInvalid={!!errors.confirmPassword}
                    disabled={loading}
                />
                <Button
                    variant="outline-secondary"
                    type="button"
                    onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                    disabled={loading}
                >
                    <i className={`bi ${showConfirmPassword ? "bi-eye-slash" : "bi-eye"}`} />
                </Button>
            </div>
            <Form.Control.Feedback type="invalid">
                {errors.confirmPassword}
            </Form.Control.Feedback>
        </Form.Group>

        {/* Mensaje de feedback */}
        {feedbackMessage && (
            <Alert variant={feedbackType} className="py-2">
                {feedbackMessage}
            </Alert>
        )}

        <Button variant="primary" type="submit" className="w-100" disabled={loading}>
            {loading ? (
                <>
                    <Spinner animation="border" size="sm" className="me-2" />
                    Enviando...
                </>
            ) : (
                "Cambiar contraseña"
            )}
        </Button>
    </Form>
);

export default ResetPasswordForm;
