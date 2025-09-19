import React from "react";
import { NavLink } from "react-router-dom";
import logo from "../assets/logo.png";

const Header = () => {
  return (
    <header className="fixed-top">
      {/* Barra de navegación */}
      <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
        <div className="container-fluid">
          {/* Logo */}
          <NavLink className="navbar-brand d-flex align-items-center" to="/">
            <img src={logo} alt="Logo" height="40" className="me-2" />
          </NavLink>

          {/* Botón de toggle */}
          <button
            className="navbar-toggler"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#navbarCompuSearch"
            aria-controls="navbarCompuSearch"
            aria-expanded="false"
            aria-label="Toggle navigation"
          >
            <span className="navbar-toggler-icon"></span>
          </button>

          {/* Contenido que se colapsa para pantallas pequeñas */}
          <div className="collapse navbar-collapse" id="navbarCompuSearch">
            {/* Enlaces de navegación */}
            <ul className="navbar-nav d-block d-lg-none">
              <li className="nav-item">
                <NavLink
                  to="/"
                  className={({ isActive }) =>
                    "nav-link" + (isActive ? " active" : "")
                  }
                >
                  Inicio
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink
                  to="/componentes"
                  className={({ isActive }) =>
                    "nav-link" + (isActive ? " active" : "")
                  }
                >
                  Componentes
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink
                  to="/tiendas"
                  className={({ isActive }) =>
                    "nav-link" + (isActive ? " active" : "")
                  }
                >
                  Tiendas
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink
                  to="/categorias"
                  className={({ isActive }) =>
                    "nav-link" + (isActive ? " active" : "")
                  }
                >
                  Categorías
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink
                  to="/builds"
                  className={({ isActive }) =>
                    "nav-link" + (isActive ? " active" : "")
                  }
                >
                  Armado (Builds)
                </NavLink>
              </li>
            </ul>

            {/* Formulario de búsqueda */}
            <form
              className="d-flex col-12 col-lg-6 mx-auto mt-3 mt-lg-0 order-lg-2"
              role="search"
            >
              <div className="input-group">
                <input
                  className="form-control"
                  type="search"
                  id="inputBusqueda"
                  placeholder="¿Qué estás buscando?"
                  aria-label="Buscar"
                />
                <button className="btn btn-outline-light" type="submit">
                  <i className="bi bi-search fs-6 me-0"></i>
                </button>
              </div>
            </form>

            {/* Iconos de usuario y tienda solo para pantallas pequeñas */}
            <ul className="navbar-nav d-block d-lg-none mt-3">
              <li className="nav-item">
                <NavLink to="/login" className="nav-link">
                  <i className="bi bi-person-fill fs-4 me-2"></i>Iniciar Sesión
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink to="/tiendas" className="nav-link">
                  <i className="bi bi-shop-window fs-4 me-2"></i>Tiendas
                </NavLink>
              </li>
            </ul>
          </div>

          {/* Iconos de usuario y tienda solo para pantallas grandes */}
          <div className="d-none d-lg-flex align-items-center order-lg-3 ms-4">
            <NavLink to="/login" className="text-white">
              <i className="bi bi-person-fill fs-4"></i>
            </NavLink>
            <NavLink to="/tiendas" className="ms-3 text-white">
              <i className="bi bi-shop-window fs-4"></i>
            </NavLink>
          </div>
        </div>
      </nav>

      {/* Barra de navegación inferior solo en pantallas grandes */}
      <nav className="navbar navbar-expand navbar-dark bg-dark d-none d-lg-block">
        <div className="container-fluid">
          <ul className="navbar-nav mx-auto" style={{ gap: "5rem" }}>
            <li className="nav-item">
              <NavLink
                to="/"
                className={({ isActive }) =>
                  "nav-link" + (isActive ? " active" : "")
                }
              >
                Inicio
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink
                to="/componentes"
                className={({ isActive }) =>
                  "nav-link" + (isActive ? " active" : "")
                }
              >
                Componentes
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink
                to="/tiendas"
                className={({ isActive }) =>
                  "nav-link" + (isActive ? " active" : "")
                }
              >
                Tiendas
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink
                to="/categorias"
                className={({ isActive }) =>
                  "nav-link" + (isActive ? " active" : "")
                }
              >
                Categorías
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink
                to="/builds"
                className={({ isActive }) =>
                  "nav-link" + (isActive ? " active" : "")
                }
              >
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
