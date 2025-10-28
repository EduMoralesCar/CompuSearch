import React, { useState } from "react";
import { Form, Button, Alert, Spinner, Card, Row, Col } from "react-bootstrap";
import axios from "axios";

const SolicitudTiendaForm = ({ idUsuario }) => {
    const [formData, setFormData] = useState({
        nombreTienda: "",
        descripcion: "",
        telefono: "",
        direccion: "",
        sitioWeb: "",
        ruc: "",
        emailContacto: "",
        aniosExperiencia: "",
        redesSociales: "",
        fotoLocal: "",
        documentoIdentidad: "",
    });

    const [errors, setErrors] = useState({});
    const [loading, setLoading] = useState(false);
    const [mensaje, setMensaje] = useState(null);
    const [errorGeneral, setErrorGeneral] = useState(null);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
        setErrors((prev) => ({ ...prev, [name]: "" }));
    };

    const validarFormulario = () => {
        const nuevosErrores = {};
        const {
            nombreTienda,
            descripcion,
            telefono,
            direccion,
            sitioWeb,
            ruc,
            emailContacto,
            documentoIdentidad,
        } = formData;

        // Campos vacíos
        const camposObligatorios = {
            nombreTienda,
            descripcion,
            telefono,
            direccion,
            sitioWeb,
            ruc,
            emailContacto,
            documentoIdentidad,
        };

        for (const [campo, valor] of Object.entries(camposObligatorios)) {
            if (!valor || valor.trim() === "") {
                nuevosErrores[campo] = "Este campo es obligatorio.";
            }
        }

        // Validar RUC (Perú)
        const rucRegex = /^(10|15|17|20)\d{9}$/;
        if (ruc && !rucRegex.test(ruc)) {
            nuevosErrores.ruc = "El RUC no es válido. Debe tener 11 dígitos y empezar con 10, 15, 17 o 20.";
        }

        // Validar correo
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (emailContacto && !emailRegex.test(emailContacto)) {
            nuevosErrores.emailContacto = "El correo electrónico no es válido.";
        }

        // Validar teléfono
        const telefonoRegex = /^[0-9+\s-]{6,15}$/;
        if (telefono && !telefonoRegex.test(telefono)) {
            nuevosErrores.telefono = "El número de teléfono no es válido.";
        }

        // Validar sitio web
        if (sitioWeb) {
            try {
                new URL(sitioWeb);
            } catch {
                nuevosErrores.sitioWeb = "El sitio web ingresado no es una URL válida.";
            }
        }

        setErrors(nuevosErrores);
        return Object.keys(nuevosErrores).length === 0;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMensaje(null);
        setErrorGeneral(null);

        const esValido = validarFormulario();
        if (!esValido) return;

        setLoading(true);

        try {
            const datosFormulario = JSON.stringify(formData);

            await axios.post(
                `http://localhost:8080/solicitud/${idUsuario}`,
                { datosFormulario },
                { withCredentials: true }
            );

            setMensaje("Solicitud enviada correctamente. Un administrador la revisará pronto.");
            setFormData({
                nombreTienda: "",
                descripcion: "",
                telefono: "",
                direccion: "",
                sitioWeb: "",
                ruc: "",
                emailContacto: "",
                aniosExperiencia: "",
                redesSociales: "",
                fotoLocal: "",
                documentoIdentidad: "",
            });
            setErrors({});
        } catch (err) {
            setErrorGeneral(err.response?.data?.message || "Error al enviar la solicitud.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <Card className="shadow-lg border-0 rounded-4 p-4 my-5 bg-light">
            <div className="text-center mb-4">
                <h3 className="fw-bold text-primary">Solicitud para crear una Tienda</h3>
                <p className="text-muted mb-0">
                    Completa los datos a continuación para solicitar la verificación y registro de tu tienda.
                </p>
            </div>

            <Form noValidate onSubmit={handleSubmit}>
                <Row>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Nombre de la tienda *</Form.Label>
                            <Form.Control
                                type="text"
                                name="nombreTienda"
                                value={formData.nombreTienda}
                                onChange={handleChange}
                                placeholder="Ej: Computech Store"
                                isInvalid={!!errors.nombreTienda}
                            />
                            <Form.Control.Feedback type="invalid">
                                {errors.nombreTienda}
                            </Form.Control.Feedback>
                        </Form.Group>
                    </Col>

                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>RUC o número de registro *</Form.Label>
                            <Form.Control
                                type="text"
                                name="ruc"
                                value={formData.ruc}
                                onChange={handleChange}
                                placeholder="Ej: 20456789321"
                                isInvalid={!!errors.ruc}
                            />
                            <Form.Control.Feedback type="invalid">
                                {errors.ruc}
                            </Form.Control.Feedback>
                        </Form.Group>
                    </Col>
                </Row>

                <Form.Group className="mb-3">
                    <Form.Label>Descripción de la tienda *</Form.Label>
                    <Form.Control
                        as="textarea"
                        name="descripcion"
                        rows={3}
                        value={formData.descripcion}
                        onChange={handleChange}
                        placeholder="Describe brevemente qué productos o servicios ofrece tu tienda..."
                        isInvalid={!!errors.descripcion}
                    />
                    <Form.Control.Feedback type="invalid">
                        {errors.descripcion}
                    </Form.Control.Feedback>
                </Form.Group>

                <Row>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Teléfono de contacto *</Form.Label>
                            <Form.Control
                                type="tel"
                                name="telefono"
                                value={formData.telefono}
                                onChange={handleChange}
                                placeholder="Ej: +51 987 654 321"
                                isInvalid={!!errors.telefono}
                            />
                            <Form.Control.Feedback type="invalid">
                                {errors.telefono}
                            </Form.Control.Feedback>
                        </Form.Group>
                    </Col>

                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Correo electrónico de contacto *</Form.Label>
                            <Form.Control
                                type="email"
                                name="emailContacto"
                                value={formData.emailContacto}
                                onChange={handleChange}
                                placeholder="Ej: contacto@mitienda.com"
                                isInvalid={!!errors.emailContacto}
                            />
                            <Form.Control.Feedback type="invalid">
                                {errors.emailContacto}
                            </Form.Control.Feedback>
                        </Form.Group>
                    </Col>
                </Row>

                <Row>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Dirección de la tienda *</Form.Label>
                            <Form.Control
                                type="text"
                                name="direccion"
                                value={formData.direccion}
                                onChange={handleChange}
                                placeholder="Ej: Av. Los Olivos 123, Lima"
                                isInvalid={!!errors.direccion}
                            />
                            <Form.Control.Feedback type="invalid">
                                {errors.direccion}
                            </Form.Control.Feedback>
                        </Form.Group>
                    </Col>

                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Documento de identidad del propietario *</Form.Label>
                            <Form.Control
                                type="text"
                                name="documentoIdentidad"
                                value={formData.documentoIdentidad}
                                onChange={handleChange}
                                placeholder="DNI o CE del dueño"
                                isInvalid={!!errors.documentoIdentidad}
                            />
                            <Form.Control.Feedback type="invalid">
                                {errors.documentoIdentidad}
                            </Form.Control.Feedback>
                        </Form.Group>
                    </Col>
                </Row>

                <Row>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Sitio web *</Form.Label>
                            <Form.Control
                                type="url"
                                name="sitioWeb"
                                value={formData.sitioWeb}
                                onChange={handleChange}
                                placeholder="https://www.mi-tienda.com"
                                isInvalid={!!errors.sitioWeb}
                            />
                            <Form.Control.Feedback type="invalid">
                                {errors.sitioWeb}
                            </Form.Control.Feedback>
                        </Form.Group>
                    </Col>

                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Redes sociales</Form.Label>
                            <Form.Control
                                type="text"
                                name="redesSociales"
                                value={formData.redesSociales}
                                onChange={handleChange}
                                placeholder="Opcional - Ej: @mitienda.pe"
                            />
                        </Form.Group>
                    </Col>
                </Row>

                <Row>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Años de experiencia</Form.Label>
                            <Form.Control
                                type="number"
                                min="0"
                                name="aniosExperiencia"
                                value={formData.aniosExperiencia}
                                onChange={handleChange}
                                placeholder="Opcional - Ej: 5"
                            />
                        </Form.Group>
                    </Col>

                    <Col md={6}>
                        <Form.Group className="mb-4">
                            <Form.Label>Enlace a foto del local físico</Form.Label>
                            <Form.Control
                                type="url"
                                name="fotoLocal"
                                value={formData.fotoLocal}
                                onChange={handleChange}
                                placeholder="Opcional - Ej: https://i.imgur.com/foto-local.jpg"
                            />
                        </Form.Group>
                    </Col>
                </Row>

                <div className="text-center">
                    <Button type="submit" variant="primary" className="px-4 py-2" disabled={loading}>
                        {loading ? (
                            <>
                                <Spinner as="span" animation="border" size="sm" /> Enviando...
                            </>
                        ) : (
                            "Enviar solicitud"
                        )}
                    </Button>
                </div>

                {/* Mensaje general debajo del botón */}
                <div className="mt-3 text-center">
                    {mensaje && <Alert variant="success">{mensaje}</Alert>}
                    {errorGeneral && <Alert variant="danger">{errorGeneral}</Alert>}
                </div>
            </Form>
        </Card>
    );
};

export default SolicitudTiendaForm;
