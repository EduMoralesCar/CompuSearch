import React, { useEffect, useRef, useState } from "react";
import { NavLink, useLocation, useNavigate } from "react-router-dom";
import logo from "../assets/logo.png";
import { Collapse } from "bootstrap";

const Header = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const bsRef = useRef(null);
  const navbarCollapseId = "navbarCompuSearch";

  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem("token");
    setIsAuthenticated(!!token);
  }, [location]);

  useEffect(() => {
    const el = document.getElementById(navbarCollapseId);
    if (!el) return;

    bsRef.current =
      Collapse.getInstance(el) || new Collapse(el, { toggle: false });

    return () => {
      if (bsRef.current) {
        bsRef.current.dispose();
        bsRef.current = null;
      }
    };
  }, []);

  useEffect(() => {
    if (bsRef.current) {
      bsRef.current.hide();
    }
  }, [location]);

  const toggleNavbar = () => {
    if (bsRef.current) bsRef.current.toggle();
  };

  const handleProfileClick = () => {
    if (isAuthenticated) {
      navigate("/perfil");
    } else {
      navigate("/login");
    }
  };

  return (
    <header className="fixed-top">
      <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
        <div className="container-fluid">
          {/* Logo */}
          <NavLink className="navbar-brand d-flex align-items-center" to="/">
            <img src={logo} alt="Logo" height="40" className="me-2" />
          </NavLink>

          {/* Botón toggle */}
          <button
            className="navbar-toggler"
            type="button"
            aria-controls={navbarCollapseId}
            aria-expanded="false"
            aria-label="Toggle navigation"
            onClick={toggleNavbar}
          >
            <span className="navbar-toggler-icon"></span>
          </button>

          {/* Contenido colapsable */}
          <div className="collapse navbar-collapse" id={navbarCollapseId}>
            {/* ... resto de tus menús ... */}
          </div>

          {/* Iconos en escritorio */}
          <div className="d-none d-lg-flex align-items-center order-lg-3 ms-4">
            <button
              onClick={handleProfileClick}
              className="btn btn-link text-white p-0"
            >
              <i className="bi bi-person-fill fs-4"></i>
            </button>
            <NavLink to="/tiendas" className="ms-3 text-white">
              <i className="bi bi-shop-window fs-4"></i>
            </NavLink>
          </div>
        </div>
      </nav>

      {/* Barra secundaria */}
      <nav className="navbar navbar-expand navbar-dark bg-dark d-none d-lg-block">
        <div className="container-fluid">
          <ul className="navbar-nav mx-auto" style={{ gap: "5rem" }}>
            <li className="nav-item">
              <NavLink to="/" className="nav-link">
                Inicio
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink to="/componentes" className="nav-link">
                Componentes
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink to="/tiendas" className="nav-link">
                Tiendas
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink to="/categorias" className="nav-link">
                Categorías
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink to="/builds" className="nav-link">
                Armado (Builds)
              </NavLink>
            </li>
          </ul>
        </div>
      </nav>
    </header>
  );
};

export default Header;
