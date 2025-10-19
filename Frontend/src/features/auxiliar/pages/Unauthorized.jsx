import React, { useEffect } from "react";
import { Link } from "react-router-dom";
import { applyHeaderOffset } from "../../../utils/layout";

const Unauthorized = () => {
    useEffect(() => {
        applyHeaderOffset();
        window.addEventListener("resize", applyHeaderOffset);
        return () => window.removeEventListener("resize", applyHeaderOffset);
    }, []);

    return (
        <main className="d-flex flex-column justify-content-center align-items-center text-center">
            <i className="bi bi-lock-fill text-secondary" style={{ fontSize: "4rem" }}></i>
            <h1 className="mt-3 fw-bold text-dark">401 - Acceso no autorizado</h1>
            <p className="text-muted">No tienes permisos para acceder a esta sección.</p>
            <Link to="/login" className="btn btn-outline-primary mt-3">
                Iniciar sesión
            </Link>
        </main>
    );
};

export default Unauthorized;
