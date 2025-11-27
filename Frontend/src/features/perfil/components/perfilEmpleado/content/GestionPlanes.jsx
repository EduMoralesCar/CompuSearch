
import React, { useState, useEffect } from 'react';
import { Card, Button, Pagination } from 'react-bootstrap';
import { usePlanes } from '../../../hooks/usePlanes';
import ModalConfirmacion from "../modal/ModalConfirmacion"
import ModalGestionPlan from '../modal/ModalGestionPlan';
import TablaPlanes from "../table/TablaPlanes"
import ModalDetallePlan from "../modal/ModalDetallePlan"

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
    const [showModal, setShowModal] = useState(false);
    const [showDetailModal, setShowDetailModal] = useState(false);
    const [planSeleccionado, setPlanSeleccionado] = useState(null);
    const [planDetalleData, setPlanDetalleData] = useState(null);

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
                    <TablaPlanes
                        planes={planes}
                        loading={loading}
                        error={error}
                        mensaje={mensaje}
                        tipoMensaje={tipoMensaje}
                        handleVerDetalle={handleVerDetalle}
                        handleEditar={handleEditar}
                        confirmarCambioEstado={confirmarCambioEstado}
                        currentPage={currentPage}
                        totalPages={totalPages}
                        setCurrentPage={setCurrentPage}
                    />
                </Card.Body>
                
                {totalPages > 1 && (
                    <div className="d-flex justify-content-center">
                        <Pagination>
                            <Pagination.First
                                onClick={() => setCurrentPage(0)}
                                disabled={currentPage === 0}
                            />
                            <Pagination.Prev
                                onClick={() => setCurrentPage(currentPage - 1)}
                                disabled={currentPage === 0}
                            />

                            {renderPaginationItems()}

                            <Pagination.Next
                                onClick={() => setCurrentPage(currentPage + 1)}
                                disabled={currentPage === totalPages - 1}
                            />
                            <Pagination.Last
                                onClick={() => setCurrentPage(totalPages - 1)}
                                disabled={currentPage === totalPages - 1}
                            />
                        </Pagination>
                    </div>
                )}

            </Card>

            <ModalDetallePlan
                show={showDetailModal}
                handleClose={() => setShowDetailModal(false)}
                plan={planDetalleData}
            />

            <ModalGestionPlan
                show={showModal}
                handleClose={handleCloseModal}
                plan={planSeleccionado}
                onSubmit={handleSubmit}
            />

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