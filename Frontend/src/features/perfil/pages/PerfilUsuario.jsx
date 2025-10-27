import React, { useState, useCallback } from "react";
import { Container, Row, Col, Spinner, Alert } from "react-bootstrap";
import PerfilSidebar from "../components/PerfilSidebar";
import PerfilContent from "../components/PerfilContent";
import { useAuth } from "../../../context/useAuth";

const PerfilUsuario = () => {
    const [activeView, setActiveView] = useState("informacion");
    const { usuario, sessionReady } = useAuth();
    
    // El estado de las estadísticas se inicializa en cero.
    const [stats, setStats] = useState({
        construcciones: 0,
        alertas: 0,
    });

    if (!sessionReady) {
        return (
            <Container className="text-center my-5">
                <Spinner animation="border" variant="primary" />
                <p className="mt-2">Cargando perfil...</p>
            </Container>
        );
    }

    if (!usuario) {
        return (
            <Container className="my-5">
                <Alert variant="warning">
                    Debes <Alert.Link href="/login">iniciar sesión</Alert.Link> para ver tu perfil.
                </Alert>
            </Container>
        );
    }

    return (
        <Container className="my-5">
            <h2 className="mb-4 fw-bold">Mi Perfil</h2>
            <Row>
                <Col md={4} lg={3} className="mb-4 mb-md-0">
                    <PerfilSidebar
                        usuario={usuario}
                        stats={stats}
                        activeView={activeView}
                        setActiveView={setActiveView}
                    />
                </Col>
                <Col md={8} lg={9}>
                    <PerfilContent 
                        activeView={activeView} 
                        setStats={setStats} // Pasamos la función para que los hijos actualicen las stats
                    />
                </Col>
            </Row>
        </Container>
    );
};

export default PerfilUsuario;