import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

const PrivateRoute = ({ allowedRoles }) => {
    const token = localStorage.getItem("token");
    const userString = localStorage.getItem("user");

    // 1. Verificar autenticación
    if (!token || !userString) {
        return <Navigate to="/login" replace />;
    }

    try {
        const user = JSON.parse(userString);
        const userRole = user.rol;

        // 2. Verificar autorización por rol
        if (allowedRoles && !allowedRoles.includes(userRole)) {
            return <Navigate to="/" replace />;
        }

        // 3. Logueado y con el rol permitido
        return <Outlet />;

    } catch (e) {
        // Error al parsear usuario: limpiar y redirigir al login
        console.error("Error en PrivateRoute al parsear usuario:", e);
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        return <Navigate to="/login" replace />;
    }
};

export default PrivateRoute;