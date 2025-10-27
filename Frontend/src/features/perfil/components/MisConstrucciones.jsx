import React, { useState, useEffect, useCallback } from "react";
import { ListGroup, Spinner, Alert, Button, Badge } from "react-bootstrap";
import { useAuth } from "../../../context/useAuth";
import { useNavigate } from "react-router-dom";
import axios from "axios"; 

const MisConstrucciones = ({ setStats }) => {
    const { idUsuario } = useAuth();
    const navigate = useNavigate();
    
    const [builds, setBuilds] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    const cargarBuilds = useCallback(async (pagina) => {
        if (!idUsuario) {
            setLoading(false);
            return;
        }
        setLoading(true);
        setError(null);
        try {
            const res = await axios.get(`http://localhost:8080/builds/usuario/${idUsuario}`, {
                params: { page: pagina, size: 5 },
                withCredentials: true
            });
            
            const data = res.data;
            setBuilds(data.content || []);
            setTotalPages(data.totalPages || 0);

            setStats(prev => ({ ...prev, construcciones: data.totalElements || 0 }));

        } catch (err) {
            setError("No se pudieron cargar tus construcciones.");
        } finally {
            setLoading(false);
        }
    }, [idUsuario, setStats]);

    useEffect(() => {
        cargarBuilds(page);
    }, [page, cargarBuilds]);

    const handleNavigateToBuilds = (build) => {
        navigate('/builds', { state: { buildToLoad: build } });
    };

    if (loading) {
        return <div className="text-center py-5"><Spinner animation="border" variant="primary"/></div>;
    }

    if (error) {
        return <Alert variant="danger">{error}</Alert>;
    }

    return (
        <>
            <h3 className="mb-4">Mis Construcciones</h3>
            {builds.length > 0 ? (
                <>
                    <ListGroup variant="flush">
                        {builds.map(build => (
                            <ListGroup.Item key={build.idBuild} className="d-flex justify-content-between align-items-center px-0 py-3">
                                <div>
                                    <h5 className="mb-1 fw-bold">{build.nombre}</h5>
                                    <small className="text-muted">Costo Total: S/ {build.costoTotal.toFixed(2)}</small>
                                    <Badge bg={build.compatible ? "success" : "danger"} className="ms-2">
                                        {build.compatible ? "Compatible" : "Incompatible"}
                                    </Badge>
                                </div>
                                <Button variant="outline-primary" size="sm" onClick={() => handleNavigateToBuilds(build)}>
                                    Ver/Editar
                                </Button>
                            </ListGroup.Item>
                        ))}
                    </ListGroup>

                    {totalPages > 1 && (
                         <div className="d-flex justify-content-between mt-4">
                            <Button onClick={() => setPage(p => p - 1)} disabled={page === 0}>Anterior</Button>
                            <span className="align-self-center">Página {page + 1} de {totalPages}</span>
                            <Button onClick={() => setPage(p => p + 1)} disabled={page + 1 >= totalPages}>Siguiente</Button>
                        </div>
                    )}
                </>
            ) : (
                <Alert variant="info">
                    Aún no tienes ninguna construcción guardada. <Alert.Link href="/builds">¡Crea una ahora!</Alert.Link>
                </Alert>
            )}
        </>
    );
};

export default MisConstrucciones;