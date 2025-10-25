export const getProfileNavigation = (isAuthenticated, tipoUsuario) => {
    if (!isAuthenticated) {
        return {
            path: "/login",
            iconClass: "bi-person-fill",
            text: "Iniciar Sesión",
        };
    }

    if (tipoUsuario === "EMPLEADO") {
        return {
            path: "/perfil/empleado",
            iconClass: "bi-shield-lock-fill",
            text: "Mi Tienda",
        };
    }

    if (tipoUsuario === "TIENDA") {
        return {
            path: "/perfil/tienda",
            iconClass: "bi-shop",
            text: "Administrador",
        };
    }

    return {
        path: "/perfil/usuario",
        iconClass: "bi-person-fill-check",
        text: "Mi Perfil",
    };
};
