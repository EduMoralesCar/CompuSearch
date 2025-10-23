import { useAuth } from "../../../context/useAuth";
import { useNavigate } from "react-router-dom";
import { Button } from "react-bootstrap";

const BotonLogout = ({ className = "", variant = "outline-danger", texto = "Cerrar sesión" }) => {
    const { logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = async () => {
        await logout();
        navigate("/login");
    };

    return (
        <Button variant={variant} className={className} onClick={handleLogout}>
            {texto}
        </Button>
    );
};

export default BotonLogout;
