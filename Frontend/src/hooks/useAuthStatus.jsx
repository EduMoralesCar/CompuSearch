import { useAuth } from "../context/useAuth";

export const useAuthStatus = () => {
    const { usuario, tipoUsuario, idUsuario, sessionReady } = useAuth();

    const isAuthenticated = !!usuario;

    return { isAuthenticated, tipoUsuario, idUsuario, sessionReady };
};
