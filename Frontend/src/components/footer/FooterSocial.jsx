import React from "react";

const FooterSocial = () => (
    <>
        <h6 className="fw-bold">LEGAL</h6>
        <ul className="list-unstyled">
            <li>
                <a href="#" className="text-white text-decoration-none">Términos de uso</a>
            </li>
            <li>
                <a href="#" className="text-white text-decoration-none">Políticas de Privacidad</a>
            </li>
        </ul>

        <h6 className="fw-bold mt-4">SÍGUENOS</h6>
        <div className="d-flex justify-content-center gap-3">
            <a href="#" className="text-white fs-5" aria-label="Twitter">
                <i className="bi bi-twitter-x"></i>
            </a>
            <a href="#" className="text-white fs-5" aria-label="Instagram">
                <i className="bi bi-instagram"></i>
            </a>
            <a href="#" className="text-white fs-5" aria-label="YouTube">
                <i className="bi bi-youtube"></i>
            </a>
        </div>
    </>
);

export default FooterSocial;
