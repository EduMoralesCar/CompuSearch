import React, { useEffect, useState } from "react";
import { ListGroup, Spinner, Alert, Card, Button, Modal, Form } from "react-bootstrap";
import { useAuthStatus } from "../../../../../hooks/useAuthStatus";
import { useIncidentes } from "../../../hooks/useIncidentes";
import axios from "axios";

const MisIncidentes = () => {
    const { idUsuario } = useAuthStatus();
    const { respuesta, loading, error, obtenerIncidentes, totalPages } = useIncidentes();

    const [page, setPage] = useState(0);
    const [showModal, setShowModal] = useState(false);
    const [titulo, setTitulo] = useState("");
    const [descripcion, setDescripcion] = useState("");
    const [submitLoading, setSubmitLoading] = useState(false);

    const [submitError, setSubmitError] = useState(null);

    const [tituloError, setTituloError] = useState("");
    const [descripcionError, setDescripcionError] = useState("");


    useEffect(() => {
        if (idUsuario) {
            obtenerIncidentes(idUsuario, page, 5);
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [idUsuario, page]);

    const MIN_TITULO = 5;
    const MIN_DESCRIPCION = 20;

    const handleCrearIncidente = async (e) => {
        e.preventDefault();
        setSubmitError(null);

        let valido = true;
        if (titulo.trim().length < MIN_TITULO) {
            setTituloError(`El título debe tener al menos ${MIN_TITULO} caracteres.`);
            valido = false;
        } else {
            setTituloError("");
        }

        if (descripcion.trim().length < MIN_DESCRIPCION) {
            setDescripcionError(`La descripción debe tener al menos ${MIN_DESCRIPCION} caracteres.`);
            valido = false;
        } else {
            setDescripcionError("");
        }

        if (!valido) return;

        setSubmitLoading(true);

        try {
            await axios.post(
                "http://localhost:8080/incidentes",
                { titulo, descripcion, idUsuario },
                { withCredentials: true }
            );
            setTitulo("");
            setDescripcion("");
            setShowModal(false);
            obtenerIncidentes(idUsuario, page, 5);
        } catch (err) {
            setSubmitError(err.response?.data?.message || "Error al crear el incidente");
        } finally {
            setSubmitLoading(false);
        }
    };


    const handlePrevPage = () => setPage((p) => Math.max(p - 1, 0));
    const handleNextPage = () => setPage((p) => Math.min(p + 1, totalPages - 1));

    return (
        <>
            <div className="d-flex justify-content-between align-items-center mb-3">
                <h3 className="mb-0">Mis Incidentes</h3>
                <Button onClick={() => setShowModal(true)}>Crear Incidente</Button>
            </div>

            {error && (
                <div style={{ color: "red", marginBottom: "1rem" }}>
                    {error}
                </div>
            )}


            {loading && (
                <div className="text-center py-5">
                    <Spinner animation="border" variant="primary" />
                </div>
            )}

            {error && <Alert variant="danger">{error}</Alert>}

            {!loading && !error && (!respuesta || respuesta.length === 0) && (
                <Alert variant="info">
                    Aún no tienes incidentes registrados.
                </Alert>
            )}

            {!loading && !error && respuesta && respuesta.length > 0 && (
                <>
                    <ListGroup>
                        {respuesta.map((incidente) => (
                            <ListGroup.Item key={incidente.idIncidente} className="mb-2">
                                <Card>
                                    <Card.Body>
                                        <Card.Title>{incidente.titulo}</Card.Title>
                                        <Card.Text>{incidente.descripcion}</Card.Text>
                                        <small className="text-muted">
                                            Creado el: {new Date(incidente.fechaCreacion).toLocaleString()}
                                        </small>
                                    </Card.Body>
                                </Card>
                            </ListGroup.Item>
                        ))}
                    </ListGroup>

                    {/* Paginación */}
                    <div className="d-flex justify-content-between mt-3">
                        <Button onClick={handlePrevPage} disabled={page === 0}>Anterior</Button>
                        <span>Página {page + 1} de {totalPages}</span>
                        <Button onClick={handleNextPage} disabled={page + 1 >= totalPages}>Siguiente</Button>
                    </div>
                </>
            )}

            {/* Modal para crear incidente */}
            <Modal show={showModal} onHide={() => setShowModal(false)}>
                <Form onSubmit={handleCrearIncidente}>
                    <Modal.Header closeButton>
                        <Modal.Title>Nuevo Incidente</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {submitError && (
                            <Alert variant="danger" onClose={() => setSubmitError(null)} dismissible>
                                {submitError}
                            </Alert>
                        )}
                        <Form.Group className="mb-3" controlId="titulo">
                            <Form.Label>Título</Form.Label>
                            <Form.Control
                                type="text"
                                value={titulo}
                                onChange={(e) => setTitulo(e.target.value)}
                                required
                                isInvalid={!!tituloError}
                            />
                            {tituloError && <Form.Text className="text-danger">{tituloError}</Form.Text>}
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="descripcion">
                            <Form.Label>Descripción</Form.Label>
                            <Form.Control
                                as="textarea"
                                rows={4}
                                value={descripcion}
                                onChange={(e) => setDescripcion(e.target.value)}
                                required
                                isInvalid={!!descripcionError}
                            />
                            {descripcionError && <Form.Text className="text-danger">{descripcionError}</Form.Text>}
                        </Form.Group>

                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowModal(false)} disabled={submitLoading}>
                            Cancelar
                        </Button>
                        <Button type="submit" variant="primary" disabled={submitLoading}>
                            {submitLoading ? "Creando..." : "Crear"}
                        </Button>
                    </Modal.Footer>
                </Form>
            </Modal>
        </>
    );
};

export default MisIncidentes;
