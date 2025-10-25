import React, { useEffect } from "react";
import { Link } from "react-router-dom";
import { MarginTop } from "../../../utils/MarginTop";

const Unauthorized = () => {
    useEffect(() => {
        MarginTop();
        window.addEventListener("resize", MarginTop);
        return () => window.removeEventListener("resize", MarginTop);
    }, []);

    return (
        <main className="d-flex flex-column justify-content-center align-items-center text-center">
            <i className="bi bi-lock-fill text-secondary" style={{ fontSize: "4rem" }}></i>
            <h1 className="mt-3 fw-bold text-dark">401 - Acceso no autorizado</h1>
            <p className="text-muted">No tienes permisos para acceder a esta sección.</p>
            <Link to="/" className="btn btn-outline-primary mt-3">
                Volver al inicio
            </Link>
        </main>
    );
};

export default Unauthorized;
