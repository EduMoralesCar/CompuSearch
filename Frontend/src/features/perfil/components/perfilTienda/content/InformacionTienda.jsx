import { useEffect, useState, useCallback } from "react";
import { useTiendas } from "../../../hooks/useTiendas";
import {
    Container, Row, Col, Card, Alert, Spinner, Badge, Image
} from "react-bootstrap";

import TiendaFormulario from "../form/TiendaFormulario";
import TiendaLogoUploader from "../form/TiendaLogoUploader"

const InformacionTienda = ({ idTienda }) => {
    const [infoTienda, setInfoTienda] = useState(null);
    const [statusFormulario, setStatusFormulario] = useState(null);
    const [statusLogo, setStatusLogo] = useState(null);

    const [formDatos, setFormDatos] = useState({
        nombre: "",
        descripcion: "",
        direccion: "",
        telefono: "",
        urlPagina: "",
    });

    const {
        obtenerTiendaPorId,
        actualizarDatosTienda,
        actualizarLogo,
        loading,
        error,
    } = useTiendas();

    const fetchTiendaData = useCallback(async () => {
        if (!idTienda) return;
        const result = await obtenerTiendaPorId(idTienda, false);
        if (result.success) {
            setInfoTienda(result.data);
        }
    }, [idTienda, obtenerTiendaPorId]);

    useEffect(() => {
        fetchTiendaData();
    }, [fetchTiendaData]);

    useEffect(() => {
        if (infoTienda) {
            setFormDatos({
                nombre: infoTienda.nombre || "",
                descripcion: infoTienda.descripcion || "",
                direccion: infoTienda.direccion || "",
                telefono: infoTienda.telefono || "",
                urlPagina: infoTienda.pagina || "",
            });
        }
    }, [infoTienda]);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormDatos((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!idTienda) return;
        setStatusFormulario(null);
        const result = await actualizarDatosTienda(idTienda, formDatos);

        if (result.success) {
            setStatusFormulario({
                type: "success",
                message: "¡Datos de la tienda actualizados con éxito!",
            });
            await fetchTiendaData();
        } else {
            setStatusFormulario({
                type: "error",
                message: result.error
            });
        }
    };

    if (loading && !infoTienda) {
        return <Spinner animation="border" variant="primary" />;
    }

    if (error && !infoTienda) {
        return <Alert variant="danger">Error al cargar la tienda: {error}</Alert>;
    }

    if (!infoTienda) {
        return <h1>Información de la tienda</h1>;
    }

    return (
        <Container className="py-4">

            <Row className="mb-4">
                <Col>
                    <h2 className="fw-bold text-primary">
                        Información de la Tienda
                    </h2>
                    <p className="text-muted">
                        Revisa y edita los datos principales de tu tienda.
                    </p>
                </Col>
            </Row>

            <Row className="g-4">

                <Col lg={7}>
                    <Card className="shadow-sm border-0">
                        <Card.Body>
                            <div className="d-flex align-items-center justify-content-between mb-3">
                                <h4 className="fw-semibold text-dark mb-0">
                                    {infoTienda.nombre}
                                </h4>
                                <Badge bg={infoTienda.activo ? "success" : "danger"}>
                                    {infoTienda.activo ? "Activa" : "Inactiva"}
                                </Badge>
                            </div>

                            <div className="mb-3 text-muted">
                                <small>ID de Usuario: {infoTienda.idUsuario}</small>
                            </div>

                            {infoTienda.logo && (
                                <div className="text-center mb-4">
                                    <Image
                                        src={`data:image/png;base64,${infoTienda.logo}`}
                                        alt="Logo"
                                        rounded
                                        fluid
                                        className="border p-2 bg-light"
                                        style={{ maxWidth: "180px" }}
                                    />
                                </div>
                            )}

                            <div className="mb-3">
                                <strong>Miembro desde:</strong>{" "}
                                {new Date(infoTienda.fechaAfiliacion).toLocaleDateString()}
                            </div>

                            <div className="mt-4">
                                <h5 className="fw-semibold text-secondary">Datos Generales</h5>
                                <ul className="list-unstyled mt-3">
                                    <li className="mb-1">
                                        <strong>Descripción:</strong> {infoTienda.descripcion || "—"}
                                    </li>
                                    <li className="mb-1">
                                        <strong>Teléfono:</strong> {infoTienda.telefono || "—"}
                                    </li>
                                    <li className="mb-1">
                                        <strong>Dirección:</strong> {infoTienda.direccion || "—"}
                                    </li>
                                    <li className="mb-1">
                                        <strong>Página Web:</strong>{" "}
                                        {infoTienda.pagina ? (
                                            <a href={infoTienda.pagina} target="_blank" rel="noreferrer">
                                                {infoTienda.pagina}
                                            </a>
                                        ) : "—"}
                                    </li>
                                </ul>
                            </div>

                            <hr />

                            <div>
                                <h5 className="fw-semibold text-secondary">Inventario</h5>
                                <p className="mb-1">
                                    <strong>Productos totales:</strong> {infoTienda.productos}
                                </p>
                                <p>
                                    <strong>Etiquetas:</strong>{" "}
                                    {infoTienda.etiquetas?.length
                                        ? infoTienda.etiquetas.map(e => e.nombre).join(", ")
                                        : "Sin etiquetas"}
                                </p>
                            </div>
                        </Card.Body>
                    </Card>
                </Col>

                <Col lg={5}>
                    <Card className="shadow-sm border-0">
                        <Card.Body>
                            <h5 className="fw-semibold mb-3 text-secondary">
                                Editar Información
                            </h5>

                            <TiendaFormulario
                                formDatos={formDatos}
                                handleInputChange={handleInputChange}
                                handleSubmit={handleSubmit}
                                loading={loading}
                                updateStatus={statusFormulario}
                            />

                        </Card.Body>
                    </Card>

                    <Card className="shadow-sm border-0 mt-4">
                        <Card.Body>
                            <h5 className="fw-semibold mb-3 text-secondary">
                                Actualizar Logo
                            </h5>

                            <TiendaLogoUploader
                                idTienda={idTienda}
                                actualizarLogo={actualizarLogo}
                                fetchTiendaData={fetchTiendaData}
                                loading={loading}
                                updateStatus={statusLogo}
                                setUpdateStatus={setStatusLogo}
                            />

                        </Card.Body>
                    </Card>
                </Col>

            </Row>
        </Container>
    );
};

export default InformacionTienda;
