import React, { useState } from "react";
import { Container, Row, Col, Spinner, Button } from "react-bootstrap";
import { useAuthStatus } from "../../../hooks/useAuthStatus";
import { useDatosUsuario } from "../hooks/useDatosUsuario";
import PerfilSidebar from "../components/perfilUsuario/slider/PerfilSidebar";
import PerfilContent from "../components/perfilUsuario/content/PerfilContent";

const PerfilUsuario = () => {
    const [vista, setVista] = useState("informacion");
    const { idUsuario } = useAuthStatus();
    const { usuario, loading, error } = useDatosUsuario(idUsuario);

    if (loading)
        return (
            <div
                className="d-flex flex-column justify-content-center align-items-center"
                style={{ height: "100vh" }}
            >
                <Spinner animation="border" variant="primary" role="status" />
                <p className="text-secondary fs-5 mt-3">Cargando perfil...</p>
            </div>
        );

    if (error)
        return (
            <div
                className="d-flex flex-column justify-content-center align-items-center"
                style={{ height: "100vh" }}
            >
                <p className="text-danger fs-5 mb-2">{error}</p>
                <p className="text-muted">Por favor, intenta nuevamente más tarde.</p>
            </div>
        );

    return (
        <Container className="my-5 position-relative" style={{ minHeight: "80vh" }}>
            

            <h2 className="mb-4 fw-bold text-center">Mi Perfil</h2>
            <Row>
                <Col md={4} lg={3} className="mb-4 mb-md-0">
                    <PerfilSidebar
                        usuario={usuario}
                        vista={vista}
                        setVista={setVista}
                    />
                </Col>
                <Col md={8} lg={9}>
                    <PerfilContent
                        vista={vista}
                        username={usuario.username}
                        email={usuario.email}
                        fechaRegistro={usuario.fechaRegistro}
                    />
                </Col>
            </Row>
        </Container>
    );
};

export default PerfilUsuario;
