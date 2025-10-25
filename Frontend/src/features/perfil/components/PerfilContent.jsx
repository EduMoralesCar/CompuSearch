import React from "react";
import { Card } from "react-bootstrap";
import InformacionPersonal from "./InformacionPersonal";
import MisConstrucciones from "./MisConstrucciones";
import AlertasDePrecio from "./AlertasDePrecio";
import Seguridad from "./Seguridad"; 

const PerfilContent = ({ activeView, setStats }) => {
    const renderContent = () => {
        switch (activeView) {
            case "informacion":
                return <InformacionPersonal />;
            case "construcciones":
                return <MisConstrucciones setStats={setStats} />;
            case "alertas":
                return <AlertasDePrecio />;
            case "seguridad":
                return <Seguridad />;
            default:
                return <InformacionPersonal />;
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