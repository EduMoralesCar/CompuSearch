import { useAuth } from "../context/useAuth";

export const useAuthStatus = () => {
    const { usuario, tipoUsuario, sessionReady } = useAuth();

    const isAuthenticated = !!usuario;

    return { isAuthenticated, tipoUsuario, sessionReady };
};
