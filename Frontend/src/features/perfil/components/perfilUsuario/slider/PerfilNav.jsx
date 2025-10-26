import React from "react";
import { ListGroup } from "react-bootstrap";
import BotonLogout from "../../auxiliar/BotonLogout";

const PerfilNav = ({ vista, setVista }) => {
    const navItems = [
        { key: "informacion", icon: "bi-person-fill", text: "Información personal" },
        { key: "builds", icon: "bi-wrench", text: "Mis builds" },
        { key: "incidentes", icon: "bi-exclamation-triangle-fill", text: "Mis reportes" },
        { key: "solicitud", icon: "bi-file-earmark-text-fill", text: "Mis solicitudes" },
        { key: "seguridad", icon: "bi-shield-lock-fill", text: "Seguridad" },
    ];

    return (
        <ListGroup variant="flush">
            {navItems.map((item) => (
                <ListGroup.Item
                    key={item.key}
                    action
                    active={vista === item.key}
                    onClick={() => setVista(item.key)}
                    className="d-flex align-items-center"
                >
                    <i className={`bi ${item.icon} me-3`}></i> {item.text}
                </ListGroup.Item>
            ))}
            <ListGroup.Item className="p-0 pt-2">
                <BotonLogout className="w-100" variant="danger" texto="Cerrar Sesión" />
            </ListGroup.Item>
        </ListGroup>
    );
};

export default PerfilNav;