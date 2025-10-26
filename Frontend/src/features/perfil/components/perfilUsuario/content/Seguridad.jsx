import React, { useState } from "react";
import { Form, Button, Card, Spinner, Alert, InputGroup } from "react-bootstrap";
import { useUsuario } from "../../../hooks/useUsuario";
import { useAuthStatus } from "../../../../../hooks/useAuthStatus";
import { Eye, EyeSlash } from "react-bootstrap-icons"; // iconos para el ojito

const Seguridad = () => {
    const { idUsuario } = useAuthStatus();
    const { actualizarPassword, loading, error } = useUsuario();

    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [showCurrent, setShowCurrent] = useState(false);
    const [showNew, setShowNew] = useState(false);
    const [showConfirm, setShowConfirm] = useState(false);
    const [successMessage, setSuccessMessage] = useState("");
    const [validationError, setValidationError] = useState("");

    const validatePassword = (password) => {
        if (!password || password.trim() === "") {
            return "La contraseña es obligatoria";
        }
        if (/\s/.test(password)) {
            return "La contraseña no debe contener espacios";
        }
        const regex = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$/;
        if (!regex.test(password)) {
            return "Debe tener al menos 8 caracteres, incluyendo mayúscula, minúscula, número y carácter especial";
        }
        return null;
    };

    const handleActualizarPassword = async (e) => {
        e.preventDefault();
        setSuccessMessage("");
        setValidationError("");

        const errorCurrent = validatePassword(currentPassword);
        const errorNew = validatePassword(newPassword);

        if (errorCurrent) return setValidationError(`Contraseña actual: ${errorCurrent}`);
        if (errorNew) return setValidationError(`Nueva contraseña: ${errorNew}`);
        if (newPassword !== confirmPassword) return setValidationError("La nueva contraseña y su confirmación no coinciden");

        const result = await actualizarPassword(idUsuario, { 
            currentPassword: currentPassword, 
            newPassword: newPassword, 
            confirmPassword: confirmPassword });

        if (result.success) {
            setSuccessMessage("Contraseña actualizada con éxito");
            setCurrentPassword("");
            setNewPassword("");
            setConfirmPassword("");
        }
    };

    return (
        <>
            <h3 className="mb-4">Seguridad de la Cuenta</h3>
            <Card className="mb-4">
                <Card.Body>
                    <Card.Title as="h5">Cambiar contraseña</Card.Title>
                    {validationError && <Alert variant="danger">{validationError}</Alert>}
                    {error && <Alert variant="danger">{error}</Alert>}
                    {successMessage && <Alert variant="success">{successMessage}</Alert>}

                    <Form onSubmit={handleActualizarPassword}>
                        {/* Contraseña actual */}
                        <Form.Group className="mb-3">
                            <Form.Label>Contraseña actual</Form.Label>
                            <InputGroup>
                                <Form.Control
                                    type={showCurrent ? "text" : "password"}
                                    value={currentPassword}
                                    onChange={(e) => setCurrentPassword(e.target.value)}
                                    disabled={loading}
                                />
                                <Button
                                    variant="outline-secondary"
                                    onClick={() => setShowCurrent(!showCurrent)}
                                    type="button"
                                >
                                    {showCurrent ? <EyeSlash /> : <Eye />}
                                </Button>
                            </InputGroup>
                        </Form.Group>

                        {/* Nueva contraseña */}
                        <Form.Group className="mb-3">
                            <Form.Label>Nueva contraseña</Form.Label>
                            <InputGroup>
                                <Form.Control
                                    type={showNew ? "text" : "password"}
                                    value={newPassword}
                                    onChange={(e) => setNewPassword(e.target.value)}
                                    disabled={loading}
                                />
                                <Button
                                    variant="outline-secondary"
                                    onClick={() => setShowNew(!showNew)}
                                    type="button"
                                >
                                    {showNew ? <EyeSlash /> : <Eye />}
                                </Button>
                            </InputGroup>
                        </Form.Group>

                        {/* Confirmar nueva contraseña */}
                        <Form.Group className="mb-3">
                            <Form.Label>Confirmar nueva contraseña</Form.Label>
                            <InputGroup>
                                <Form.Control
                                    type={showConfirm ? "text" : "password"}
                                    value={confirmPassword}
                                    onChange={(e) => setConfirmPassword(e.target.value)}
                                    disabled={loading}
                                />
                                <Button
                                    variant="outline-secondary"
                                    onClick={() => setShowConfirm(!showConfirm)}
                                    type="button"
                                >
                                    {showConfirm ? <EyeSlash /> : <Eye />}
                                </Button>
                            </InputGroup>
                        </Form.Group>

                        <Button variant="primary" type="submit" disabled={loading}>
                            {loading ? (
                                <>
                                    <Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" />
                                    <span className="ms-2">Actualizando...</span>
                                </>
                            ) : (
                                "Actualizar contraseña"
                            )}
                        </Button>
                    </Form>
                </Card.Body>
            </Card>
        </>
    );
};

export default Seguridad;
