import React, { useState, useEffect, useCallback } from "react";
import { Card, Button, Alert, Spinner, Form } from "react-bootstrap";
import useEmpleadosApi from "../../../hooks/useEmpleados";
import ModalFormularioEmpleado from "../modal/ModalFormularioEmpleado";
import TablaEmpleados from "../table/TablaEmpleados";
import ModalInfoEmpleado from "../modal/ModalInfoEmpleado"
import { FiPlus } from "react-icons/fi";
import { FiRefreshCw } from "react-icons/fi";

const GestionEmpleados = () => {
    const {
        getEmpleados,
        updateEmpleadoActivo,
        createEmpleado,
        updateEmpleado,
        isLoading,
        error,
        empleados: empleadosFromHook
    } = useEmpleadosApi();

    const [empleados, setEmpleados] = useState(empleadosFromHook);
    useEffect(() => {
        setEmpleados(empleadosFromHook);
    }, [empleadosFromHook]);

    const [isTogglingId, setIsTogglingId] = useState(null);
    const [showModalInfo, setShowModalInfo] = useState(false);
    const [showModalForm, setShowModalForm] = useState(false);
    const [empleadoSeleccionado, setEmpleadoSeleccionado] = useState(null);
    const [empleadoAEditar, setEmpleadoAEditar] = useState(null);
    const [gestionError, setGestionError] = useState(null);
    const [formSubmitError, setFormSubmitError] = useState(null);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const [searchTerm, setSearchTerm] = useState('');

    const fetchEmpleados = useCallback(async (search) => {
        setGestionError(null);
        await getEmpleados(0, 50, search);
    }, [getEmpleados]);

    useEffect(() => {
        fetchEmpleados('');
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const handleInputChange = (e) => {
        setSearchTerm(e.target.value);
    };

    const handleSearchSubmit = () => {
        fetchEmpleados(searchTerm);
    };

    const handleClearSearch = () => {
        setSearchTerm('');
        fetchEmpleados('');
    };

    const handleToggleActivo = async (idUsuario, currentActivo) => {
        setIsTogglingId(idUsuario);
        setGestionError(null);

        const nuevoEstado = !currentActivo;
        const response = await updateEmpleadoActivo(idUsuario, nuevoEstado);

        if (!response.success) {
            setGestionError(response.error || `Error al cambiar el estado activo del empleado ID ${idUsuario}.`);
        }

        setIsTogglingId(null);
    };

    const handleVerInfo = (empleado) => {
        setEmpleadoSeleccionado(empleado);
        setShowModalInfo(true);
    };

    const handleCloseModalInfo = () => {
        setShowModalInfo(false);
        setEmpleadoSeleccionado(null);
    };

    const handleCrear = () => {
        setEmpleadoAEditar(null);
        setFormSubmitError(null);
        setShowModalForm(true);
    };

    const handleEditar = (empleado) => {
        setEmpleadoAEditar(empleado);
        setFormSubmitError(null);
        setShowModalForm(true);
    };

    const handleCloseModalForm = () => {
        setShowModalForm(false);
        setEmpleadoAEditar(null);
        setFormSubmitError(null);
    };

    const handleCreateOrUpdate = async (empleadoData) => {
        setIsSubmitting(true);
        setFormSubmitError(null);

        let response;
        if (empleadoAEditar) {
            response = await updateEmpleado(empleadoAEditar.idUsuario, empleadoData);
        } else {
            response = await createEmpleado(empleadoData);
        }

        if (response.success) {
            handleCloseModalForm();
            fetchEmpleados(searchTerm);
        } else {
            setFormSubmitError(response.error || 'Ocurrió un error desconocido al guardar el empleado.');
        }

        setIsSubmitting(false);
    };

    const isGlobalLoading = isLoading && isTogglingId === null && !isSubmitting;

    return (
        <>
            <Card className="shadow-lg border-0">
                <Card.Header as="h5" className="d-flex justify-content-between align-items-center bg-light text-primary">
                    Gestión de Empleados

                    <div className="d-flex align-items-center gap-3">

                        <Form.Control
                            type="text"
                            placeholder="Buscar por Username..."
                            value={searchTerm}
                            onChange={handleInputChange}
                            disabled={isGlobalLoading && empleados.length === 0}
                            style={{ minWidth: "250px" }}
                        />

                        <Button
                            variant="primary"
                            onClick={handleSearchSubmit}
                            disabled={isGlobalLoading}
                            className="px-3"
                        >
                            {isGlobalLoading ? (
                                <Spinner animation="border" size="sm" />
                            ) : 'Buscar'}
                        </Button>

                        <Button
                            variant="outline-secondary"
                            onClick={handleClearSearch}
                            disabled={isGlobalLoading || searchTerm === ''}
                            className="px-3"
                        >
                            Limpiar
                        </Button>

                        <Button variant="success" onClick={handleCrear} disabled={isGlobalLoading}>
                            <FiPlus size={18} />
                        </Button>

                        <Button
                            variant="outline-secondary"
                            onClick={handleSearchSubmit}
                            disabled={isGlobalLoading}
                            className="d-flex align-items-center justify-content-center"
                            style={{ width: "42px", height: "38px" }}
                        >
                            <FiRefreshCw size={18} />
                        </Button>

                    </div>

                </Card.Header>

                <Card.Body>

                    {(error || gestionError) && (
                        <Alert variant="danger">{error || gestionError}</Alert>
                    )}

                    {isGlobalLoading && empleados.length === 0 && (
                        <div className="text-center p-5">
                            <Spinner animation="border" role="status" className="me-2" />
                            Cargando empleados...
                        </div>
                    )}

                    {!(isGlobalLoading && empleados.length === 0) && (
                        <TablaEmpleados
                            empleados={empleados}
                            onVerInfo={handleVerInfo}
                            onToggleActivo={handleToggleActivo}
                            onEditar={handleEditar}
                            isTogglingId={isTogglingId}
                        />
                    )}

                </Card.Body>
            </Card>

            <ModalInfoEmpleado
                show={showModalInfo}
                handleClose={handleCloseModalInfo}
                empleado={empleadoSeleccionado}
            />

            <ModalFormularioEmpleado
                show={showModalForm}
                handleClose={handleCloseModalForm}
                empleadoAEditar={empleadoAEditar}
                onCreateOrUpdate={handleCreateOrUpdate}
                isSubmitting={isSubmitting}
                submitError={formSubmitError}
            />
        </>
    );
};

export default GestionEmpleados;