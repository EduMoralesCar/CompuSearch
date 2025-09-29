// src/pages/ResetPassword.jsx
import React, { useState, useEffect } from "react";
import { Form, Button, Card, Alert } from "react-bootstrap";
import axios from "axios";
import { useNavigate, useLocation } from "react-router-dom";

const ResetPassword = () => {
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();
    const location = useLocation();
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    // Extraer token desde la URL
    const queryParams = new URLSearchParams(location.search);
    const token = queryParams.get("token");

    useEffect(() => {
        const authToken = localStorage.getItem("token");
        // Si el usuario está logueado, redirigir a home
        if (authToken) {
            navigate("/"); 
            return; // Detener la ejecución del useEffect
        }
        // Si no está logueado Y falta el token de restablecimiento, redirigir a forgot
        if (!token) {
            setError("Token de restablecimiento de contraseña no encontrado. Por favor, solicita uno nuevamente.");
            
            setTimeout(() => {
                navigate("/forgot-password"); 
            }, 2000); 
        }
    }, [token, navigate]);

    const validatePassword = (pass) => {
        // Debe cumplir con la validación del backend
        const regex = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$/;
        return regex.test(pass);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage("");
        setError("");

        if (!token) {
             setError("Token no válido. No se puede procesar la solicitud.");
             return;
        }

        if (!validatePassword(password)) {
            setError(
                "La contraseña debe tener al menos 8 caracteres, incluyendo mayúscula, minúscula, número y caracter especial."
            );
            return;
        }

        if (password !== confirmPassword) {
            setError("Las contraseñas no coinciden.");
            return;
        }

        try {
            const res = await axios.post("http://localhost:8080/auth/password/reset", {
                token,
                password,
            });
            setMessage(res.data.message);

            setTimeout(() => {
                navigate("/login");
            }, 1500);
        } catch (err) {
            if (err.response && err.response.data.error) {
                setError(err.response.data.error);
            } else {
                setError("Error de conexión con el servidor");
            }
        }
    };

    if (!token) {
        return (
             <div className="container d-flex justify-content-center align-items-center flex-grow-1 my-4">
                 <Alert variant="warning" className="w-50">
                    {error || "Verificando token de acceso..."}
                 </Alert>
             </div>
        );
    }

    return (
        <div className="container d-flex justify-content-center align-items-center flex-grow-1">
            <div className="row w-100 my-4">
                <div className="col-md-6 offset-md-3">
                    <Card className="shadow-sm p-4">
                        <h3 className="fw-bold text-primary mb-4">Restablecer Contraseña</h3>
                        <Form onSubmit={handleSubmit}>
                            
                            {/* Nueva contraseña */}
                            <Form.Group className="mb-3">
                                <Form.Label>Nueva contraseña</Form.Label>
                                <div className="input-group">
                                    <Form.Control
                                        type={showPassword ? "text" : "password"}
                                        placeholder="Nueva contraseña"
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                        required
                                    />
                                    <Button
                                        variant="outline-secondary"
                                        type="button"
                                        onClick={() => setShowPassword(!showPassword)}
                                    >
                                        <i className={`bi ${showPassword ? "bi-eye-slash" : "bi-eye"}`} />
                                    </Button>
                                </div>
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
                                        required
                                    />
                                    <Button
                                        variant="outline-secondary"
                                        type="button"
                                        onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                                    >
                                        <i className={`bi ${showConfirmPassword ? "bi-eye-slash" : "bi-eye"}`} />
                                    </Button>
                                </div>
                            </Form.Group>

                            {message && <Alert variant="success">{message}</Alert>}
                            {error && <Alert variant="danger">{error}</Alert>}

                            <Button variant="primary" type="submit" className="w-100">
                                Cambiar contraseña
                            </Button>
                        </Form>
                    </Card>
                </div>
            </div>
        </div>
    );
};

export default ResetPassword;
