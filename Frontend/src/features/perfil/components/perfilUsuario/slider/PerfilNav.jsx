import React from "react";
import { ListGroup, Button } from "react-bootstrap";
import { AiFillHome } from "react-icons/ai";
import { useNavigate } from "react-router-dom";
import BotonLogout from "../../auxiliar/BotonLogout";

const PerfilNav = ({ vista, setVista }) => {
    const navigate = useNavigate();

    const navItems = [
        { key: "informacion", icon: "bi-person-fill", text: "Información personal" },
        { key: "builds", icon: "bi-wrench", text: "Mis builds" },
        { key: "incidentes", icon: "bi-exclamation-triangle-fill", text: "Mis reportes" },
        { key: "solicitud", icon: "bi-file-earmark-text-fill", text: "Mis solicitudes" },
        { key: "seguridad", icon: "bi-shield-lock-fill", text: "Seguridad" },
    ];

    return (
        <ListGroup variant="flush">
            {/* Botón de inicio */}
            <ListGroup.Item className="p-0 mb-2">
                <Button
                    variant="outline-primary"
                    onClick={() => navigate("/")}
                    className="w-100 d-flex align-items-center justify-content-center"
                >
                    <AiFillHome className="me-2" />
                    Inicio
                </Button>
            </ListGroup.Item>

            {/* Items de navegación */}
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

            {/* Botón de logout */}
            <ListGroup.Item className="p-0 pt-2">
                <BotonLogout className="w-100" variant="danger" texto="Cerrar Sesión" />
            </ListGroup.Item>
        </ListGroup>
    );
};

export default PerfilNav;
