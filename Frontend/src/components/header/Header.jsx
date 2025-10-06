import React, { useEffect } from "react";
import NavbarMobile from "./NavbarMobile";
import NavbarDesktop from "./NavbarDesktop";
import SearchBar from "./SearchBar";
import { NavLink } from "react-router-dom";
import Logo from "../../assets/logo.webp"
import bootstrap from 'bootstrap/dist/js/bootstrap.bundle.min';

const Header = () => {
    useEffect(() => {
        const navbarCollapse = document.getElementById("navbarCompuSearch");

        const handleClickOutside = (event) => {
            const isOpen = navbarCollapse?.classList.contains("show");
            if (isOpen && !navbarCollapse.contains(event.target)) {
                const bsCollapse = bootstrap.Collapse.getInstance(navbarCollapse);
                bsCollapse?.hide();
            }
        };

        const handleNavLinkClick = () => {
            const isOpen = navbarCollapse?.classList.contains("show");
            if (isOpen) {
                const bsCollapse = bootstrap.Collapse.getInstance(navbarCollapse);
                bsCollapse?.hide();
            }
        };

        const navTriggers = navbarCollapse?.querySelectorAll("a.nav-link, .nav-close-trigger");
        navTriggers?.forEach((el) => {
            el.addEventListener("click", handleNavLinkClick);
        });


        document.addEventListener("click", handleClickOutside);

        return () => {
            document.removeEventListener("click", handleClickOutside);
            navTriggers?.forEach((el) => {
                el.removeEventListener("click", handleNavLinkClick);
            });

        };
    }, []);


    return (
        <header className="fixed-top">
            <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
                <div className="container-fluid">
                    {/* Logo */}
                    <NavLink className="navbar-brand d-flex align-items-center" to="/">
                        <img src={Logo} alt="Logo" height="40" className="me-2" />
                    </NavLink>

                    {/* Botón toggle */}
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

                    {/* Contenido colapsable */}
                    <div className="collapse navbar-collapse" id="navbarCompuSearch">
                        <NavbarMobile />
                        <SearchBar />
                        <NavbarDesktop />
                    </div>
                </div>
            </nav>

            {/* Barra secundaria solo en escritorio */}
            <nav className="navbar navbar-expand navbar-dark bg-dark d-none d-lg-block">
                <div className="container-fluid">
                    <NavbarDesktop secondary />
                </div>
            </nav>
        </header>
    );
};

export default Header;
