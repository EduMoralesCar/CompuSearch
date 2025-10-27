import React from "react";

const FooterBottom = () => {
    const currentYear = new Date().getFullYear();

    return (
        <div className="bg-dark text-center py-3 mt-0">
            <small>© {currentYear} Grupo CompuSearch - Todos los derechos reservados</small>
        </div>
    );
};

export default FooterBottom;