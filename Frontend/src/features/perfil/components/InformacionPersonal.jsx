import React, { useState, useEffect } from "react";
import { Form, Button, Row, Col, Spinner, Alert } from "react-bootstrap";
import { useAuth } from "../../../context/useAuth";

const InformacionPersonal = () => {
    const { usuario } = useAuth();

    const [nombre, setNombre] = useState('');
    const [apellido, setApellido] = useState('');
    const [telefono, setTelefono] = useState('');
    const [ubicacion, setUbicacion] = useState('');
    const [biografia, setBiografia] = useState('');

    const [isSaving, setIsSaving] = useState(false);
    const [saveStatus, setSaveStatus] = useState({ show: false, message: '', variant: 'success' });

    useEffect(() => {
        if (usuario) {
            setNombre(usuario.identificador || '');
        }
    }, [usuario]);

    const handleSaveChanges = (e) => {
        e.preventDefault();
        setIsSaving(true);
        setSaveStatus({ show: false, message: '', variant: 'success' });

        const userData = {
            nombre,
            apellido,
            telefono,
            ubicacion,
            biografia
        };

        console.log("Datos a guardar:", userData);

        //Simulacion de 3 segundos - Espera en base al backend
        setTimeout(() => {
            setIsSaving(false);
            setSaveStatus({ show: true, message: '¡Cambios guardados con éxito!', variant: 'success' });

            setTimeout(() => setSaveStatus({ show: false, message: '', variant: 'success' }), 3000);
        }, 1500);
    };

    return (
        <>
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h3 className="mb-0">Información personal</h3>
                <Button variant="primary" onClick={handleSaveChanges} disabled={isSaving}>
                    {isSaving ? (
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
                <Alert variant={saveStatus.variant} onClose={() => setSaveStatus(false)} dismissible>
                    {saveStatus.message}
                </Alert>
            )}

            <Form onSubmit={handleSaveChanges}>
                <Row className="mb-3">
                    <Form.Group as={Col} md="6">
                        <Form.Label>Nombre</Form.Label>
                        <Form.Control
                            type="text"
                            value={nombre}
                            onChange={(e) => setNombre(e.target.value)}
                            disabled={isSaving}
                        />
                    </Form.Group>
                    <Form.Group as={Col} md="6">
                        <Form.Label>Apellido</Form.Label>
                        <Form.Control
                            type="text"
                            value={apellido}
                            onChange={(e) => setApellido(e.target.value)}
                            placeholder="(Opcional)"
                            disabled={isSaving}
                        />
                    </Form.Group>
                </Row>

                <Form.Group className="mb-3">
                    <Form.Label>Correo electrónico</Form.Label>
                    <Form.Control type="email" value={usuario?.email || ''} readOnly disabled />
                </Form.Group>

                <Row className="mb-3">
                    <Form.Group as={Col} md="6">
                        <Form.Label>Teléfono</Form.Label>
                        <Form.Control 
                            type="tel" 
                            placeholder="Opcional" 
                            value={telefono}
                            onChange={(e) => setTelefono(e.target.value)}
                            disabled={isSaving}
                        />
                    </Form.Group>
                    <Form.Group as={Col} md="6">
                        <Form.Label>Ubicación</Form.Label>
                        <Form.Control 
                            type="text" 
                            placeholder="Ej: Lima, Perú" 
                            value={ubicacion}
                            onChange={(e) => setUbicacion(e.target.value)}
                            disabled={isSaving}
                        />
                    </Form.Group>
                </Row>

                <Form.Group className="mb-3">
                    <Form.Label>Biografía</Form.Label>
                    <Form.Control 
                        as="textarea" 
                        rows={4} 
                        placeholder="Cuéntanos algo sobre ti..." 
                        value={biografia}
                        onChange={(e) => setBiografia(e.target.value)}
                        disabled={isSaving}
                    />
                </Form.Group>
            </Form>
        </>
    );
};

export default InformacionPersonal;