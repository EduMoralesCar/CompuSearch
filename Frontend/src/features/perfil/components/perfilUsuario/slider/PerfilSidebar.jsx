import React from "react";
import { Card } from "react-bootstrap";
import PerfilHeader from "./PerfilHedaer";
import PerfilNav from "./PerfilNav";
import PerfilStats from "./PerfilStats"

const PerfilSidebar = ({ usuario, vista, setVista }) => {
    return (
        <Card className="shadow-sm">
            <Card.Body>
                <PerfilHeader
                    username={usuario.username}
                    email={usuario.email}
                />
                <PerfilStats 
                    builds={usuario.builds}
                    incidentes={usuario.incidentes}
                    solicitudes={usuario.solicitudes}
                />
                <PerfilNav vista={vista} setVista={setVista} />
            </Card.Body>
        </Card>
    );
};

export default PerfilSidebar;