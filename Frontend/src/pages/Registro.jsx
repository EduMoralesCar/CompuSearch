import React, { useState } from "react";
import { Form, Button } from "react-bootstrap";

const Registro = () => {
  const [formData, setFormData] = useState({
    username: "",
    email: "",
    password: "",
  });

  const [errors, setErrors] = useState({});
  const [showPassword, setShowPassword] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const validate = () => {
    let newErrors = {};

    if (!formData.username.trim()) {
      newErrors.username = "El nombre de usuario es obligatorio";
    } else if (formData.username.length < 3) {
      newErrors.username = "Debe tener al menos 3 caracteres";
    }

    if (!formData.email) {
      newErrors.email = "El correo es obligatorio";
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = "El correo no es válido";
    }

    if (!formData.password) {
      newErrors.password = "La contraseña es obligatoria";
    } else if (formData.password.length < 8) {
      newErrors.password = "Debe tener al menos 8 caracteres";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validate()) {
    }
  };

  return (
    <div className="container my-4 d-flex justify-content-center">
      <div
        className="card shadow p-4"
        style={{ maxWidth: "500px", width: "100%" }}
      >
        <h3 className="text-center text-primary fw-bold mb-4">REGISTRARSE</h3>
        <Form onSubmit={handleSubmit}>
          {/* Nombre de usuario */}
          <Form.Group className="mb-3">
            <Form.Label>Nombre de usuario</Form.Label>
            <Form.Control
              type="text"
              name="username"
              value={formData.username}
              onChange={handleChange}
              isInvalid={!!errors.username}
              placeholder="Ingresa tu usuario"
            />
            <Form.Control.Feedback type="invalid">
              {errors.username}
            </Form.Control.Feedback>
          </Form.Group>

          {/* Email */}
          <Form.Group className="mb-3">
            <Form.Label>Dirección de correo electrónico</Form.Label>
            <Form.Control
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              isInvalid={!!errors.email}
              placeholder="ejemplo@correo.com"
            />
            <Form.Control.Feedback type="invalid">
              {errors.email}
            </Form.Control.Feedback>
          </Form.Group>

          {/* Contraseña */}
          <Form.Group className="mb-3">
            <Form.Label>Contraseña</Form.Label>
            <div className="input-group">
              <Form.Control
                type={showPassword ? "text" : "password"}
                name="password"
                value={formData.password}
                onChange={handleChange}
                isInvalid={!!errors.password}
                placeholder="Mínimo 8 caracteres"
                className="rounded-end-0"
              />
              <Button
                variant="outline-secondary"
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="rounded-start-0 rounded-end" 
              >
                <i
                  className={`bi ${showPassword ? "bi-eye-slash" : "bi-eye"}`}
                />
              </Button>
              <Form.Control.Feedback type="invalid">
                {errors.password}
              </Form.Control.Feedback>
            </div>
          </Form.Group>

          {/* Texto */}
          <p className="text-muted small mt-2">
            Tus datos personales serán utilizados para optimizar tu experiencia
            en la página, administrar el acceso a tu cuenta y cumplir con
            nuestra política de privacidad.
          </p>

          {/* Boton */}
          <div className="d-grid">
            <Button variant="primary" type="submit">
              REGISTRARSE
            </Button>
          </div>
        </Form>
      </div>
    </div>
  );
};

export default Registro;
