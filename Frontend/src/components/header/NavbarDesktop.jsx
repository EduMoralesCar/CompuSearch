import { useState } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { useAuthStatus } from "../../hooks/useAuthStatus";
import ProfileSelectorModal from "../auth/ProfileSelectorModal";
import { getProfileNavigation } from "../../utils/profileNavigation";
import NavLinksList from "./NavLinksList";

const NavbarDesktop = ({ secondary = false }) => {
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

    if (secondary) {
        return (
            <ul className="navbar-nav mx-auto" style={{ gap: "5rem" }}>
                <NavLinksList itemClassName="" linkClassName="nav-link" />
            </ul>
        );
    }

    return (
        <div className="d-none d-lg-flex align-items-center order-lg-3 ms-4">
            <button onClick={handleProfileClick} className="btn btn-link text-white p-0" title={text}>
                <i className={`bi ${iconClass} fs-4 align-middle`}></i>
            </button>
            <NavLink to="/tiendas" className="ms-3 text-white">
                <i className="bi bi-shop-window fs-4 align-middle"></i>
            </NavLink>

            {isModalOpen && (
                <ProfileSelectorModal
                    isOpen={isModalOpen}
                    onClose={() => setIsModalOpen(false)}
                    navigate={navigate}
                />
            )}
        </div>
    );
};

export default NavbarDesktop;
