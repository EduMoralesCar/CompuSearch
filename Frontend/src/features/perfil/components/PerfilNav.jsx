import React from "react";
import { ListGroup } from "react-bootstrap";
import BotonLogout from "./BotonLogout";

const PerfilNav = ({ activeView, setActiveView }) => {
    const navItems = [
        { key: "informacion", icon: "bi-person-fill", text: "Información personal" },
        { key: "construcciones", icon: "bi-wrench", text: "Mis construcciones" },
        { key: "alertas", icon: "bi-bell-fill", text: "Alertas de precio" },
        { key: "seguridad", icon: "bi-shield-lock-fill", text: "Seguridad" },
    ];

    return (
        <ListGroup variant="flush">
            {navItems.map((item) => (
                <ListGroup.Item
                    key={item.key}
                    action
                    active={activeView === item.key}
                    onClick={() => setActiveView(item.key)}
                    className="d-flex align-items-center"
                >
                    <i className={`bi ${item.icon} me-3`}></i> {item.text}
                </ListGroup.Item>
            ))}
             <ListGroup.Item className="p-0 pt-2">
               <BotonLogout className="w-100" variant="danger" texto="Cerrar Sesión"/>
            </ListGroup.Item>
        </ListGroup>
    );
};

export default PerfilNav;