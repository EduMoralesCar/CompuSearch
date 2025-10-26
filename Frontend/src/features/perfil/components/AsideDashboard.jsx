import { Nav } from "react-bootstrap";
import Logo from "../../../assets/logo/logo.webp";
import { NavLink } from "react-router-dom";
import BotonLogout from "../components/auxiliar/BotonLogout";

const AsideDashboard = ({ setVistaActual, vistaActual, setSidebarAbierto }) => {
  const handleSelect = (vista) => {
    setVistaActual(vista);

    // Cierra el sidebar solo si estamos en pantalla móvil (< 992px)
    if (window.innerWidth < 992) {
      setSidebarAbierto(false);
    }
  };

  return (
    <div className="d-flex flex-column justify-content-between h-100 p-3">
      {/* Contenedor superior para Logo y Nav */}
      <div>
        <NavLink className="navbar-brand d-flex align-items-center" to="/">
          <img src={Logo} alt="Logo" height="50" className="mb-4" />
        </NavLink>
        <Nav
          variant="pills"
          className="flex-column gap-2"
          activeKey={vistaActual}
          onSelect={handleSelect}
        >
          <Nav.Item>
            <Nav.Link eventKey="categorias">Categorías</Nav.Link>
          </Nav.Item>
          <Nav.Item>
            <Nav.Link eventKey="etiquetas">Etiquetas</Nav.Link>
          </Nav.Item>
          <Nav.Item>
            <Nav.Link eventKey="usuarios">Usuarios</Nav.Link>
          </Nav.Item>
          <Nav.Item>
            <Nav.Link eventKey="solicitudes">Solicitudes</Nav.Link>
          </Nav.Item>
          <Nav.Item>
            <Nav.Link eventKey="incidencias">Incidencias</Nav.Link>
          </Nav.Item>
          <div className="mt-auto">
            <BotonLogout className="w-100" />
          </div>
        </Nav>
      </div>
    </div>
  );
};

export default AsideDashboard;
