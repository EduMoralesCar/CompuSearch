import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

/**
 * Componente de Ruta Privada.
 * Redirige a /login si no hay token.
 * Redirige a / (o a otra ruta) si el usuario no tiene el rol requerido.
 * * @param {Array<string>} allowedRoles - Array de roles permitidos (ej: ['EMPLEADO', 'TIENDA']).
 */
const PrivateRoute = ({ allowedRoles }) => {
    const token = localStorage.getItem("token");
    const userString = localStorage.getItem("user");

    // 1. Verificar autenticación
    if (!token || !userString) {
        // No logueado: redirige a la página de login
        return <Navigate to="/login" replace />;
    }

    try {
        const user = JSON.parse(userString);
        const userRole = user.rol;

        // 2. Verificar autorización por rol
        if (allowedRoles && !allowedRoles.includes(userRole)) {
            // Logueado, pero sin el rol permitido: redirige a la página principal
            // Esto evita que un USUARIO acceda a /admin o /admin-tienda.
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