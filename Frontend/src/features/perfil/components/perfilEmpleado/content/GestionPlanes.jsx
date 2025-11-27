import React, { useState, useEffect } from 'react';
import { Card, Button } from 'react-bootstrap';
import { FiPlus } from "react-icons/fi";

import { usePlanes } from '../../../hooks/usePlanes';
import ModalConfirmacion from "../modal/ModalConfirmacion";
import ModalGestionPlan from '../modal/ModalGestionPlan';
import ModalDetallePlan from "../modal/ModalDetallePlan";
import TablaPlanes from "../table/TablaPlanes";
import HeaderBase from '../auxiliar/HeaderBase';
import PaginacionBase from '../auxiliar/PaginacionBase';

const PAGE_SIZE = 10;

const GestionPlanes = () => {
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

    // ---- HANDLERS ----
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
            setTipoMensaje(result.success ? "success" : "danger");
            setMensaje(result.success ? "Plan creado correctamente." : result.error || "Error al crear el plan.");
        } else {
            result = await actualizarPlan(planSeleccionado.idPlan, planData);
            setTipoMensaje(result.success ? "success" : "danger");
            setMensaje(result.success ? "Plan actualizado correctamente." : result.error || "Error al actualizar el plan.");
        }

        setShowModal(false);
        obtenerTodosLosPlanes(currentPage, PAGE_SIZE, '', true);
    };

    return (
        <>
            <Card className="shadow-lg border-0">
                <HeaderBase title="Gestión de Planes de Suscripción">
                    <Button variant="success" onClick={handleCrear} disabled={loading}>
                        <FiPlus size={18} />
                    </Button>
                </HeaderBase>

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

                <Card.Footer>
                    <div className="d-flex justify-content-center mt-3">
                        <PaginacionBase
                            page={currentPage}
                            totalPages={totalPages}
                            loading={loading}
                            onPageChange={setCurrentPage}
                        />
                    </div>
                </Card.Footer>
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
