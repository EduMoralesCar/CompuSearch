import { Navigate } from "react-router-dom";
import { useAuth } from "../../context/useAuth";

const RequireAnonimo = ({ children }) => {
    const { tipoUsuario } = useAuth();

    if (tipoUsuario) return <Navigate to="/" replace />;

    return children;
};

export default RequireAnonimo;
