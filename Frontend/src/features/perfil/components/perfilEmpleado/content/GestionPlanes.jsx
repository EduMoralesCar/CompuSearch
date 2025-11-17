
import React, { useState, useEffect } from 'react';
import { Card, Button, Table, Stack, Spinner, Alert, Pagination, Modal, Form, InputGroup, ListGroup } from 'react-bootstrap';
import { usePlanes } from '../../../hooks/usePlanes';


const ModalConfirmacion = ({ show, onHide, titulo, mensaje, onConfirmar, textoConfirmar, variantConfirmar = "primary" }) => {
    return (
        <Modal show={show} onHide={onHide} centered>
            <Modal.Header closeButton>
                <Modal.Title>{titulo}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p>{mensaje}</p>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>
                    Cancelar
                </Button>
                <Button variant={variantConfirmar} onClick={onConfirmar}>
                    {textoConfirmar}
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

const ModalDetallePlan = ({ show, handleClose, plan }) => {
    if (!plan) return null;

    // Función para mostrar los beneficios como una lista
    const renderBeneficios = (beneficiosString) => {
        if (!beneficiosString) return <p className="text-muted fst-italic">No hay beneficios detallados.</p>;
        
        // Intenta dividir por salto de línea
        const items = beneficiosString.split('\n').filter(item => item.trim() !== '');

        if (items.length > 0) {
            return (
                <ListGroup variant="flush">
                    {items.map((item, index) => (
                        <ListGroup.Item key={index} className="py-1">
                            {item.trim()}
                        </ListGroup.Item>
                    ))}
                </ListGroup>
            );
        }
        return <p>{beneficiosString}</p>;
    };

    return (
        <Modal show={show} onHide={handleClose} centered size="lg">
            <Modal.Header closeButton className="bg-primary text-white">
                <Modal.Title>Detalle del Plan: {plan.nombre}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <div className="row">
                    <div className="col-md-6 mb-3">
                        <Card className="shadow-sm border-primary">
                            <Card.Body>
                                <Card.Title className="text-primary">Información General</Card.Title>
                                <hr />
                                <ListGroup variant="flush">
                                    <ListGroup.Item>
                                        <strong>ID Plan:</strong> {plan.idPlan}
                                    </ListGroup.Item>
                                    <ListGroup.Item>
                                        <strong>Duración:</strong> {plan.duracionMeses} Meses
                                    </ListGroup.Item>
                                    <ListGroup.Item>
                                        <strong>Precio Mensual:</strong> 
                                        <span className="fw-bold text-success ms-2">
                                            S/.{parseFloat(plan.precioMensual).toFixed(2)}
                                        </span>
                                    </ListGroup.Item>
                                    <ListGroup.Item>
                                        <strong>Estado:</strong> 
                                        <span className={`badge ${plan.activo ? 'bg-success' : 'bg-secondary'} ms-2`}>
                                            {plan.activo ? 'Activo' : 'Inactivo'}
                                        </span>
                                    </ListGroup.Item>
                                </ListGroup>
                            </Card.Body>
                        </Card>
                    </div>
                    <div className="col-md-6 mb-3">
                        <Card className="shadow-sm h-100 border-info">
                            <Card.Body>
                                <Card.Title className="text-info">Métricas</Card.Title>
                                <hr />
                                <div className="d-flex justify-content-center align-items-center h-75">
                                    <div className="text-center">
                                        <h1 className="display-4 text-info">{plan.suscripciones || 0}</h1>
                                        <p className="lead">Suscripciones Activas</p>
                                    </div>
                                </div>
                            </Card.Body>
                        </Card>
                    </div>
                </div>

                <Card className="mt-3 shadow-sm">
                    <Card.Body>
                        <Card.Title className="text-primary">Descripción</Card.Title>
                        <hr />
                        <p>{plan.descripcion}</p>
                    </Card.Body>
                </Card>

                <Card className="mt-3 shadow-sm">
                    <Card.Body>
                        <Card.Title className="text-primary">Beneficios</Card.Title>
                        <hr />
                        {renderBeneficios(plan.beneficios)}
                    </Card.Body>
                </Card>

            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Cerrar
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

const ModalGestionPlan = ({ show, handleClose, plan, onSubmit }) => {
    const isEditing = !!plan;

    const [formData, setFormData] = useState({
        nombre: '',
        duracionMeses: 1,
        precioMensual: 0.01,
        descripcion: '',
        beneficios: ''
    });

    useEffect(() => {
        if (isEditing && plan) {
            setFormData({
                nombre: plan.nombre || '',
                duracionMeses: plan.duracionMeses || 1,
                precioMensual: plan.precioMensual ? parseFloat(plan.precioMensual) : 0.01,
                descripcion: plan.descripcion || '',
                beneficios: plan.beneficios || ''
            });
        } else {
            setFormData({
                nombre: '',
                duracionMeses: 1,
                precioMensual: 0.01,
                descripcion: '',
                beneficios: ''
            });
        }
    }, [plan, isEditing]);

    const handleChange = (e) => {
        const { name, value, type } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'number' ? parseFloat(value) : value,
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (formData.duracionMeses < 1 || formData.precioMensual <= 0) {
            console.error('Asegúrate de que la duración sea de al menos 1 mes y el precio sea positivo.');
            return;
        }

        onSubmit(formData);
    };

    return (
        <Modal show={show} onHide={handleClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>{isEditing ? `Editar Plan: ${plan.nombre}` : "Crear Nuevo Plan"}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form onSubmit={handleSubmit}>
                    <Form.Group className="mb-3" controlId="formNombre">
                        <Form.Label>Nombre del Plan</Form.Label>
                        <Form.Control
                            type="text"
                            name="nombre"
                            value={formData.nombre}
                            onChange={handleChange}
                            required
                            maxLength={100}
                        />
                    </Form.Group>

                    <Stack direction="horizontal" gap={3}>
                        <Form.Group className="mb-3 w-50" controlId="formDuracionMeses">
                            <Form.Label>Duración (Meses)</Form.Label>
                            <Form.Control
                                type="number"
                                name="duracionMeses"
                                value={formData.duracionMeses}
                                onChange={handleChange}
                                min="1"
                                required
                            />
                        </Form.Group>

                        <Form.Group className="mb-3 w-50" controlId="formPrecioMensual">
                            <Form.Label>Precio Mensual</Form.Label>
                            <InputGroup>
                                <InputGroup.Text>S/.</InputGroup.Text>
                                <Form.Control
                                    type="number"
                                    name="precioMensual"
                                    value={formData.precioMensual}
                                    onChange={handleChange}
                                    min="0.01"
                                    step="0.01"
                                    required
                                />
                            </InputGroup>
                        </Form.Group>
                    </Stack>

                    <Form.Group className="mb-3" controlId="formDescripcion">
                        <Form.Label>Descripción</Form.Label>
                        <Form.Control
                            as="textarea"
                            rows={2}
                            name="descripcion"
                            value={formData.descripcion}
                            onChange={handleChange}
                            required
                            maxLength={500}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formBeneficios">
                        <Form.Label>Beneficios (Opcional)</Form.Label>
                        <Form.Control
                            as="textarea"
                            rows={2}
                            name="beneficios"
                            value={formData.beneficios}
                            onChange={handleChange}
                            maxLength={500}
                            placeholder="Ingrese cada beneficio en una nueva línea o separado por comas."
                        />
                    </Form.Group>
                    <div className="d-flex justify-content-end">
                        <Button variant="primary" type="submit" className="mt-3">
                            {isEditing ? "Guardar Cambios" : "Crear Plan"}
                        </Button>
                    </div>
                </Form>
            </Modal.Body>
        </Modal>
    );
};

// =========================================================================
// 3. MODAL DE GESTIÓN DE PLANES (Crear/Editar) - Se mantiene la estructura
// =========================================================================

const PAGE_SIZE = 10;

export const GestionPlanes = () => {
    const {
        planes,
        totalPages,
        loading,
        error,
        obtenerTodosLosPlanes,
        crearPlan,
        actualizarPlan,
        actualizarEstadoActivo,
        clearError
    } = usePlanes();

    const [currentPage, setCurrentPage] = useState(0);
    const [showModal, setShowModal] = useState(false); // Modal Crear/Editar
    const [showDetailModal, setShowDetailModal] = useState(false); // NUEVO: Modal Detalle
    const [planSeleccionado, setPlanSeleccionado] = useState(null); // Plan para Crear/Editar
    const [planDetalleData, setPlanDetalleData] = useState(null); // NUEVO: Plan para Detalle

    const [mensaje, setMensaje] = useState("");
    const [tipoMensaje, setTipoMensaje] = useState("success");
    const [showConfirm, setShowConfirm] = useState(false);
    const [planACambiarEstado, setPlanACambiarEstado] = useState(null);

    useEffect(() => {
        obtenerTodosLosPlanes(currentPage, PAGE_SIZE, '', true);
    }, [currentPage, obtenerTodosLosPlanes]);

    const handleCrear = () => {
        setPlanSeleccionado(null);
        setMensaje("");
        clearError();
        setShowModal(true);
    };

    const handleEditar = (plan) => {
        setPlanSeleccionado(plan);
        setMensaje("");
        clearError();
        setShowModal(true);
    };
    
    // Handler para abrir el modal de Detalle
    const handleVerDetalle = (plan) => {
        setPlanDetalleData(plan);
        setShowDetailModal(true);
    };

    const confirmarCambioEstado = (plan) => {
        setPlanACambiarEstado(plan);
        setShowConfirm(true);
    };

    const handleCambiarEstado = async () => {
        if (!planACambiarEstado) return;

        const nuevoEstado = !planACambiarEstado.activo;
        const result = await actualizarEstadoActivo(planACambiarEstado.idPlan, nuevoEstado);

        if (result.success) {
            setTipoMensaje("success");
            setMensaje(`Plan "${planACambiarEstado.nombre}" ${nuevoEstado ? 'activado' : 'desactivado'} correctamente.`);
        } else {
            setTipoMensaje("danger");
            setMensaje(result.error || `Error al cambiar el estado del plan: ${planACambiarEstado.nombre}`);
        }

        setShowConfirm(false);
        setPlanACambiarEstado(null);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setMensaje("");
    };

    const handleSubmit = async (planData) => {
        let result;
        if (!planSeleccionado) {
            result = await crearPlan(planData);
            if (result.success) {
                setTipoMensaje("success");
                setMensaje("Plan creado correctamente.");
            } else {
                setTipoMensaje("danger");
                setMensaje(result.error || "Error al crear el plan.");
            }
        } else {
            result = await actualizarPlan(planSeleccionado.idPlan, planData);
            if (result.success) {
                setTipoMensaje("success");
                setMensaje("Plan actualizado correctamente.");
            } else {
                setTipoMensaje("danger");
                setMensaje(result.error || "Error al actualizar el plan.");
            }
        }
        setShowModal(false);
        obtenerTodosLosPlanes(currentPage, PAGE_SIZE, '', true);
    };

    const renderPaginationItems = () => {
        const items = [];
        for (let number = 0; number < totalPages; number++) {
            items.push(
                <Pagination.Item
                    key={number}
                    active={number === currentPage}
                    onClick={() => setCurrentPage(number)}
                >
                    {number + 1}
                </Pagination.Item>
            );
        }
        return items;
    };


    // Renderizado del componente
    return (
        <>
            <Card className="shadow-lg border-0">
                <Card.Header as="h5" className="d-flex justify-content-between align-items-center bg-light text-primary">
                    Gestión de Planes de Suscripción
                    <Button variant="primary" onClick={handleCrear}>
                        Crear Nuevo Plan
                    </Button>
                </Card.Header>
                <Card.Body>
                    <div className="text-center">
                        {loading && <Spinner animation="border" variant="primary" />}
                    </div>

                    {mensaje && <Alert variant={tipoMensaje}>{mensaje}</Alert>}
                    {!mensaje && error && <Alert variant="danger">{error}</Alert>}

                    {!loading && planes.length === 0 && !error && (
                        <Alert variant="info">No se encontraron planes disponibles.</Alert>
                    )}

                    {!loading && planes.length > 0 && (
                        <>
                            <Table striped bordered hover responsive className="mb-4">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Nombre del Plan</th>
                                        <th>Duración (Meses)</th>
                                        <th>Precio Mensual</th>
                                        <th>Estado</th>
                                        <th style={{ width: "250px" }} className="text-center">Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {planes.map((plan) => (
                                        <tr key={plan.idPlan}>
                                            <td>{plan.idPlan}</td>
                                            <td>{plan.nombre}</td>
                                            <td>{plan.duracionMeses}</td>
                                            <td>
                                                <span className="fw-semibold">
                                                    S/.{parseFloat(plan.precioMensual).toFixed(2)}
                                                </span>
                                            </td>
                                            <td>
                                                <span className={`badge ${plan.activo ? 'bg-success' : 'bg-secondary'}`}>
                                                    {plan.activo ? 'Activo' : 'Inactivo'}
                                                </span>
                                            </td>
                                            <td>
                                                <Stack direction="horizontal" gap={2} className="justify-content-center">
                                                    {/* Botón de detalle */}
                                                    <Button
                                                        variant="info"
                                                        size="sm"
                                                        onClick={() => handleVerDetalle(plan)}
                                                        title="Ver Detalles del Plan"
                                                    >
                                                        Detalle
                                                    </Button>
                                                    
                                                    <Button
                                                        variant="warning"
                                                        size="sm"
                                                        onClick={() => handleEditar(plan)}
                                                        title="Editar Plan"
                                                    >
                                                        Editar
                                                    </Button>
                                                    <Button
                                                        variant={plan.activo ? "danger" : "success"}
                                                        size="sm"
                                                        onClick={() => confirmarCambioEstado(plan)}
                                                        title={plan.activo ? "Desactivar Plan" : "Activar Plan"}
                                                    >
                                                        {plan.activo ? "Desactivar" : "Activar"}
                                                    </Button>
                                                </Stack>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </Table>

                            {/* COMPONENTE DE PAGINACIÓN */}
                            <div className="d-flex justify-content-center">
                                <Pagination>
                                    <Pagination.First onClick={() => setCurrentPage(0)} disabled={currentPage === 0} />
                                    <Pagination.Prev onClick={() => setCurrentPage(currentPage - 1)} disabled={currentPage === 0} />
                                    {renderPaginationItems()}
                                    <Pagination.Next onClick={() => setCurrentPage(currentPage + 1)} disabled={currentPage === totalPages - 1} />
                                    <Pagination.Last onClick={() => setCurrentPage(totalPages - 1)} disabled={currentPage === totalPages - 1} />
                                </Pagination>
                            </div>
                        </>
                    )}
                </Card.Body>
            </Card>

            {/* Modal de detalle (NUEVO) */}
            <ModalDetallePlan
                show={showDetailModal}
                handleClose={() => setShowDetailModal(false)}
                plan={planDetalleData}
            />

            {/* Modal de gestión (Crear/Editar) */}
            <ModalGestionPlan
                show={showModal}
                handleClose={handleCloseModal}
                plan={planSeleccionado}
                onSubmit={handleSubmit}
            />

            {/* Modal de confirmación para cambiar el estado */}
            <ModalConfirmacion
                show={showConfirm}
                onHide={() => setShowConfirm(false)}
                titulo={planACambiarEstado?.activo ? "Desactivar Plan" : "Activar Plan"}
                mensaje={`¿Estás seguro de ${planACambiarEstado?.activo ? 'desactivar' : 'activar'} el plan "${planACambiarEstado?.nombre || ""}"?`}
                onConfirmar={handleCambiarEstado}
                textoConfirmar={planACambiarEstado?.activo ? "Desactivar" : "Activar"}
                variantConfirmar={planACambiarEstado?.activo ? "danger" : "success"}
            />
        </>
    );
};

export default GestionPlanes;