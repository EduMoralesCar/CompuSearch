import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  Container,
  Row,
  Col,
  Card,
  ListGroup,
  Button,
  Alert,
} from "react-bootstrap";
import {
  PersonCircle,
  HouseDoor,
  ShieldLock,
  BoxArrowRight,
} from "react-bootstrap-icons";

const InformacionPersonal = ({ email }) => (
  <div>
    <h3>Información Personal</h3>
    <p>Aquí puedes ver y editar tus detalles personales.</p>
    <Card>
      <Card.Body>
        <Card.Title>Detalles</Card.Title>
        <ListGroup variant="flush">
          <ListGroup.Item>
            <strong>Correo Electrónico:</strong> {email}
          </ListGroup.Item>
          <ListGroup.Item>
            <strong>Nombre:</strong> Usuario
          </ListGroup.Item>
        </ListGroup>
        <Button variant="outline-primary" size="sm" className="mt-3">
          Editar Perfil
        </Button>
      </Card.Body>
    </Card>
  </div>
);

const MisConstrucciones = () => (
  <div>
    <h3>Mis Construcciones</h3>
    <p>Aún no tienes construcciones registradas.</p>
  </div>
);

const Seguridad = () => (
  <div>
    <h3>Cambiar Contraseña</h3>
    <Card className="p-3">
      <p>Usa este formulario para actualizar tu contraseña.</p>
      {/* Un formulario simple para ilustrar */}
      <form
        onSubmit={(e) => {
          e.preventDefault();
          alert("Contraseña cambiada con éxito (simulado)");
        }}
      >
        <div className="mb-3">
          <label htmlFor="currentPass" className="form-label">
            Contraseña Actual
          </label>
          <input
            type="password"
            id="currentPass"
            className="form-control"
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="newPass" className="form-label">
            Nueva Contraseña
          </label>
          <input
            type="password"
            id="newPass"
            className="form-control"
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="confirmPass" className="form-label">
            Confirmar Nueva Contraseña
          </label>
          <input
            type="password"
            id="confirmPass"
            className="form-control"
            required
          />
        </div>
        <Button variant="warning" type="submit">
          Actualizar Contraseña
        </Button>
      </form>
    </Card>
  </div>
);

// Componente Principal Perfil
const Perfil = () => {
  const navigate = useNavigate();
  const [userRole, setUserRole] = useState(null);
  const [email, setEmail] = useState("");
  const [usernameDisplay, setUsernameDisplay] = useState("");
  const [activeContent, setActiveContent] = useState("personal");

  // Lógica de acceso y redirección
  useEffect(() => {
    const token = localStorage.getItem("token");
    const userString = localStorage.getItem("user");

    if (!token || !userString) {
      navigate("/login");
      return;
    }

    try {
      const user = JSON.parse(userString);
      const rol = user.rol;
      const userEmail = user.email;

      setUserRole(rol);
      setEmail(userEmail);

      // Extraer la primera parte del correo
      if (userEmail) {
        const parts = userEmail.split("@");
        setUsernameDisplay(parts[0]);
      }

      // Redirección de seguridad
      if (rol === "EMPLEADO") {
        navigate("/admin");
      } else if (rol === "TIENDA") {
        navigate("/admin-tienda");
      }
    } catch (e) {
      console.error("Error al parsear usuario de localStorage:", e);
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      navigate("/login");
    }
  }, [navigate]);

  // Función de cerrar sesión (Sin cambios)
  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("user");
    navigate("/login");
  };

  // Función para renderizar el contenido dinámico (Sin cambios)
  const renderContent = () => {
    switch (activeContent) {
      case "personal":
        return <InformacionPersonal email={email} />;
      case "construcciones":
        return <MisConstrucciones />;
      case "seguridad":
        return <Seguridad />;
      default:
        return <InformacionPersonal email={email} />;
    }
  };

  // Muestra un estado de carga
  if (!userRole || userRole !== "USUARIO") {
    return (
      <Container className="my-5 text-center">
        Cargando perfil o redirigiendo...
      </Container>
    );
  }

  // El componente principal con la interfaz responsiva de React-Bootstrap
  return (
    <Container className="my-4">
      <h1 className="mb-4">Mi Perfil</h1>
      <Row>
        {/* Columna de la izquierda (Menú de Navegación) */}
        <Col md={4} className="mb-4">
          <Card>
            <Card.Header className="bg-primary text-white">
              {/* *** MODIFICACIÓN CLAVE: Usar usernameDisplay en lugar de email *** */}
              <h5 className="mb-0">
                <PersonCircle className="me-2" />
                {usernameDisplay}{" "}
                {/* Muestra solo la primera parte del correo */}
              </h5>
            </Card.Header>
            <Card.Body>
              <Card.Title>{userRole}</Card.Title>
              <ListGroup variant="flush">
                {/* Botones de navegación*/}
                <ListGroup.Item
                  action
                  onClick={() => setActiveContent("personal")}
                  active={activeContent === "personal"}
                >
                  <PersonCircle className="me-2" />
                  Información Personal
                </ListGroup.Item>
                <ListGroup.Item
                  action
                  onClick={() => setActiveContent("construcciones")}
                  active={activeContent === "construcciones"}
                >
                  <HouseDoor className="me-2" />
                  Mis Construcciones
                </ListGroup.Item>
                <ListGroup.Item
                  action
                  onClick={() => setActiveContent("seguridad")}
                  active={activeContent === "seguridad"}
                >
                  <ShieldLock className="me-2" />
                  Seguridad (Cambiar Contraseña)
                </ListGroup.Item>
              </ListGroup>

              {/* Botón de Cerrar Sesión */}
              <Button
                variant="danger"
                className="w-100 mt-3"
                onClick={handleLogout}
              >
                <BoxArrowRight className="me-2" />
                Cerrar sesión
              </Button>
            </Card.Body>
          </Card>
        </Col>

        {/* Columna de la derecha (Contenido Dinámico) */}
        <Col md={8}>
          <Card className="p-4 h-100">{renderContent()}</Card>
        </Col>
      </Row>
    </Container>
  );
};

export default Perfil;
