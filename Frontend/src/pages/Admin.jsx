import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  Container,
  Row,
  Col,
  Card,
  Button,
  Nav,
  Table,
  Badge,
} from "react-bootstrap";
import {
  Speedometer2,
  Shop,
  People,
  BoxSeam,
  Globe,
  Key,
  BoxArrowRight,
  Gear,
  Search,
  List,
  XLg,
  ArrowBarLeft,
} from "react-bootstrap-icons";

// Componente de Tarjeta de Métrica
const MetricCard = ({ icon: Icon, title, value, colorClass }) => (
  <Card className="text-center shadow-sm h-100">
    <Card.Body>
      <Icon size={40} className={`mb-2 text-${colorClass}`} />
      <h4 className="fw-bold mb-0">{value}</h4>
      <p className="text-muted mb-0">{title}</p>
    </Card.Body>
  </Card>
);

// Componente de Navegación Lateral (Sidebar)
const Sidebar = ({ handleLogout, isOpen, setIsOpen }) => (
  // Estilos para Sidebar fijo, alto 100vh y colapsable
  <div
    className={`bg-dark text-white p-3 d-flex flex-column transition-transform shadow-lg`}
    style={{
      width: "250px",
      height: "100vh",
      position: "fixed",
      zIndex: 1030,
      transform: isOpen ? "translateX(0)" : "translateX(-250px)",
      transition: "transform 0.3s ease-in-out",
    }}
  >
    <div className="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
      <h3 className="mb-0">
        COMPU<span className="fw-light">SEARCH</span>
      </h3>
      {/* Botón para cerrar el sidebar en cualquier pantalla */}
      <Button
        variant="dark"
        onClick={() => setIsOpen(false)}
        className="d-md-none"
      >
        <XLg size={20} />
      </Button>
    </div>

    <p className="text-muted text-uppercase small">
      Panel de Administrador Web
    </p>

    <Nav className="flex-column flex-grow-1">
      <Nav.Link href="#" className="text-white active bg-primary rounded my-1">
        <Speedometer2 className="me-2" />
        Dashboard
      </Nav.Link>
      <Nav.Link href="#" className="text-white my-1">
        <Shop className="me-2" />
        Gestionar Tiendas
      </Nav.Link>
      <Nav.Link href="#" className="text-white my-1">
        <People className="me-2" />
        Gestionar Usuarios
      </Nav.Link>
      <Nav.Link href="#" className="text-white my-1">
        <Globe className="me-2" />
        Métricas Globales
      </Nav.Link>
      <Nav.Link href="#" className="text-white my-1">
        <Key className="me-2" />
        Planes de Suscripción
      </Nav.Link>
    </Nav>

    <div className="mt-auto pt-3 border-top">
      <Button variant="outline-light" className="w-100" onClick={handleLogout}>
        <BoxArrowRight className="me-2" />
        Cerrar Sesión
      </Button>
    </div>
  </div>
);

