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
  Form,
} from "react-bootstrap";
import {
  Speedometer2,
  BoxSeam,
  CurrencyDollar,
  GraphUp,
  BoxArrowRight,
  List,
  XLg,
  Download,
  Upload,
  Tools,
  ArrowBarLeft,
  ArrowBarRight,
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
      {/* Botón para cerrar en móvil (el overlay también lo cierra) */}
      <Button
        variant="dark"
        onClick={() => setIsOpen(false)}
        className="d-md-none" 
      >
        <XLg size={20} />
      </Button>
    </div>
    {/* ... (Resto de la navegación) ... */}
    <p className="text-muted text-uppercase small">Panel de Tienda</p>

    <Nav className="flex-column flex-grow-1">
      <Nav.Link href="#" className="text-white active bg-info rounded my-1">
        <Speedometer2 className="me-2" />
        Dashboard
      </Nav.Link>
      <Nav.Link href="#" className="text-white my-1">
        <BoxSeam className="me-2" />
        Gestión de Productos
      </Nav.Link>
      <Nav.Link href="#" className="text-white my-1">
        <GraphUp className="me-2" />
        Métricas
      </Nav.Link>
      <Nav.Link href="#" className="text-white my-1">
        <CurrencyDollar className="me-2" />
        Suscripciones
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

// Componente Principal AdminTienda
const AdminTienda = () => {
  const navigate = useNavigate();
  const [userEmail, setUserEmail] = useState("Cargando...");
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);
  const [isMobile, setIsMobile] = useState(window.innerWidth < 768);

  // Lógica para detectar el tamaño de la pantalla (Responsividad)
  useEffect(() => {
    const handleResize = () => {
      const newIsMobile = window.innerWidth < 768;
      setIsMobile(newIsMobile);
      // En móvil, inicia cerrado por defecto. En desktop, abierto.
      if (newIsMobile) {
         setIsSidebarOpen(false);
      } else {
         setIsSidebarOpen(true);
      }
    };

    window.addEventListener("resize", handleResize);
    
    // Configuración inicial al cargar
    if (window.innerWidth < 768) {
        setIsSidebarOpen(false);
    } else {
        setIsSidebarOpen(true);
    }

    return () => window.removeEventListener("resize", handleResize);
  }, []);

  // Lógica de acceso y validación de rol
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

      if (rol !== "TIENDA") {
        if (rol === "USUARIO") navigate("/perfil");
        if (rol === "EMPLEADO") navigate("/admin");
      }

      setUserEmail(user.email);
    } catch (e) {
      console.error("Error al obtener datos de usuario:", e);
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      navigate("/login");
    }
  }, [navigate]);

  // Función de cerrar sesión
  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("user");
    navigate("/login");
  };

  if (userEmail === "Cargando...") {
    return (
      <div className="container my-5 text-center">
        Validando acceso y cargando panel...
      </div>
    );
  }

  // Estilos dinámicos para el área de contenido (Empuje en desktop)
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

      {/* 2. Overlay para móviles cuando el sidebar está abierto (Superposición) */}
      {isSidebarOpen && isMobile && (
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
          
          {/* BOTÓN DE TOGGLE*/}
          <Button
            variant="info"
            className="me-3" 
            onClick={() => setIsSidebarOpen(!isSidebarOpen)}
          >
            {/* Cambiar ícono según el estado */}
            {isSidebarOpen && !isMobile ? <ArrowBarLeft size={24} /> : <List size={24} />}
          </Button>

          <h5 className="mb-0">Dashboard de Tienda - {userEmail.split("@")[0]}</h5>

          <div className="d-flex align-items-center">
            <span className="text-muted small me-3 d-none d-sm-inline">
              Última actualización: --
            </span>
            <Button variant="outline-info" size="sm">
              Actualizar
            </Button>
          </div>
        </header>

        {/* Main Content Area (Resto del contenido sin cambios) */}
        <Container fluid className="p-4">
          {/* Métrica de Productos */}
          <Row className="g-4 mb-4">
            {/* ... (Cards de Métricas) ... */}
            <Col lg={3} sm={6}>
              <MetricCard
                icon={BoxSeam}
                title="Productos Activos"
                value="--"
                colorClass="primary"
              />
            </Col>
            <Col lg={3} sm={6}>
              <MetricCard
                icon={BoxSeam}
                title="Productos Publicados"
                value="--"
                colorClass="info"
              />
            </Col>
            <Col lg={3} sm={6}>
              <MetricCard
                icon={CurrencyDollar}
                title="Conversiones"
                value="--"
                colorClass="success"
              />
            </Col>
            <Col lg={3} sm={6}>
              <MetricCard
                icon={GraphUp}
                title="Actualiz. hoy"
                value="--"
                colorClass="danger"
              />
            </Col>
          </Row>

          {/* Fila Principal: Gestión de Productos y Resumen de Métricas */}
          <Row className="g-4">
            {/* Columna Izquierda (Gestión de Productos y Datos de Tienda) */}
            <Col lg={8}>
              {/* Gestión de Productos */}
              <Card className="shadow-sm mb-4">
                <Card.Header className="h5">Gestión de Productos</Card.Header>
                <Card.Body>
                  <div className="d-flex justify-content-end mb-3">
                    <Button variant="outline-primary" className="me-2">
                      <Upload className="me-2" />
                      Importar
                    </Button>
                    <Button variant="primary">
                      <Tools className="me-2" />
                      Nuevo Producto
                    </Button>
                  </div>

                  <Nav variant="tabs" defaultActiveKey="lista">
                    <Nav.Item>
                      <Nav.Link eventKey="lista">Lista de Productos</Nav.Link>
                    </Nav.Item>
                    <Nav.Item>
                      <Nav.Link eventKey="csv">Importar desde CSV</Nav.Link>
                    </Nav.Item>
                  </Nav>
                  <Table hover responsive className="mt-3 small">
                    <thead>
                      <tr>
                        <th>NOMBRE</th>
                        <th>CATEGORÍA</th>
                        <th>PRECIO</th>
                        <th>STOCK</th>
                        <th>ACCIONES</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td>--</td>
                        <td>--</td>
                        <td>S/ --</td>
                        <td>
                          <Badge bg="success">En Stock</Badge>
                        </td>
                        <td>
                          <Button variant="outline-info" size="sm">
                            Editar
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

              {/* Datos de Tienda */}
              <Card className="shadow-sm">
                <Card.Header className="h5">Datos de la Tienda</Card.Header>
                <Card.Body>
                  <Form>
                    <Row>
                      <Col md={6}>
                        <Form.Group className="mb-3">
                          <Form.Label>Nombre de la Empresa</Form.Label>
                          <Form.Control type="text" placeholder="Mi Tienda S.A.C." />
                        </Form.Group>
                      </Col>
                      <Col md={6}>
                        <Form.Group className="mb-3">
                          <Form.Label>URL de la Tienda</Form.Label>
                          <Form.Control type="text" placeholder="https://mi-tienda.com" />
                        </Form.Group>
                      </Col>
                    </Row>
                    <Row>
                      <Col md={6}>
                        <Form.Group className="mb-3">
                          <Form.Label>Teléfono</Form.Label>
                          <Form.Control type="text" placeholder="987 654 321" />
                        </Form.Group>
                      </Col>
                      <Col md={6}>
                        <Form.Group className="mb-3">
                          <Form.Label>Email</Form.Label>
                          <Form.Control type="email" placeholder="contacto@mitienda.com" />
                        </Form.Group>
                      </Col>
                    </Row>
                    <Form.Group className="mb-3">
                      <Form.Label>Descripción</Form.Label>
                      <Form.Control as="textarea" rows={3} placeholder="Breve descripción de la tienda..." />
                    </Form.Group>
                    <Button variant="primary" className="mt-2">
                      Guardar cambios
                    </Button>
                  </Form>
                </Card.Body>
              </Card>
            </Col>

            {/* Columna Derecha (Resumen de Métricas y Suscripción) */}
            <Col lg={4}>
              {/* Resumen de Métricas */}
              <Card className="shadow-sm mb-4">
                <Card.Header className="h5">Resumen de Métricas</Card.Header>
                <Card.Body>
                  <div className="mb-2">
                    <div className="d-flex justify-content-between small">
                      <span>Producto comparado</span>
                      <span>--</span>
                    </div>
                    <div className="progress" style={{ height: "8px" }}>
                      <div
                        className="progress-bar bg-primary"
                        role="progressbar"
                        style={{ width: "90%" }}
                      ></div>
                    </div>
                  </div>
                  <div className="mb-2">
                    <div className="d-flex justify-content-between small">
                      <span>Visitas a tu tienda</span>
                      <span>--</span>
                    </div>
                    <div className="progress" style={{ height: "8px" }}>
                      <div
                        className="progress-bar bg-success"
                        role="progressbar"
                        style={{ width: "65%" }}
                      ></div>
                    </div>
                  </div>
                  <div className="mb-2">
                    <div className="d-flex justify-content-between small">
                      <span>Conversiones</span>
                      <span>--</span>
                    </div>
                    <div className="progress" style={{ height: "8px" }}>
                      <div
                        className="progress-bar bg-warning"
                        role="progressbar"
                        style={{ width: "30%" }}
                      ></div>
                    </div>
                  </div>
                </Card.Body>
              </Card>

              {/* Acciones Rápidas */}
              <Card className="shadow-sm mb-4">
                <Card.Header className="h5">Acciones Rápidas</Card.Header>
                <Card.Body>
                  <div className="d-grid gap-2">
                    <Button variant="outline-info">Actualizar Precios</Button>
                    <Button variant="outline-info">
                      Exportar Productos <Download className="ms-1" />
                    </Button>
                    <Button variant="outline-info">Generar Reporte</Button>
                    <Button variant="outline-info">Soporte</Button>
                  </div>
                </Card.Body>
              </Card>

              {/* Plan de Suscripción */}
              <Card className="shadow-sm">
                <Card.Header className="d-flex justify-content-between align-items-center h5">
                    Plan de Suscripción
                    <Button variant="outline-primary" size="sm">Cambiar Plan</Button>
                </Card.Header>
                <Card.Body>
                  <h6 className="mb-1 text-info">Plan Premium (Actual)</h6>
                  <p className="text-muted small mb-3">Hasta 5000 productos</p>
                  <ul className="small list-unstyled">
                    <li>• 5000 productos máx.</li>
                    <li>• Actualizaciones cada hora</li>
                    <li>• Soporte prioritario</li>
                    <li>• Reportes avanzados</li>
                  </ul>
                  <Button variant="danger" className="w-100 mt-2">
                    Renovar Suscripción
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

export default AdminTienda;