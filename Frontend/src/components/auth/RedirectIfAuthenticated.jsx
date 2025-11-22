import { Navigate } from "react-router-dom";
import { useAuth } from "../../context/useAuth";

const RedirectIfAuthenticated = ({ children }) => {
    const { tipoUsuario, sessionReady } = useAuth();

    if (!sessionReady) return null;

    if (tipoUsuario) return <Navigate to="/" replace />;

    return children;
};

export default RedirectIfAuthenticated;
