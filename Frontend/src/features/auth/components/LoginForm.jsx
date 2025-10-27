import React from "react";
import { Form, Button, Spinner, Alert } from "react-bootstrap";
import { Link } from "react-router-dom";

const LoginForm = ({
    identifier,
    setIdentifier,
    password,
    setPassword,
    showPassword,
    togglePassword,
    rememberMe,
    setRememberMe,
    errors,
    feedbackMessage,
    feedbackType = "danger",
    handleSubmit,
    loading
}) => {
    return (
        <Form onSubmit={handleSubmit}>
            {/* Identificador */}
            <Form.Group className="mb-3">
                <Form.Label>Correo o Usuario</Form.Label>
                <Form.Control
                    type="text"
                    value={identifier}
                    placeholder="Ingresa tu correo o usuario"
                    isInvalid={!!errors.identifier}
                    onChange={(e) => setIdentifier(e.target.value)}
                    disabled={loading}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.identifier}
                </Form.Control.Feedback>
            </Form.Group>

            {/* Contraseña */}
            <Form.Group className="mb-3">
                <Form.Label>Contraseña</Form.Label>
                <div className="input-group">
                    <Form.Control
                        type={showPassword ? "text" : "password"}
                        value={password}
                        placeholder="••••••••"
                        isInvalid={!!errors.password}
                        onChange={(e) => setPassword(e.target.value)}
                        disabled={loading}
                    />
                    <Button variant="outline-secondary" onClick={togglePassword} disabled={loading}>
                        <i className={`bi ${showPassword ? "bi-eye-slash" : "bi-eye"}`} />
                    </Button>
                </div>
                {errors.password && (
                    <div className="invalid-feedback d-block">{errors.password}</div>
                )}
            </Form.Group>

            {/* Recuérdame + Olvidaste */}
            <div className="d-flex justify-content-between align-items-center mb-3">
                <div
                    className="d-flex align-items-center gap-2 cursor-pointer"
                    onClick={() => !loading && setRememberMe(!rememberMe)}
                >
                    <i
                        className={`bi ${rememberMe ? "bi-toggle-on" : "bi-toggle-off"} text-primary fs-4`}
                    />
                    <span className="text-decoration-none">Recuérdame</span>
                </div>

                <Link
                    to="/forgot-password"
                    className="small text-decoration-none justify-content-end"
                >
                    ¿Olvidaste tu contraseña?
                </Link>
            </div>

            {/* Mensaje de éxito */}
            {feedbackMessage && (
                <Alert variant={feedbackType} className="py-2">
                    {feedbackMessage}
                </Alert>
            )}

            {/* Submit */}
            <Button type="submit" variant="primary" className="w-100" disabled={loading}>
                {loading ? (
                    <>
                        <Spinner animation="border" size="sm" className="me-2" />
                        Iniciando sesión...
                    </>
                ) : (
                    "Iniciar Sesión"
                )}
            </Button>
        </Form>
    );
};

export default LoginForm;
