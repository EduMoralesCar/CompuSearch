import { Card } from "react-bootstrap";
import InformacionPersonal from "./InformacionPersonal";
import Seguridad from "./Seguridad";
import MisBuilds from "./MisBuilds";
import MisIncidentes from "./MisIncidentes";
import MisSolicitudes from "./MisSolicitudes"

const PerfilContent = ({ vista, username, email, fechaRegistro }) => {
    const renderContent = () => {
        switch (vista) {
            case "informacion":
                return <InformacionPersonal
                    username={username}
                    email={email}
                    fechaRegistro={fechaRegistro}
                />;
            case "builds":
                return <MisBuilds />;
            case "incidentes":
                return <MisIncidentes />;
            case "solicitud":
                return <MisSolicitudes />;
            case "seguridad":
                return <Seguridad />;
            default:
                return <InformacionPersonal
                    username={username}
                    email={email}
                    fechaRegistro={fechaRegistro}
                />;
        }
    };

    return (
        <Card className="shadow-sm">
            <Card.Body className="p-4">
                {renderContent()}
            </Card.Body>
        </Card>
    );
};

export default PerfilContent;