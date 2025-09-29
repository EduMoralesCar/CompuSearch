import React, { useState } from "react";
import { Form, Button } from "react-bootstrap";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const Registro = () => {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const navigate = useNavigate();
  const [errors, setErrors] = useState({});
  const [serverError, setServerError] = useState("");
  const [showPassword, setShowPassword] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const validate = () => {
    let newErrors = {};

    if (!formData.email) {
      newErrors.email = "El correo es obligatorio";
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = "El correo no es válido";
    }

    if (!formData.password) {
      newErrors.password = "La contraseña es obligatoria";
    } else {
      const passwordRegex = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$/;
      if (!passwordRegex.test(formData.password)) {
        newErrors.password =
          "La contraseña debe tener al menos 8 caracteres, incluyendo mayúscula, minúscula, número y carácter especial";
      }
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setServerError("");
    if (validate()) {
      try {
        const response = await axios.post(
          "http://localhost:8080/auth/register",
          {
            email: formData.email,
            contrasena: formData.password,
            tipoUsuario: "USUARIO",
          }
        );

        localStorage.setItem("token", response.data.data.token);
        navigate("/");
      } catch (error) {
        if (error.response && error.response.data.error) {
          setServerError(error.response.data.error);
        } else {
          setServerError("Error de conexión con el servidor");
        }
      }
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

          {/* Mensaje de error del servidor */}
          {serverError && (
            <p className="text-danger mt-3 text-center">{serverError}</p>
          )}
        </Form>
      </div>
    </div>
  );
};

export default Registro;