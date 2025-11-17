import { useState, useEffect, useCallback } from "react";
import { Card, Table, Button, Stack, Alert, Spinner, Form, InputGroup } from "react-bootstrap";
// Importaciones corregidas para apuntar a la misma carpeta (o un path simple)
import useEmpleadosApi from "../../../hooks/useEmpleados";

import React from 'react';
import { Modal, ListGroup } from 'react-bootstrap';

const formatDate = (localDateTimeString) => {
    if (!localDateTimeString) return 'N/A';
    const date = new Date(localDateTimeString);
    if (isNaN(date)) return localDateTimeString;
    return new Intl.DateTimeFormat('es-ES', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
    }).format(date);
};

/**
 * Componente Modal para crear o actualizar la información de un Empleado.
 * @param {object} props - Propiedades del componente.
 * @param {boolean} props.show - Controla la visibilidad del modal.
 * @param {function} props.handleClose - Función para cerrar el modal.
 * @param {import('../../../hooks/useEmpleadosApi').EmpleadoResponse | null} props.empleadoAEditar - Objeto Empleado a editar o null para crear.
 * @param {function} props.onCreateOrUpdate - Función a ejecutar al guardar (recibe EmpleadoRequest).
 * @param {boolean} props.isSubmitting - Estado de carga de la operación.
 * @param {string | null} props.submitError - Mensaje de error al enviar.
 */
const ModalFormularioEmpleado = ({
    show,
    handleClose,
    empleadoAEditar,
    onCreateOrUpdate,
    isSubmitting,
    submitError
}) => {
    // Estado inicial del formulario
    const [formData, setFormData] = useState({
        username: '',
        nombre: '',
        apellido: '',
        email: '',
        rol: 'ADMIN', // Valor por defecto
        password: '', // Campo solo necesario para la creación o cambio de password
    });
    // eslint-disable-next-line no-unused-vars
    const [passwordRequired, setPasswordRequired] = useState(false); // Para mostrar/ocultar el campo password

    // Sincronizar el estado del formulario con el empleado a editar
    useEffect(() => {
        if (empleadoAEditar) {
            setFormData({
                username: empleadoAEditar.username || '',
                nombre: empleadoAEditar.nombre || '',
                apellido: empleadoAEditar.apellido || '',
                email: empleadoAEditar.email || '',
                rol: empleadoAEditar.rol || 'ADMIN',
                password: '', // Siempre se reinicia el campo password al editar
            });
            // La contraseña no es obligatoria al editar (a menos que se quiera cambiar)
            setPasswordRequired(false);
        } else {
            // Valores por defecto para creación
            setFormData({
                username: '',
                nombre: '',
                apellido: '',
                email: '',
                rol: 'ADMIN',
                password: '',
            });
            // La contraseña es obligatoria al crear
            setPasswordRequired(true);
        }
    }, [empleadoAEditar, show]); // Re-ejecutar al cambiar el empleado o al mostrar el modal

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        // Validar campos obligatorios
        if (!formData.username || !formData.nombre || !formData.apellido || !formData.email || !formData.rol) {
            alert('Por favor, complete todos los campos obligatorios.');
            return;
        }

        // Si estamos creando o si el campo password fue llenado en edición, lo incluimos
        const dataToSend = {
            ...formData,
        };

        // Si estamos editando y el campo password está vacío, lo eliminamos del objeto
        // CREAR: la contraseña es obligatoria
        if (!empleadoAEditar && formData.password.trim() === '') {
            alert('La contraseña es obligatoria para un nuevo empleado.');
            return;
        }

        // EDITAR: si password está vacío NO enviarlo
        if (empleadoAEditar && formData.password.trim() === '') {
            delete dataToSend.password;
        }

        // Ejecutar la función de creación o actualización pasada por props
        onCreateOrUpdate(dataToSend);
    };

    const title = empleadoAEditar ?
        `Editar Empleado: ${empleadoAEditar.username}` :
        'Crear Nuevo Empleado';

    // Función de cierre que se asegura de limpiar el formulario
    const handleCloseInternal = () => {
        if (!isSubmitting) {
            handleClose();
        }
    };


    return (
        <Modal show={show} onHide={handleCloseInternal} centered>
            <Modal.Header closeButton>
                <Modal.Title>{title}</Modal.Title>
            </Modal.Header>
            <Form onSubmit={handleSubmit}>
                <Modal.Body>
                    {submitError && <Alert variant="danger">{submitError}</Alert>}

                    {/* Username */}
                    <Form.Group className="mb-3" controlId="formUsername">
                        <Form.Label>Username (*)</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Ingrese nombre de usuario"
                            name="username"
                            value={formData.username}
                            onChange={handleChange}
                            required
                            disabled={isSubmitting}
                        />
                    </Form.Group>

                    {/* Nombre */}
                    <Form.Group className="mb-3" controlId="formNombre">
                        <Form.Label>Nombre (*)</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Ingrese su nombre"
                            name="nombre"
                            value={formData.nombre}
                            onChange={handleChange}
                            required
                            disabled={isSubmitting}
                        />
                    </Form.Group>

                    {/* Apellido */}
                    <Form.Group className="mb-3" controlId="formApellido">
                        <Form.Label>Apellido (*)</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Ingrese su apellido"
                            name="apellido"
                            value={formData.apellido}
                            onChange={handleChange}
                            required
                            disabled={isSubmitting}
                        />
                    </Form.Group>

                    {/* Email */}
                    <Form.Group className="mb-3 col-md-6" controlId="formEmail">
                        <Form.Label>Email (*)</Form.Label>
                        <Form.Control
                            type="email"
                            placeholder="name@example.com"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            required
                            disabled={isSubmitting}
                        />
                    </Form.Group>

                    {/* Rol */}
                    <Form.Group className="mb-3" controlId="formRol">
                        <Form.Label>Rol (*)</Form.Label>
                        <Form.Select
                            name="rol"
                            value={formData.rol}
                            onChange={handleChange}
                            disabled={isSubmitting}
                        >
                            <option value="ADMIN">ADMIN</option>
                            <option value="MONITORIO">MONITORIO</option>
                            <option value="SOPORTE">SOPORTE</option>
                            {/* Puedes añadir más roles si tu backend los soporta */}
                        </Form.Select>
                    </Form.Group>

                    {/* Contraseña */}
                    <Form.Group className="mb-3" controlId="formPassword">
                        <Form.Label>
                            Contraseña {empleadoAEditar ? '(Dejar vacío para no cambiar)' : '(*)'}
                        </Form.Label>
                        <Form.Control
                            type="password"
                            placeholder={empleadoAEditar ? '••••••••' : 'Ingrese contraseña'}
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            required={!empleadoAEditar} // Solo requerido si es creación
                            disabled={isSubmitting}
                        />
                        {empleadoAEditar && (
                            <Form.Text className="text-muted">
                                Si no desea cambiar la contraseña, déjelo en blanco.
                            </Form.Text>
                        )}
                    </Form.Group>

                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseInternal} disabled={isSubmitting}>
                        Cancelar
                    </Button>
                    <Button variant="primary" type="submit" disabled={isSubmitting}>
                        {isSubmitting ? (
                            <Spinner animation="border" size="sm" className="me-2" />
                        ) : (
                            empleadoAEditar ? 'Guardar Cambios' : 'Crear Empleado'
                        )}
                    </Button>
                </Modal.Footer>
            </Form>
        </Modal>
    );
};

