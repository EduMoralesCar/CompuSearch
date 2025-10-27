import React from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { useAuthStatus } from "../../hooks/useAuthStatus";
import { getProfileNavigation } from "../../utils/profileNavigation";

const NavbarMobile = () => {
    const navigate = useNavigate();
    const { isAuthenticated, tipoUsuario } = useAuthStatus();
    const { path, iconClass, text } = getProfileNavigation(isAuthenticated, tipoUsuario);

    return (
        <ul className="navbar-nav d-block d-lg-none mt-3">
            <li className="nav-item">
                <NavLink to="/" className="nav-link">Inicio</NavLink>
            </li>
            <li className="nav-item">
                <NavLink to="/componentes" className="nav-link">Componentes</NavLink>
            </li>
            <li className="nav-item">
                <NavLink to="/categorias" className="nav-link">Categorías</NavLink>
            </li>
            <li className="nav-item">
                <NavLink to="/builds" className="nav-link">Armado</NavLink>
            </li>
            <li className="nav-item">
                <button onClick={() => navigate(path)} className="nav-link btn btn-link text-start w-100 nav-close-trigger">
                    <i className={`bi ${iconClass} fs-4 me-2 align-middle`}></i>{text}
                </button>
            </li>
            <li className="nav-item">
                <NavLink to="/tiendas" className="nav-link">
                    <i className="bi bi-shop-window fs-4 me-2 align-middle"></i>Tiendas
                </NavLink>
            </li>
        </ul>
    );
};

export default NavbarMobile;
