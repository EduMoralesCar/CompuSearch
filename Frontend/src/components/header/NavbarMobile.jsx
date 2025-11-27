import React, { useState } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { useAuthStatus } from "../../hooks/useAuthStatus";
import { getProfileNavigation } from "../../utils/profileNavigation";
import ProfileSelectorModal from "../auth/ProfileSelectorModal";
import NavLinksList from "./NavLinksList";

const NavbarMobile = () => {
    const navigate = useNavigate();
    const { isAuthenticated, tipoUsuario } = useAuthStatus();
    const { path, iconClass, text } = getProfileNavigation(isAuthenticated, tipoUsuario);

    const [isModalOpen, setIsModalOpen] = useState(false);

    const handleProfileClick = () => {
        if (path === "/modal-selector") {
            setIsModalOpen(true);
        } else {
            navigate(path);
        }
    };

    return (
        <>
            <ul className="navbar-nav d-block d-lg-none mt-3">
                <NavLinksList itemClassName="nav-item" linkClassName="nav-link" />

                <li className="nav-item">
                    <button
                        onClick={handleProfileClick}
                        className="nav-link btn btn-link text-start w-100 nav-close-trigger"
                    >
                        <i className={`bi ${iconClass} fs-4 me-2 align-middle`}></i>{text}
                    </button>
                </li>

                <li className="nav-item">
                    <NavLink to="/tiendas" className="nav-link">
                        <i className="bi bi-shop-window fs-4 me-2 align-middle"></i>Tiendas
                    </NavLink>
                </li>
            </ul>

            {isModalOpen && (
                <ProfileSelectorModal
                    isOpen={isModalOpen}
                    onClose={() => setIsModalOpen(false)}
                    navigate={navigate}
                />
            )}
        </>
    );
};

export default NavbarMobile;