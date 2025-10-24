import React, { useEffect } from "react";
import { Link } from "react-router-dom";
import { MarginTop } from "../../../utils/MarginTop";

const NotFound = () => {
    useEffect(() => {
        MarginTop();
        window.addEventListener("resize", MarginTop);
        return () => window.removeEventListener("resize", MarginTop);
    }, []);

    return (
        <main className="d-flex flex-column justify-content-center align-items-center text-center">
            <i className="bi bi-exclamation-triangle text-warning" style={{ fontSize: "4rem" }}></i>
            <h1 className="mt-3 fw-bold text-danger">404 - Página no encontrada</h1>
            <p className="text-muted">La página que estás buscando no existe o fue movida.</p>
            <Link to="/" className="btn btn-primary mt-3">
                Volver al inicio
            </Link>
        </main>
    );
};

export default NotFound;
