import React, { useState } from "react";
import { Container, Row, Col, Spinner } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { useAuthStatus } from "../../../hooks/useAuthStatus";
import { useDatosUsuario } from "../hooks/useDatosUsuario";
import PerfilSidebar from "../components/perfilUsuario/slider/PerfilSidebar";
import PerfilContent from "../components/perfilUsuario/content/PerfilContent";
import Header from "../../../components/header/Header";
import Footer from "../../../components/footer/Footer";

const PerfilUsuario = () => {
    const [vista, setVista] = useState("informacion");
    const { idUsuario } = useAuthStatus();
    const { usuario, loading, error } = useDatosUsuario(idUsuario);
    const navigate = useNavigate();

    if (loading)
        return (
            <>
                <Header />
                <div
                    className="d-flex flex-column justify-content-center align-items-center"
                    style={{ minHeight: "80vh" }}
                >
                    <Spinner animation="border" variant="primary" role="status" />
                    <p className="text-secondary fs-5 mt-3">Cargando perfil...</p>
                </div>
                <Footer />
            </>
        );

    if (error)
        return (
            <>
                <Header />
                <div
                    className="d-flex flex-column justify-content-center align-items-center"
                    style={{ minHeight: "80vh" }}
                >
                    <p className="text-danger fs-5 mb-2">{error}</p>
                    <p className="text-muted">Por favor, intenta nuevamente más tarde.</p>
                </div>
                <Footer />
            </>
        );

    return (
        <>
            <br /> <br />
            <div style={{
                display: "flex",
                flexDirection: "column",
                minHeight: "100vh",
                marginTop: "80px" // Espacio para el header fijo
            }}>
                <Header />
                <div style={{
                    flex: 1,
                    display: "flex",
                    alignItems: "center",
                    paddingTop: "2rem",
                    paddingBottom: "2rem"
                }}>
                    <Container className="position-relative" style={{ width: "100%" }}>
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
                </div>
                <Footer />
            </div>
        </>
    );
};

export default PerfilUsuario;
