import React, { useState, useEffect } from "react";
import { Form, Button, Row, Col, Spinner, Alert } from "react-bootstrap";
import { useUsuario } from "../../../hooks/useUsuario";
import { useAuthStatus } from "../../../../../hooks/useAuthStatus";

const InformacionPersonal = ({ username: initialUsername, email: initialEmail, fechaRegistro }) => {
    const { actualizarInfo, loading } = useUsuario();
    const { idUsuario } = useAuthStatus();
    
    const [username, setUsername] = useState(initialUsername || '');
    const [email, setEmail] = useState(initialEmail || '');
    const [saveStatus, setSaveStatus] = useState({ show: false, message: '', variant: 'success' });

    useEffect(() => {
        setUsername(initialUsername || '');
        setEmail(initialEmail || '');
    }, [initialUsername, initialEmail]);

    const validateEmail = (email) => {
        if (!email || email.trim() === "") {
            return "El correo electrónico es obligatorio";
        }
        if (!/\S+@\S+\.\S+/.test(email)) {
            return "El correo electrónico no es válido";
        }
        if (/\s/.test(email)) {
            return "El email no debe contener espacios";
        }
        return null;
    };

    const handleSaveChanges = async (e) => {
        e.preventDefault();
        setSaveStatus({ show: false, message: '', variant: 'success' });

        const validationError = validateEmail(email);
        if (validationError) {
            setSaveStatus({ show: true, message: validationError, variant: 'danger' });
            return;
        }

        const userData = { email };

        try {
            const res = await actualizarInfo(idUsuario, userData);

            if (res.success) {
                setSaveStatus({ show: true, message: '¡Cambios guardados con éxito!', variant: 'success' });
            } else {
                setSaveStatus({ show: true, message: res.error || 'Error al actualizar información', variant: 'danger' });
            }
        // eslint-disable-next-line no-unused-vars
        } catch (err) {
            setSaveStatus({ show: true, message: 'Error inesperado', variant: 'danger' });
        }

        setTimeout(() => setSaveStatus({ show: false, message: '', variant: 'success' }), 5000);
    };

    return (
        <>
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h3 className="mb-0">Información personal</h3>
                <Button variant="primary" onClick={handleSaveChanges} disabled={loading || initialEmail === email}>
                    {loading ? (
                        <>
                            <Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" />
                            <span className="ms-2">Guardando...</span>
                        </>
                    ) : (
                        "Guardar cambios"
                    )}
                </Button>
            </div>

            {saveStatus.show && (
                <Alert variant={saveStatus.variant} onClose={() => setSaveStatus({ show: false, message: '', variant: 'success' })} dismissible>
                    {saveStatus.message}
                </Alert>
            )}

            <Form onSubmit={handleSaveChanges}>
                <Row className="mb-3">
                    <Form.Group as={Col} md="6">
                        <Form.Label>Nombre de usuario</Form.Label>
                        <Form.Control
                            type="text"
                            value={username}
                            readOnly
                            disabled
                        />
                    </Form.Group>
                    <Form.Group as={Col} md="6">
                        <Form.Label>Correo electrónico</Form.Label>
                        <Form.Control
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            disabled={loading}
                        />
                    </Form.Group>
                </Row>

                <Form.Group className="mb-3">
                    <Form.Label>Fecha de registro</Form.Label>
                    <Form.Control
                        type="text"
                        value={fechaRegistro ? new Date(fechaRegistro).toLocaleDateString() : ''}
                        readOnly
                        disabled
                    />
                </Form.Group>
            </Form>
        </>
    );
};

export default InformacionPersonal;