const ModalInfoEmpleado = ({ show, handleClose, empleado }) => {
    // Evita renderizar si no hay empleado seleccionado
    if (!empleado) {
        return null;
    }

    return (
        <Modal show={show} onHide={handleClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>Detalles del Empleado: {empleado.username}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <ListGroup variant="flush">
                    <ListGroup.Item><strong>ID Empleado:</strong> {empleado.idUsuario}</ListGroup.Item>
                    <ListGroup.Item><strong>Nombre Completo:</strong> {empleado.nombre} {empleado.apellido}</ListGroup.Item>
                    <ListGroup.Item><strong>Username:</strong> {empleado.username}</ListGroup.Item>
                    <ListGroup.Item><strong>Email:</strong> {empleado.email}</ListGroup.Item>
                    <ListGroup.Item><strong>Rol:</strong> <span className="badge bg-primary">{empleado.rol}</span></ListGroup.Item>
                    <ListGroup.Item>
                        <strong>Estado:</strong>
                        <span className={`badge bg-${empleado.activo ? 'success' : 'danger'} ms-2`}>
                            {empleado.activo ? 'Activo' : 'Inactivo'}
                        </span>
                    </ListGroup.Item>
                    <ListGroup.Item><strong>Fecha de Asignación:</strong> {formatDate(empleado.fechaAsignacion)}</ListGroup.Item>
                </ListGroup>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Cerrar
                </Button>
            </Modal.Footer>
        </Modal>
    );
};
// ... [Fin del código de ModalInfoEmpleado] ...


// Componente interno para renderizar la tabla de empleados (Ahora con botón de Editar)
const TablaEmpleados = ({ empleados, onVerInfo, onToggleActivo, onEditar, isTogglingId }) => { // <--- Agregado onEditar

    return (
        <Table striped bordered hover responsive size="sm" className="mt-3">
            <thead>
                <tr>
                    <th>ID Empleado</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Nombre</th>
                    <th>Rol</th>
                    <th>Estado</th>
                    <th style={{ width: "220px" }}>Acciones</th>
                </tr>
            </thead>
            <tbody>
                {empleados.length === 0 ? (
                    <tr>
                        <td colSpan="7" className="text-center">No se encontraron empleados.</td>
                    </tr>
                ) : (
                    empleados.map((empleado) => (
                        <tr key={empleado.idUsuario}>
                            <td>{empleado.idUsuario}</td>
                            <td>{empleado.username}</td>
                            <td>{empleado.email}</td>
                            <td>{empleado.nombre}</td>
                            <td>
                                <span className="badge bg-primary">{empleado.rol}</span>
                            </td>
                            <td>
                                <span className={`badge bg-${empleado.activo ? 'success' : 'danger'}`}>
                                    {empleado.activo ? 'Activo' : 'Inactivo'}
                                </span>
                            </td>

                            <td>
                                <Stack direction="horizontal" gap={2}>
                                    <Button variant="info" size="sm" onClick={() => onVerInfo(empleado)}>Info</Button>

                                    <Button variant="warning" size="sm" onClick={() => onEditar(empleado)}>Editar</Button>

                                    <Button
                                        variant={empleado.activo ? 'secondary' : 'success'}
                                        size="sm"
                                        onClick={() => onToggleActivo(empleado.idUsuario, empleado.activo)}
                                        disabled={isTogglingId === empleado.idUsuario}
                                    >
                                        {isTogglingId === empleado.idUsuario ? (
                                            <Spinner animation="border" size="sm" />
                                        ) : (
                                            empleado.activo ? 'Deshabilitar' : 'Activar'
                                        )}
                                    </Button>
                                </Stack>
                            </td>
                        </tr>
                    ))
                )}
            </tbody>
        </Table>
    );
};

// Componente principal
const GestionEmpleados = () => {
    // Usamos el hook de empleados
    const {
        getEmpleados,
        updateEmpleadoActivo,
        createEmpleado,        // <--- NUEVAS FUNCIONES
        updateEmpleado,        // <--- NUEVAS FUNCIONES
        isLoading,
        error,
        empleados: empleadosFromHook
    } = useEmpleadosApi();

    // Estado local para la lista, sincronizado con el hook
    const [empleados, setEmpleados] = useState(empleadosFromHook);
    useEffect(() => {
        setEmpleados(empleadosFromHook);
    }, [empleadosFromHook]);

    const [isTogglingId, setIsTogglingId] = useState(null);
    const [showModalInfo, setShowModalInfo] = useState(false); // Modal Info
    const [showModalForm, setShowModalForm] = useState(false); // <--- NUEVO: Modal Crear/Editar
    const [empleadoSeleccionado, setEmpleadoSeleccionado] = useState(null);
    const [empleadoAEditar, setEmpleadoAEditar] = useState(null); // <--- NUEVO: Empleado a editar
    const [gestionError, setGestionError] = useState(null);
    const [formSubmitError, setFormSubmitError] = useState(null); // <--- NUEVO: Error específico del formulario
    const [isSubmitting, setIsSubmitting] = useState(false);    // <--- NUEVO: Estado de carga para el formulario

    // Estado para el término de búsqueda
    const [searchTerm, setSearchTerm] = useState('');

    /**
     * Función para obtener los datos de la API.
     * @param {string} search - El término de búsqueda.
     */
    const fetchEmpleados = useCallback(async (search) => {
        setGestionError(null);
        await getEmpleados(0, 50, search);
        // El hook actualiza el estado 'empleadosFromHook', que a su vez actualiza 'empleados'
    }, [getEmpleados]);

    // Cargar empleados al montar el componente (Run once - carga inicial)
    useEffect(() => {
        fetchEmpleados('');
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    // Búsqueda y Limpieza (Se mantienen igual)
    const handleInputChange = (e) => {
        setSearchTerm(e.target.value);
    };

    const handleSearchSubmit = (e) => {
        if (e) e.preventDefault();
        fetchEmpleados(searchTerm);
    };

    const handleClearSearch = () => {
        setSearchTerm('');
        fetchEmpleados('');
    };

    // Toggle Activo (Se mantiene igual)
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

    // --- Funciones para Modal de Info ---
    const handleVerInfo = (empleado) => {
        setEmpleadoSeleccionado(empleado);
        setShowModalInfo(true);
    };

    const handleCloseModalInfo = () => {
        setShowModalInfo(false);
        setEmpleadoSeleccionado(null);
    };

    // --- Funciones para Modal de Crear/Editar ---

    // Abre el modal para crear
    const handleCrear = () => {
        setEmpleadoAEditar(null); // Asegura que está en modo Creación
        setFormSubmitError(null);
        setShowModalForm(true);
    };

    // Abre el modal para editar
    const handleEditar = (empleado) => {
        setEmpleadoAEditar(empleado);
        setFormSubmitError(null);
        setShowModalForm(true);
    };

    // Cierra el modal de formulario
    const handleCloseModalForm = () => {
        setShowModalForm(false);
        setEmpleadoAEditar(null);
        setFormSubmitError(null);
    };

    /**
     * Maneja el envío del formulario para crear o actualizar.
     */
    const handleCreateOrUpdate = async (empleadoData) => {
        setIsSubmitting(true);
        setFormSubmitError(null);

        let response;
        if (empleadoAEditar) {
            // Actualizar
            response = await updateEmpleado(empleadoAEditar.idUsuario, empleadoData);
        } else {
            // Crear
            response = await createEmpleado(empleadoData);
        }

        if (response.success) {
            handleCloseModalForm();
            // Recarga la lista para mostrar el nuevo/empleado actualizado.
            fetchEmpleados(searchTerm);
        } else {
            setFormSubmitError(response.error || 'Ocurrió un error desconocido al guardar el empleado.');
        }

        setIsSubmitting(false);
    };

    // Determina si hay una operación de carga que no sea solo el toggle de un elemento
    const isGlobalLoading = isLoading && isTogglingId === null && !isSubmitting;

    return (
        <>
            <Card>
                <Card.Header as="h5">Gestión de Empleados</Card.Header>
                <Card.Body>

                    {/* Botón de Creación */} {/* <--- NUEVO BOTÓN */}
                    <div className="d-flex justify-content-end mb-3">
                        <Button variant="success" onClick={handleCrear} disabled={isGlobalLoading}>
                            + Crear Nuevo Empleado
                        </Button>
                    </div>

                    {/* Componente de Búsqueda (Se mantiene igual) */}
                    <Form onSubmit={handleSearchSubmit} className="mb-4">
                        <InputGroup>
                            <Form.Control
                                type="text"
                                placeholder="Buscar por Username..."
                                value={searchTerm}
                                onChange={handleInputChange}
                                disabled={isGlobalLoading && empleados.length === 0}
                            />
                            {/* Botón de Búsqueda */}
                            <Button
                                variant="primary"
                                type="submit"
                                disabled={isGlobalLoading}
                            >
                                {isGlobalLoading ? <Spinner animation="border" size="sm" className="me-2" /> : 'Buscar'}
                            </Button>
                            {/* Botón de Limpiar Búsqueda */}
                            <Button
                                variant="outline-secondary"
                                onClick={handleClearSearch}
                                disabled={isGlobalLoading || searchTerm === ''}
                            >
                                Limpiar
                            </Button>
                        </InputGroup>
                    </Form>

                    {/* Mensajes de Error y Loading */}
                    {(error || gestionError) && (
                        <Alert variant="danger">{error || gestionError}</Alert>
                    )}

                    {isGlobalLoading && empleados.length === 0 && (
                        <div className="text-center p-5">
                            <Spinner animation="border" role="status" className="me-2" />
                            Cargando empleados...
                        </div>
                    )}

                    {/* Tabla de Empleados */}
                    {!(isGlobalLoading && empleados.length === 0) && (
                        <TablaEmpleados
                            empleados={empleados}
                            onVerInfo={handleVerInfo}
                            onToggleActivo={handleToggleActivo}
                            onEditar={handleEditar}
                            isTogglingId={isTogglingId}
                        />
                    )}

                    {/* Botón para recargar */}
                    <div className="d-grid gap-2 mt-3">
                        <Button
                            variant="outline-secondary"
                            onClick={handleSearchSubmit}
                            disabled={isGlobalLoading}
                        >
                            {isGlobalLoading ? 'Recargando...' : 'Recargar Lista'}
                        </Button>
                    </div>

                </Card.Body>
            </Card>

            {/* Modal para ver la info (se mantiene) */}
            <ModalInfoEmpleado
                show={showModalInfo}
                handleClose={handleCloseModalInfo}
                empleado={empleadoSeleccionado}
            />

            {/* Modal para crear o editar (NUEVO) */}
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