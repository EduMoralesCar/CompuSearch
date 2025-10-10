import React from "react";
import { Form, Button, Spinner } from "react-bootstrap";

const RegistroForm = ({
    username,
    setUsername,
    email,
    setEmail,
    password,
    setPassword,
    showPassword,
    togglePassword,
    errors,
    feedbackMessage,
    feedbackType = "danger",
    handleSubmit,
    loading
}) => {
    return (
        <Form onSubmit={handleSubmit}>
            {/* Username */}
            <Form.Group className="mb-3">
                <Form.Label>Nombre de usuario</Form.Label>
                <Form.Control
                    type="text"
                    value={username}
                    placeholder="Ej: Pepe123"
                    isInvalid={!!errors.username}
                    onChange={(e) => setUsername(e.target.value)}
                    disabled={loading}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.username}
                </Form.Control.Feedback>
            </Form.Group>

            {/* Email */}
            <Form.Group className="mb-3">
                <Form.Label>Correo electrónico</Form.Label>
                <Form.Control
                    type="text"
                    value={email}
                    placeholder="ejemplo@correo.com"
                    isInvalid={!!errors.email}
                    onChange={(e) => setEmail(e.target.value)}
                    disabled={loading}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.email}
                </Form.Control.Feedback>
            </Form.Group>

            {/* Password */}
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
                    <Button
                        variant="outline-secondary"
                        onClick={togglePassword}
                        disabled={loading}
                    >
                        <i className={`bi ${showPassword ? "bi-eye-slash" : "bi-eye"}`} />
                    </Button>
                </div>
                {errors.password && (
                    <div className="invalid-feedback d-block">{errors.password}</div>
                )}
            </Form.Group>

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
                        Creando cuenta...
                    </>
                ) : (
                    "Crear cuenta"
                )}
            </Button>
        </Form>
    );
};

export default RegistroForm;