// --- Componente Principal Admin ---
const Admin = () => {
  const navigate = useNavigate();
  const [userEmail, setUserEmail] = useState("Cargando...");
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);
  const [isMobile, setIsMobile] = useState(window.innerWidth < 768);

  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth < 768);
    };

    window.addEventListener("resize", handleResize);

    return () => window.removeEventListener("resize", handleResize);
  }, []);

  // Lógica de acceso (Mantenida)
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

      // Redirección de seguridad si no es el rol de 'EMPLEADO'
      if (rol !== "EMPLEADO") {
        if (rol === "USUARIO") navigate("/perfil");
        if (rol === "TIENDA") navigate("/admin-tienda");
      }

      setUserEmail(user.email);
    } catch (e) {
      console.error("Error al obtener datos de usuario:", e);
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      navigate("/login");
    }
  }, [navigate]);

  // Función de cerrar sesión (Mantenida)
  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("user");
    navigate("/login");
  };

  // Si aún está cargando el email o validando, muestra un mensaje
  if (userEmail === "Cargando...") {
    return (
      <div className="container my-5 text-center">
        Validando acceso y cargando panel...
      </div>
    );
  }

  // Estilos dinámicos para el área de contenido (para el efecto de "empujar")
  const contentStyle = {
    marginLeft: !isMobile && isSidebarOpen ? "250px" : "0",
    transition: "margin-left 0.3s ease-in-out",
    minHeight: "100vh",
    width: "100%",
  };

  return (
    <div className="d-flex">
      {/* 1. Sidebar Fijo y Colapsable */}
      <Sidebar
        handleLogout={handleLogout}
        isOpen={isSidebarOpen}
        setIsOpen={setIsSidebarOpen}
      />

      {/* 2. Overlay para móviles cuando el sidebar está abierto */}
      {isSidebarOpen && (
        <div
          className="position-fixed top-0 start-0 w-100 h-100 bg-dark bg-opacity-50 d-md-none"
          style={{ zIndex: 1025 }}
          onClick={() => setIsSidebarOpen(false)} // Cierra al hacer click fuera
        ></div>
      )}

      {/* 3. Contenido Principal que es "Empujado" */}
      <div className="flex-grow-1" style={contentStyle}>
        {/* Navbar Superior (Header) */}
        <header className="bg-white shadow-sm p-3 d-flex justify-content-between align-items-center">
          {/* Botón de menú para abrir el sidebar (visible si está cerrado o en móvil) */}
          <Button
            variant="info"
            className="me-3"
            onClick={() => setIsSidebarOpen(!isSidebarOpen)}
          >
            {isSidebarOpen && !isMobile ? (
              <ArrowBarLeft size={24} />
            ) : (
              <List size={24} />
            )}
          </Button>

          <h5 className="mb-0">Dashboard de Administración Web</h5>

          <div className="d-flex align-items-center">
            <span className="text-muted small me-3 d-none d-sm-inline">
              Base de datos: --
            </span>
            <Button variant="outline-primary" size="sm">
              Actualizar
            </Button>
          </div>
        </header>

        {/* Main Content Area */}
        <Container fluid className="p-4">
          {/* Título y Bienvenida */}
          <Row className="mb-4">
            <Col>
              <h4>
                Bienvenido,{" "}
                <span className="text-primary">{userEmail.split("@")[0]}</span>
              </h4>
            </Col>
          </Row>

          <Row className="g-4 mb-4">
            <Col lg={3} sm={6}>
              <MetricCard
                icon={Shop}
                title="Tiendas Activas"
                value="--"
                colorClass="info"
              />
            </Col>
            <Col lg={3} sm={6}>
              <MetricCard
                icon={People}
                title="Usuarios Registrados"
                value="--"
                colorClass="success"
              />
            </Col>
            <Col lg={3} sm={6}>
              <MetricCard
                icon={BoxSeam}
                title="Productos Totales"
                value="--"
                colorClass="warning"
              />
            </Col>
            <Col lg={3} sm={6}>
              <MetricCard
                icon={Globe}
                title="Actualiz. hoy"
                value="--"
                colorClass="danger"
              />
            </Col>
          </Row>

          {/* Fila de Tablas y Planes */}
          <Row className="g-4">
            {/* Columna Izquierda (Tiendas y Usuarios) */}
            <Col lg={8}>
              {/* Gestión de Tiendas */}
              <Card className="shadow-sm mb-4">
                <Card.Header className="h5">Gestión de Tiendas</Card.Header>
                <Card.Body>
                  <Nav variant="tabs" defaultActiveKey="todas">
                    <Nav.Item>
                      <Nav.Link eventKey="todas">Todas las Tiendas</Nav.Link>
                    </Nav.Item>
                    <Nav.Item>
                      <Nav.Link eventKey="suspendidas">
                        Tiendas Suspendidas
                      </Nav.Link>
                    </Nav.Item>
                  </Nav>
                  <Table hover responsive className="mt-3 small">
                    <thead>
                      <tr>
                        <th>NOMBRE</th>
                        <th>PRODUCTOS</th>
                        <th>ESTADO</th>
                        <th>ACCIONES</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td>--</td>
                        <td>--</td>
                        <td>
                          <Badge bg="success">--</Badge>
                        </td>
                        <td>
                          <Button variant="outline-primary" size="sm">
                            Observación
                          </Button>
                        </td>
                      </tr>
                    </tbody>
                  </Table>
                  <div className="d-flex justify-content-center">
                    <Button variant="light" size="sm" className="me-2">
                      ← Anterior
                    </Button>
                    <Button variant="light" size="sm">
                      Siguiente →
                    </Button>
                  </div>
                </Card.Body>
              </Card>

              {/* Gestión de Usuarios */}
              <Card className="shadow-sm">
                <Card.Header className="h5">Gestión de Usuarios</Card.Header>
                <Card.Body>
                  <Table hover responsive className="small">
                    <thead>
                      <tr>
                        <th>NOMBRE</th>
                        <th>REGISTRO</th>
                        <th>ESTADO</th>
                        <th>ACCIONES</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td>--</td>
                        <td>--</td>
                        <td>
                          <Badge bg="success">--</Badge>
                        </td>
                        <td>
                          <Button variant="outline-primary" size="sm">
                            Observación
                          </Button>
                        </td>
                      </tr>
                    </tbody>
                  </Table>
                </Card.Body>
              </Card>
            </Col>

            {/* Columna Derecha (Métricas y Planes) */}
            <Col lg={4}>
              {/* Reporte de Métricas Globales */}
              <Card className="shadow-sm mb-4">
                <Card.Header className="h5">
                  Reporte de Métricas Globales
                </Card.Header>
                <Card.Body>
                  <div className="mb-2">
                    <div className="d-flex justify-content-between small">
                      <span>Total de Búsquedas</span>
                      <span>--</span>
                    </div>
                    <div className="progress" style={{ height: "8px" }}>
                      <div
                        className="progress-bar bg-primary"
                        role="progressbar"
                        style={{ width: "100%" }}
                      ></div>
                    </div>
                  </div>
                  <div className="mb-2">
                    <div className="d-flex justify-content-between small">
                      <span>Comparaciones realizadas</span>
                      <span>--</span>
                    </div>
                    <div className="progress" style={{ height: "8px" }}>
                      <div
                        className="progress-bar bg-success"
                        role="progressbar"
                        style={{ width: "100%" }}
                      ></div>
                    </div>
                  </div>
                  <div className="mb-2">
                    <div className="d-flex justify-content-between small">
                      <span>Productos Actualizados</span>
                      <span>--</span>
                    </div>
                    <div className="progress" style={{ height: "8px" }}>
                      <div
                        className="progress-bar bg-danger"
                        role="progressbar"
                        style={{ width: "100%" }}
                      ></div>
                    </div>
                  </div>
                </Card.Body>
              </Card>

              {/* Configurar Planes de Suscripción */}
              <Card className="shadow-sm">
                <Card.Header className="h5">
                  Configurar Planes de Suscripción
                </Card.Header>
                <Card.Body>
                  {/* Plan Básico */}
                  <div className="d-flex justify-content-between align-items-start mb-3 border-bottom pb-3">
                    <div>
                      <h6 className="mb-0">Plan Básico</h6>
                      <p className="text-muted small">$ 19/mes</p>
                      <ul className="small list-unstyled">
                        <li>• 1000 productos máx.</li>
                        <li>• Actualizaciones cada 6 horas</li>
                      </ul>
                    </div>
                    <Button variant="outline-primary" size="sm">
                      Editar
                    </Button>
                  </div>
                  {/* Plan Premium */}
                  <div className="d-flex justify-content-between align-items-start mb-3 border-bottom pb-3">
                    <div>
                      <h6 className="mb-0">Plan Premium</h6>
                      <p className="text-muted small">$ 49/mes</p>
                      <ul className="small list-unstyled">
                        <li>• 5000 productos máx.</li>
                        <li>• Actualizaciones cada hora</li>
                      </ul>
                    </div>
                    <Button variant="outline-primary" size="sm">
                      Editar
                    </Button>
                  </div>
                  {/* Plan Enterprise */}
                  <div className="d-flex justify-content-between align-items-start mb-3">
                    <div>
                      <h6 className="mb-0">Plan Enterprise</h6>
                      <p className="text-muted small">$ 99/mes</p>
                      <ul className="small list-unstyled">
                        <li>• Productos ilimitados</li>
                        <li>• Actualizaciones en tiempo real</li>
                      </ul>
                    </div>
                    <Button variant="outline-primary" size="sm">
                      Editar
                    </Button>
                  </div>
                  <Button variant="success" className="w-100 mt-2">
                    Crear Nuevo Plan
                  </Button>
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </Container>
      </div>
    </div>
  );
};

export default Admin;
