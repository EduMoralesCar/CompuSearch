import axios from "axios";
import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Form, Button, Card } from "react-bootstrap";

const Login = () => {
  const [showPassword, setShowPassword] = useState(false);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errors, setErrors] = useState({});
  const [serverError, setServerError] = useState("");
  const [ip, setIp] = useState("desconocido"); // valor por defecto
  const navigate = useNavigate();

  // Obtener IP pública al montar el componente
  useEffect(() => {
    const getIp = async () => {
      try {
        const res = await axios.get("https://api.ipify.org?format=json");
        setIp(res.data.ip);
      } catch (err) {
        console.error("No se pudo obtener IP:", err);
      }
    };
    getIp();
  }, []);

  const togglePassword = () => {
    setShowPassword(!showPassword);
  };

  // Validaciones
  const validate = () => {
    let newErrors = {};
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!email) {
      newErrors.email = "El correo es obligatorio";
    } else if (!emailRegex.test(email)) {
      newErrors.email = "Formato de correo inválido";
    }

    if (!password) {
      newErrors.password = "La contraseña es obligatoria";
    } else if (password.length < 8) {
      newErrors.password = "Debe tener al menos 8 caracteres";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // Enviar formulario
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;

    try {
      const response = await axios.post("http://localhost:8080/auth/login", {
        email: email,
        contrasena: password,
        dispositivo: `web-${ip}`,
      });

      const data = response.data;

      localStorage.setItem("token", data.token);
      localStorage.setItem("refreshToken", data.refreshToken);
      localStorage.setItem("user", JSON.stringify(data.data));

      navigate("/");
    } catch (error) {
      console.error("Error en login:", error);
      if (error.response && error.response.status === 401) {
        setServerError("Credenciales inválidas");
      } else {
        setServerError("Error en el servidor, intenta más tarde");
      }
    }
  };

  return (
    <div className="container d-flex justify-content-center align-items-center flex-grow-1">
      <div className="row w-100 mt-4">
        {/* Login */}
        <div className="col-md-6 mb-4">
          <Card className="shadow-sm p-4">
            <h3 className="fw-bold text-primary mb-4">Acceder</h3>
            <Form onSubmit={handleSubmit}>

              {/* Email */}
              <Form.Group className="mb-3" controlId="formEmail">
                <Form.Label>Correo electrónico</Form.Label>
                <Form.Control
                  type="email"
                  placeholder="Ingresa tu email"
                  value={email}
                  isInvalid={!!errors.email}
                  onChange={(e) => setEmail(e.target.value)}
                />
                <Form.Control.Feedback type="invalid">
                  {errors.email}
                </Form.Control.Feedback>
              </Form.Group>

              {/* Contraseña */}
              <Form.Group className="mb-3" controlId="formPassword">
                <Form.Label>Contraseña</Form.Label>
                <div className="input-group">
                  <Form.Control
                    type={showPassword ? "text" : "password"}
                    placeholder="••••••••"
                    value={password}
                    isInvalid={!!errors.password}
                    onChange={(e) => setPassword(e.target.value)}
                  />
                  <Button
                    variant="outline-secondary"
                    type="button"
                    onClick={togglePassword}
                  >
                    <i
                      className={`bi ${showPassword ? "bi-eye-slash" : "bi-eye"
                        }`}
                    />
                  </Button>
                </div>
                {errors.password && (
                  <div className="invalid-feedback d-block">
                    {errors.password}
                  </div>
                )}
              </Form.Group>

              {/* Opciones debajo de la contraseña */}
              <div className="d-flex justify-content-between align-items-center mb-3">
                <Form.Check
                  type="checkbox"
                  id="rememberMe"
                  label="Recuérdame"
                />
                <Link to="/forgot-password" className="text-decoration-none small">
                  ¿Olvidaste tu contraseña?
                </Link>
              </div>


              {/* Error del servidor */}
              {serverError && (
                <div className="alert alert-danger py-2">{serverError}</div>
              )}

              <Button variant="primary" type="submit" className="w-100">
                Iniciar Sesión
              </Button>
            </Form>
          </Card>
        </div>

        {/* Registro */}
        <div className="col-md-6 mb-4">
          <Card className="shadow-sm p-4 d-flex justify-content-center align-items-center">
            <h3 className="fw-bold text-primary mb-3">Registro</h3>
            <p className="text-muted text-center">
              ¿Aún no tienes cuenta? Regístrate gratis y accede a todos nuestros
              beneficios: guarda productos favoritos, arma builds personalizadas
              y recibe ofertas en componentes.
            </p>
            <Link to="/registro" className="btn btn-outline-primary w-50">
              Registrarse
            </Link>
          </Card>
        </div>
      </div>
    </div>
  );
};

export default Login;
