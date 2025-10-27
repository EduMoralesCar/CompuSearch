import React, { useEffect, useState } from "react";
import { Modal, Button, ListGroup, Spinner, Alert } from "react-bootstrap";

const BuildsModal = ({
    show,
    onClose,
    onSeleccionarBuild,
    idUsuario,
    obtenerBuildsPorUsuario
}) => {
    const [builds, setBuilds] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [pagina, setPagina] = useState(0);
    const [totalPaginas, setTotalPaginas] = useState(1);

    useEffect(() => {
        if (show && idUsuario) {
            cargarBuilds(0); // siempre empieza en la página 0
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [show, idUsuario]);

    const cargarBuilds = async (paginaActual = 0) => {
        setLoading(true);
        setError(null);

        const result = await obtenerBuildsPorUsuario(idUsuario, paginaActual, 5);

        if (result.success) {
            const data = result.data;
            setBuilds(data.content || []);
            setPagina(data.number || 0);
            setTotalPaginas(data.totalPages || 1);
        } else {
            const mensaje =
                typeof result.error === "string"
                    ? result.error
                    : result.error?.message || "Error al obtener las builds";
            setError(mensaje);
        }

        setLoading(false);
    };

    return (
        <Modal show={show} onHide={onClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>Mis Armados Guardados</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {loading ? (
                    <div className="text-center">
                        <Spinner animation="border" />
                    </div>
                ) : error ? (
                    <Alert variant="danger">{error}</Alert>
                ) : builds.length === 0 ? (
                    <p className="text-muted text-center">No tienes builds guardadas.</p>
                ) : (
                    <ListGroup>
                        {builds.map((build) => (
                            <ListGroup.Item key={build.idBuild} className="px-3 py-2">
                                <div className="d-flex justify-content-between align-items-center mb-2">
                                    <div>
                                        <strong>{build.nombre}</strong>
                                        <br />
                                        <span className="text-muted">
                                            Total: S/. {(build.costoTotal ?? 0).toFixed(2)} —{" "}
                                            {build.compatible ? (
                                                <span className="text-success">Compatible ✅</span>
                                            ) : (
                                                <span className="text-danger">Incompatible ⚠️</span>
                                            )}
                                        </span>
                                    </div>
                                    <Button
                                        variant="outline-primary"
                                        size="sm"
                                        onClick={() => onSeleccionarBuild(build)}
                                    >
                                        Cargar
                                    </Button>
                                </div>

                                <ul className="mb-0 ps-3">
                                    {build.detalles.map((item, index) => (
                                        <li key={index} className="text-muted">
                                            {item.nombreProducto}
                                        </li>
                                    ))}
                                </ul>
                            </ListGroup.Item>
                        ))}
                    </ListGroup>
                )}
            </Modal.Body>

            {totalPaginas > 1 && (
                <div className="d-flex justify-content-between align-items-center px-3 py-2">
                    <Button
                        variant="outline-secondary"
                        disabled={pagina === 0}
                        onClick={() => cargarBuilds(pagina - 1)}
                    >
                        ← Anterior
                    </Button>
                    <span className="text-muted">
                        Página {pagina + 1} de {totalPaginas}
                    </span>
                    <Button
                        variant="outline-secondary"
                        disabled={pagina + 1 >= totalPaginas}
                        onClick={() => cargarBuilds(pagina + 1)}
                    >
                        Siguiente →
                    </Button>
                </div>
            )}

            <Modal.Footer>
                <Button variant="secondary" onClick={onClose}>
                    Cerrar
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default BuildsModal;
