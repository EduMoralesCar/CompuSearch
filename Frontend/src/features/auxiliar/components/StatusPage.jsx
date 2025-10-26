import React, { useEffect } from "react";
import { Link } from "react-router-dom";
import { MarginTop } from "../../../utils/MarginTop";

const StatusPage = ({ code, title, message, iconClass, buttonVariant = "primary", buttonText = "Volver al inicio", buttonLink = "/" }) => {
    useEffect(() => {
        MarginTop();
        window.addEventListener("resize", MarginTop);
        return () => window.removeEventListener("resize", MarginTop);
    }, []);

    return (
        <main className="d-flex flex-column justify-content-center align-items-center text-center">
            <i className={`${iconClass}`} style={{ fontSize: "4rem" }}></i>
            <h1 className={`mt-3 fw-bold ${code === 404 ? "text-danger" : "text-dark"}`}>
                {code} - {title}
            </h1>
            <p className="text-muted">{message}</p>
            <Link to={buttonLink} className={`btn btn-${buttonVariant} mt-3`}>
                {buttonText}
            </Link>
        </main>
    );
};

export default StatusPage;
