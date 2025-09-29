// src/pages/ForgotPassword.jsx
import React, { useState, useEffect } from "react";
import { Form, Button, Card, Alert } from "react-bootstrap";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const ForgotPassword = () => {
    const [email, setEmail] = useState("");
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
            navigate("/");
        }
    }, [navigate]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage("");
        setError("");

        try {
            const res = await axios.post("http://localhost:8080/auth/password/forgot", { email });
            // ✅ Solo mostramos el mensaje, no el token
            setMessage(res.data.message);
        } catch (err) {
            if (err.response && err.response.data.error) {
                setError(err.response.data.error);
            } else {
                setError("Error de conexión con el servidor");
            }
        }
    };

    return (
        <div className="container d-flex justify-content-center align-items-center flex-grow-1">
            <div className="row w-100 my-4">
                <div className="col-md-6 offset-md-3">
                    <Card className="shadow-sm p-4">
                        <h3 className="fw-bold text-primary mb-4">Recuperar Contraseña</h3>
                        <p className="text-muted">
                            Ingresa tu correo electrónico y te enviaremos un enlace para restablecer tu contraseña.
                        </p>
                        <Form onSubmit={handleSubmit}>
                            <Form.Group className="mb-3">
                                <Form.Label>Correo electrónico</Form.Label>
                                <Form.Control
                                    type="email"
                                    placeholder="ejemplo@correo.com"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    required
                                />
                            </Form.Group>

                            {message && <Alert variant="success">{message}</Alert>}
                            {error && <Alert variant="danger">{error}</Alert>}

                            <Button variant="primary" type="submit" className="w-100">
                                Enviar enlace
                            </Button>
                        </Form>
                    </Card>
                </div>
            </div>
        </div>
    );
};

export default ForgotPassword;