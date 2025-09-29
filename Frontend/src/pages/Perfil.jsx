import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const Perfil = () => {
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) {
            navigate("/login"); // Si no hay token -> redirige a login
        }
    }, [navigate]);

    const handleLogout = () => {
        localStorage.removeItem("token"); // Elimina el token
        navigate("/login"); // Redirige al login
    };

    return (
        <div className="container my-4">
            <h2>Perfil de Usuario</h2>
            <button className="btn btn-danger mt-3" onClick={handleLogout}>
                Cerrar sesión
            </button>
        </div>
    );
};

export default Perfil;