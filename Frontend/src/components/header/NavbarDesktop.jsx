import React from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { useAuthStatus } from "../../hooks/useAuthStatus";
import { getProfileNavigation } from "../../utils/profileNavigation";

const NavbarDesktop = ({ secondary = false }) => {
    const navigate = useNavigate();
    const { isAuthenticated, tipoUsuario } = useAuthStatus();
    const { path, iconClass, text } = getProfileNavigation(isAuthenticated, tipoUsuario);

    if (secondary) {
        return (
            <ul className="navbar-nav mx-auto" style={{ gap: "5rem" }}>
                <NavLink to="/" className="nav-link">Inicio</NavLink>
                <NavLink to="/componentes" className="nav-link">Componentes</NavLink>
                <NavLink to="/categorias" className="nav-link">Categorías</NavLink>
                <NavLink to="/builds" className="nav-link">Armado</NavLink>
            </ul>
        );
    }

    return (
        <div className="d-none d-lg-flex align-items-center order-lg-3 ms-4">
            <button onClick={() => navigate(path)} className="btn btn-link text-white p-0" title={text}>
                <i className={`bi ${iconClass} fs-4 align-middle`}></i>
            </button>
            <NavLink to="/tiendas" className="ms-3 text-white">
                <i className="bi bi-shop-window fs-4 align-middle"></i>
            </NavLink>
        </div>
    );
};

export default NavbarDesktop;
