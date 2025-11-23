import React, { useEffect } from "react";
import NavbarMobile from "./NavbarMobile";
import NavbarDesktop from "./NavbarDesktop";
import SearchBar from "./SearchBar";
import { NavLink } from "react-router-dom";

import Logo from "../../assets/logo/CompuSearch_Logo.gif";

import bootstrap from "bootstrap/dist/js/bootstrap.bundle.min";

const Header = () => {
    useEffect(() => {
        const navbarCollapse = document.getElementById("navbarCompuSearch");

        if (!navbarCollapse) return;

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

        const handleCollapseShow = () => {
            document.body.classList.add("no-pointer-events");
            navbarCollapse.classList.add("allow-pointer-events");
        };

        const handleCollapseHide = () => {
            document.body.classList.remove("no-pointer-events");
            navbarCollapse.classList.remove("allow-pointer-events");
        };

        navbarCollapse.addEventListener("shown.bs.collapse", handleCollapseShow);
        navbarCollapse.addEventListener("hidden.bs.collapse", handleCollapseHide);

        const navTriggers = navbarCollapse.querySelectorAll("a.nav-link, .nav-close-trigger");
        navTriggers.forEach((el) => el.addEventListener("click", handleNavLinkClick));

        document.addEventListener("click", handleClickOutside);

        return () => {
            document.removeEventListener("click", handleClickOutside);
            navTriggers.forEach((el) => el.removeEventListener("click", handleNavLinkClick));
            navbarCollapse.removeEventListener("shown.bs.collapse", handleCollapseShow);
            navbarCollapse.removeEventListener("hidden.bs.collapse", handleCollapseHide);
            document.body.classList.remove("no-pointer-events");
        };
    }, []);

    return (
        <>
        <style>{`
        body.no-pointer-events {
            pointer-events: none !important;
        }
        .allow-pointer-events {
            pointer-events: auto !important;
        }
        `}</style>

            <header className="fixed-top">
                <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
                    <div className="container-fluid">
                        {/* Logo */}
                        <NavLink className="navbar-brand d-flex align-items-center" to="/">
                            <img src={Logo} alt="Logo" height="60" className="me-2" />
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
        </>
    );
};

export default Header;
