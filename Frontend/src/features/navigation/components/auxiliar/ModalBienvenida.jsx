import { Modal, Button, Row, Col } from "react-bootstrap";
import logo from "../../../../assets/logo/CompuSearch_Logo.gif";

const ModalBienvenida = ({ show, handleClose }) => {
    return (
        <Modal show={show} onHide={handleClose} centered size="lg">
            <Modal.Header closeButton className="bg-primary text-white">
                <Modal.Title className="w-100 text-center">
                    <div className="d-flex align-items-center justify-content-center gap-3">
                        <img src={logo} alt="CompuSearch Logo" style={{ width: "120px", height: "50px" }} />
                        <span>Bienvenido a CompuSearch</span>
                    </div>
                </Modal.Title>
            </Modal.Header>
            <Modal.Body className="p-4">
                <Row className="mb-3">
                    <Col className="text-center">
                        <h5 className="text-primary fw-bold">Sobre el Proyecto</h5>
                        <p className="text-muted">
                            CompuSearch es una plataforma innovadora diseñada para facilitar la búsqueda y comparación
                            de componentes de computadoras, conectando usuarios con las mejores tiendas del mercado.
                        </p>
                    </Col>
                </Row>

                <hr />

                <Row className="mb-3">
                    <Col md={6}>
                        <h6 className="text-primary fw-bold">
                            <i className="bi bi-people-fill me-2"></i>Equipo de Desarrollo
                        </h6>
                        <ul className="list-unstyled ms-3">
                            <li className="mb-1">
                                <i className="bi bi-star-fill text-warning me-2"></i>
                                <strong>Líder:</strong> Macedo Salas, Edhu Anthony
                            </li>
                            <li className="mb-1">
                                <i className="bi bi-person-fill text-primary me-2"></i>
                                Beraun García, Leonardo Ángel
                            </li>
                            <li className="mb-1">
                                <i className="bi bi-person-fill text-primary me-2"></i>
                                Matos Chuquino, Jesús Presencio
                            </li>
                            <li className="mb-1">
                                <i className="bi bi-person-fill text-primary me-2"></i>
                                Morales Carlos, Edu
                            </li>
                            <li className="mb-1">
                                <i className="bi bi-person-fill text-primary me-2"></i>
                                Olivares Chávez, Jeremi Alexander
                            </li>
                        </ul>
                    </Col>
                    <Col md={6}>
                        <h6 className="text-primary fw-bold">
                            <i className="bi bi-gear-fill me-2"></i>Tecnologías
                        </h6>
                        <ul className="list-unstyled ms-3">
                            <li className="mb-1">
                                <i className="bi bi-check-circle-fill text-success me-2"></i>
                                React + Vite
                            </li>
                            <li className="mb-1">
                                <i className="bi bi-check-circle-fill text-success me-2"></i>
                                Spring Boot
                            </li>
                            <li className="mb-1">
                                <i className="bi bi-check-circle-fill text-success me-2"></i>
                                MySQL
                            </li>
                            <li className="mb-1">
                                <i className="bi bi-check-circle-fill text-success me-2"></i>
                                Bootstrap
                            </li>
                        </ul>
                    </Col>
                </Row>

                <div className="text-center mt-3">
                    <small className="text-muted">
                        <i className="bi bi-calendar3 me-2"></i>
                        Proyecto Académico - 2025
                    </small>
                </div>
            </Modal.Body>
            <Modal.Footer className="justify-content-center">
                <Button variant="primary" onClick={handleClose} className="px-4">
                    <i className="bi bi-box-arrow-in-right me-2"></i>
                    Comenzar
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ModalBienvenida;
