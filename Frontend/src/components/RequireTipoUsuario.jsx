import { Navigate } from "react-router-dom";
import { useAuth } from "../context/useAuth";

const RequireTipoUsuario = ({ tiposPermitidos, children }) => {
    const { tipoUsuario, sessionReady } = useAuth();

    if (!sessionReady) return null;

    if (!tipoUsuario) return <Navigate to="/login" replace />;

    if (!tiposPermitidos.includes(tipoUsuario)) return <Navigate to="/unauthorized" replace />;

    return children;
};

export default RequireTipoUsuario;
