import { useState, useEffect, useCallback } from "react";
import { Card, Button, Alert, Spinner, FormControl, InputGroup } from "react-bootstrap";
import { FiPlus, FiRefreshCw } from "react-icons/fi";
import useEmpleadosApi from "../../../hooks/useEmpleados";
import TablaEmpleados from "../table/TablaEmpleados";
import ModalFormularioEmpleado from "../modal/ModalFormularioEmpleado";
import ModalInfoEmpleado from "../modal/ModalInfoEmpleado";
import HeaderBase from "../auxiliar/HeaderBase";
import PaginacionBase from "../auxiliar/PaginacionBase";

const PAGE_SIZE = 20;

const GestionEmpleados = () => {
    const {
        getEmpleados,
        updateEmpleadoActivo,
        createEmpleado,
        updateEmpleado,
        isLoading,
        error,
        empleados: empleadosFromHook,
    } = useEmpleadosApi();

    const [empleados, setEmpleados] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [currentPage, setCurrentPage] = useState(0);

    const [isTogglingId, setIsTogglingId] = useState(null);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const [showModalInfo, setShowModalInfo] = useState(false);
    const [empleadoSeleccionado, setEmpleadoSeleccionado] = useState(null);

    const [showModalForm, setShowModalForm] = useState(false);
    const [empleadoAEditar, setEmpleadoAEditar] = useState(null);

    const [gestionError, setGestionError] = useState(null);
    const [formSubmitError, setFormSubmitError] = useState(null);

    // Sincronizar empleados del hook
    useEffect(() => setEmpleados(empleadosFromHook), [empleadosFromHook]);

    const fetchEmpleados = useCallback(async (page, search) => {
        setGestionError(null);
        await getEmpleados(page, PAGE_SIZE, search);
    }, [getEmpleados]);

    useEffect(() => { fetchEmpleados(0, ""); }, [fetchEmpleados]);

    // ---- BÚSQUEDA ----
    const handleInputChange = (e) => setSearchTerm(e.target.value);
    const handleSearchSubmit = () => { setCurrentPage(0); fetchEmpleados(0, searchTerm); };
    const handleClearSearch = () => { setSearchTerm(""); setCurrentPage(0); fetchEmpleados(0, ""); };

    // ---- TOGGLE ACTIVO ----
    const handleToggleActivo = async (idUsuario, currentActivo) => {
        setIsTogglingId(idUsuario);
        setGestionError(null);

        const response = await updateEmpleadoActivo(idUsuario, !currentActivo);
        if (!response.success) setGestionError(response.error || `Error al cambiar el estado del empleado ID ${idUsuario}.`);

        setIsTogglingId(null);
    };

    // ---- MODALES ----
    const handleVerInfo = (empleado) => { setEmpleadoSeleccionado(empleado); setShowModalInfo(true); };
    const handleCloseModalInfo = () => { setShowModalInfo(false); setEmpleadoSeleccionado(null); };

    const handleCrear = () => { setEmpleadoAEditar(null); setFormSubmitError(null); setShowModalForm(true); };
    const handleEditar = (empleado) => { setEmpleadoAEditar(empleado); setFormSubmitError(null); setShowModalForm(true); };
    const handleCloseModalForm = () => { setShowModalForm(false); setEmpleadoAEditar(null); setFormSubmitError(null); };

    // ---- CREAR / ACTUALIZAR ----
    const handleCreateOrUpdate = async (empleadoData) => {
        setIsSubmitting(true);
        setFormSubmitError(null);

        const response = empleadoAEditar
            ? await updateEmpleado(empleadoAEditar.idUsuario, empleadoData)
            : await createEmpleado(empleadoData);

        if (response.success) {
            handleCloseModalForm();
            fetchEmpleados(currentPage, searchTerm);
        } else {
            setFormSubmitError(response.error || "Ocurrió un error desconocido al guardar el empleado.");
        }

        setIsSubmitting(false);
    };

    const isGlobalLoading = isLoading && isTogglingId === null && !isSubmitting;

    return (
        <>
            <Card className="shadow-lg border-0">
                <HeaderBase title="Gestión de Empleados">
                    <InputGroup>
                        <FormControl
                            type="text"
                            placeholder="Buscar por Username"
                            value={searchTerm}
                            onChange={handleInputChange}
                            disabled={isGlobalLoading && empleados.length === 0}
                        />
                        <Button variant="primary" onClick={handleSearchSubmit} disabled={isGlobalLoading}>
                            {isGlobalLoading ? <Spinner animation="border" size="sm" /> : "Buscar"}
                        </Button>
                        <Button variant="outline-secondary" onClick={handleClearSearch} disabled={isGlobalLoading || searchTerm === ""}>
                            Limpiar
                        </Button>
                    </InputGroup>

                    <Button
                        variant="outline-secondary"
                        onClick={handleSearchSubmit}
                        disabled={isGlobalLoading}
                        className="d-flex align-items-center justify-content-center"
                        style={{ width: "42px", height: "38px" }}
                    >
                        <FiRefreshCw size={18} />
                    </Button>

                    <Button variant="success" onClick={handleCrear} disabled={isGlobalLoading}>
                        <FiPlus size={18} />
                    </Button>
                </HeaderBase>

                <Card.Body>
                    {(error || gestionError) && <Alert variant="danger">{error || gestionError}</Alert>}

                    {isGlobalLoading && empleados.length === 0 ? (
                        <div className="text-center p-5">
                            <Spinner animation="border" role="status" className="me-2" />
                            Cargando empleados...
                        </div>
                    ) : (
                        empleados.length > 0 && (
                            <TablaEmpleados
                                empleados={empleados}
                                onVerInfo={handleVerInfo}
                                onToggleActivo={handleToggleActivo}
                                onEditar={handleEditar}
                                isTogglingId={isTogglingId}
                            />
                        )
                    )}
                </Card.Body>

                <Card.Footer>
                    {empleadosFromHook?.totalPages > 1 && (
                        <div className="d-flex justify-content-center mt-3">
                            <PaginacionBase
                                page={currentPage}
                                totalPages={empleadosFromHook.totalPages}
                                loading={isGlobalLoading}
                                onPageChange={(newPage) => { setCurrentPage(newPage); fetchEmpleados(newPage, searchTerm); }}
                            />
                        </div>
                    )}
                </Card.Footer>
            </Card>

            <ModalInfoEmpleado show={showModalInfo} handleClose={handleCloseModalInfo} empleado={empleadoSeleccionado} />
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
