import { useAuth } from "../context/useAuth";

export const useAuthStatus = () => {
    const { usuario, tipoUsuario, idUsuario, rol, suscripcionActiva, sessionReady } = useAuth();

    const isAuthenticated = !!usuario;

    return { isAuthenticated, tipoUsuario, idUsuario, rol, suscripcionActiva, sessionReady };
};
