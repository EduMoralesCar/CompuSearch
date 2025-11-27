import React, { useEffect, useState } from "react";
import { ListGroup, Spinner, Alert, Button, Badge, Accordion, Card } from "react-bootstrap";
import { useAuthStatus } from "../../../../../hooks/useAuthStatus";
import { useNavigate } from "react-router-dom";
import useBuilds from "../../../../navigation/hooks/useBuilds";

const MisBuilds = () => {
    const { idUsuario } = useAuthStatus();
    const navigate = useNavigate();
    const { obtenerBuildsPorUsuario, loading, error, respuesta } = useBuilds();

    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    useEffect(() => {
        if (idUsuario) {
            const fetchBuilds = async () => {
                const res = await obtenerBuildsPorUsuario(idUsuario, page, 5);
                console.log(res)
                if (res) {
                    setTotalPages(res.data.totalPages || 0);
                }
            };
            fetchBuilds();
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [page]);

    const handleNavigateToBuilds = (idBuild) => {
        navigate(`/builds?idBuild=${idBuild}`);
    };

    if (loading) {
        return <div className="text-center py-5"><Spinner animation="border" variant="primary" /></div>;
    }

    if (error) {
        return <Alert variant="danger">{error}</Alert>;
    }

    const builds = respuesta?.content || [];

    if (builds.length === 0) {
        return (
            <Alert variant="info">
                Aún no tienes ninguna construcción guardada.{" "}
                <Alert.Link
                    as="button"
                    className="btn btn-link p-0 m-0 align-baseline"
                    onClick={() => navigate("/builds")}
                >
                    ¡Crea una ahora!
                </Alert.Link>
            </Alert>
        );
    }

    return (
        <>
            <h3 className="mb-4">Mis Builds</h3>
            <ListGroup variant="flush">
                {builds.map(build => (
                    <ListGroup.Item key={build.idBuild} className="px-0 py-3">
                        <div className="d-flex justify-content-between align-items-center mb-2">
                            <div>
                                <h4 className="mb-1 fw-bold">{build.nombre}</h4>
                                <small className="text-muted">Costo Total: S/ {build.costoTotal.toFixed(2)}</small>
                                <Badge bg={build.compatible ? "success" : "danger"} className="ms-2">
                                    {build.compatible ? "Compatible" : "Incompatible"}
                                </Badge>
                            </div>
                            <Button variant="outline-primary" size="sm" onClick={() => handleNavigateToBuilds(build.idBuild)}>
                                Ver/Editar
                            </Button>
                        </div>

                        <Accordion>
                            {build.detalles.map((producto, index) => (
                                <Accordion.Item key={producto.idProductoTienda} eventKey={index.toString()}>
                                    <Accordion.Header
                                        style={{
                                            fontSize: '0.7rem',
                                            padding: '0.4rem 0.4rem'
                                        }}
                                    >
                                        {producto.categoria}: {producto.nombreProducto}
                                    </Accordion.Header>
                                    <Accordion.Body>
                                        <p><strong>Tienda:</strong> {producto.nombreTienda}</p>
                                        <p>
                                            <strong>Precio:</strong> S/ {producto.precio.toFixed(2)} x {producto.cantidad} = S/ {producto.subTotal.toFixed(2)}
                                        </p>
                                        <Button
                                            as="a"
                                            href={producto.urlProducto}
                                            target="_blank"
                                            rel="noopener noreferrer"
                                            variant="outline-primary"
                                            size="sm"
                                        >
                                            Ver producto
                                        </Button>

                                        <ul>
                                            {(producto?.detalles ?? []).map(attr => (
                                                <li key={attr.nombreAtributo}>
                                                    <strong>{attr.nombreAtributo}:</strong> {attr.valor}
                                                </li>
                                            ))}

                                            {(!producto?.detalles || producto.detalles.length === 0) && (
                                                <li className="text-muted">Sin detalles adicionales</li>
                                            )}
                                        </ul>

                                    </Accordion.Body>
                                </Accordion.Item>
                            ))}
                        </Accordion>


                    </ListGroup.Item>
                ))}
            </ListGroup>

            {totalPages > 1 && (
                <div className="d-flex justify-content-between mt-4">
                    <Button onClick={() => setPage(p => p - 1)} disabled={page === 0 || loading}>Anterior</Button>
                    <span className="align-self-center">Página {page + 1} de {totalPages}</span>
                    <Button onClick={() => setPage(p => p + 1)} disabled={page + 1 >= totalPages || loading}>Siguiente</Button>
                </div>
            )}
        </>
    );
};

export default MisBuilds;
