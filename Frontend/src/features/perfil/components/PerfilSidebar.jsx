import React from "react";
import { Card } from "react-bootstrap";
import PerfilHeader from "./PerfilHeader";
import PerfilStats from "./PerfilStats";
import PerfilNav from "./PerfilNav";

const PerfilSidebar = ({ usuario, stats, activeView, setActiveView }) => {
    return (
        <Card className="shadow-sm">
            <Card.Body>
                <PerfilHeader
                    nombre={usuario.identificador}
                    email={usuario.email}
                />
                <hr />
                <PerfilStats stats={stats} />
                <hr />
                <PerfilNav activeView={activeView} setActiveView={setActiveView} />
            </Card.Body>
        </Card>
    );
};

export default PerfilSidebar;