import { useState, useEffect, useCallback } from "react";
// Se importa Form e InputGroup de React-Bootstrap para el campo de búsqueda
import { Card, Table, Button, Stack, Alert, Spinner, Form, InputGroup } from "react-bootstrap";
import ModalInfoUsuario from "../modal/ModalInfoUsuario"; 
import { useUsuario } from "../../../hooks/useUsuario"; 

// Componente interno para renderizar la tabla de usuarios
const TablaUsuarios = ({ usuarios, onVerInfo, onToggleActivo, isTogglingId }) => {

    return (
        <Table striped bordered hover responsive size="sm" className="mt-3">
            <thead>
                <tr>
                    <th>ID Usuario</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Estado</th>
                    <th>Registro</th>
                    <th>Solicitudes</th>
                    <th>Builds</th>
                    <th>Incidentes</th>
                    <th style={{ width: "200px" }}>Acciones</th> 
                </tr>
            </thead>
            <tbody>
                {usuarios.length === 0 ? (
                    <tr>
                        <td colSpan="9" className="text-center">No hay usuarios registrados.</td> 
                    </tr>
                ) : (
                    usuarios.map((user) => (
                        <tr key={user.idUsuario}>
                            <td>{user.idUsuario}</td>
                            <td>{user.username}</td>
                            <td>{user.email}</td>
                            <td>
                                <span className={`badge bg-${user.activo ? 'success' : 'danger'}`}>
                                    {user.activo ? 'Activo' : 'Inactivo'}
                                </span>
                            </td>
                            {/* Columna de Registro */}
                            <td>
                                <small className="text-muted">
                                    {new Date(user.fechaRegistro).toLocaleDateString()}
                                </small>
                            </td>
                            {/* Nuevas Columnas de Conteo */}
                            <td className="text-center">{user.cantidadSolicitudes || 0}</td>
                            <td className="text-center">{user.cantidadBuilds || 0}</td>
                            <td className="text-center">{user.cantidadIncidentes || 0}</td>
                            
                            {/* Columna de Acciones */}
                            <td>
                                <Stack direction="horizontal" gap={2}>
                                    {/* Botón Info */}
                                    <Button variant="info" size="sm" onClick={() => onVerInfo(user)}>Info</Button>
                                    
                                    {/* Botón de toggle Activar/Desactivar */}
                                    <Button
                                        variant={user.activo ? 'secondary' : 'success'}
                                        size="sm"
                                        onClick={() => onToggleActivo(user.idUsuario, user.activo)}
                                        disabled={isTogglingId === user.idUsuario}
                                    >
                                        {isTogglingId === user.idUsuario ? (
                                            <Spinner animation="border" size="sm" />
                                        ) : (
                                            user.activo ? 'Deshabilitar' : 'Activar'
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
const GestionUsuarios = () => {
    const { obtenerUsuariosPaginados, actualizarEstadoActivo, loading, error } = useUsuario();
    
    const [usuarios, setUsuarios] = useState([]);
    const [isTogglingId, setIsTogglingId] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [usuarioSeleccionado, setUsuarioSeleccionado] = useState(null);
    const [gestionError, setGestionError] = useState(null);
    
    // Estado para el término de búsqueda
    const [searchTerm, setSearchTerm] = useState('');

    /**
     * Función para obtener los datos de la API, requiere que se le pase el término de búsqueda.
     * @param {string} search - El término de búsqueda a aplicar.
     */
    const fetchUsuarios = useCallback(async (search) => {
        setGestionError(null);
        // El 'search' (username) se pasa como parámetro al hook
        const response = await obtenerUsuariosPaginados(0, 50, search); 
        if (response.success) {
            setUsuarios(response.data.content);
        } else {
            setGestionError(response.error || "Error al cargar la lista de usuarios.");
        }
    }, [obtenerUsuariosPaginados]); 

    // Cargar usuarios al montar el componente (Run once - carga inicial)
    useEffect(() => {
        // Carga inicial con término vacío para obtener todos los usuarios
        fetchUsuarios(''); 
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []); 

    // Función para manejar el cambio en el input (solo actualiza el estado, NO busca)
    const handleInputChange = (e) => {
        setSearchTerm(e.target.value);
    };

    // Función para manejar la búsqueda explícita (al hacer clic en Buscar o Enter)
    const handleSearchSubmit = (e) => {
        if (e) e.preventDefault();
        fetchUsuarios(searchTerm);
    };

    // Función para resetear la búsqueda
    const handleClearSearch = () => {
        setSearchTerm('');
        fetchUsuarios(''); // Cargar lista completa
    };

    // Función para manejar el toggle de Activo/Inactivo (estable)
    const handleToggleActivo = async (idUsuario, currentActivo) => {
        setIsTogglingId(idUsuario);
        setGestionError(null);
        
        const nuevoEstado = !currentActivo;
        const response = await actualizarEstadoActivo(idUsuario, nuevoEstado);

        if (response.success) {
            // Actualizar el estado local para reflejar el cambio
            setUsuarios(prev => 
                prev.map(u => u.idUsuario === idUsuario ? { ...u, activo: nuevoEstado } : u)
            );
            console.log(`Usuario ID ${idUsuario} actualizado a activo=${nuevoEstado}`);
        } else {
            setGestionError(response.error || `Error al cambiar el estado activo del usuario ID ${idUsuario}.`);
        }
        
        setIsTogglingId(null);
    };

    // Función para abrir el modal (estable)
    const handleVerInfo = (usuario) => {
        setUsuarioSeleccionado(usuario);
        setShowModal(true);
    };

    // Función para cerrar el modal (estable)
    const handleCloseModal = () => {
        setShowModal(false);
        setUsuarioSeleccionado(null);
    };

    return (
        <>
            <Card>
                <Card.Header as="h5">Gestión de Usuarios Clientes</Card.Header>
                <Card.Body>
                    
                    {/* Componente de Búsqueda (Búsqueda por Submit/Click) */}
                    <Form onSubmit={handleSearchSubmit} className="mb-4">
                        <InputGroup>
                            <Form.Control
                                type="text"
                                placeholder="Buscar por Username..."
                                value={searchTerm}
                                onChange={handleInputChange} // <--- SOLO ACTUALIZA EL ESTADO
                                disabled={loading && usuarios.length === 0}
                            />
                            {/* Botón de Búsqueda (activado por el submit del Form) */}
                            <Button 
                                variant="primary" // Cambiado a 'primary' para más visibilidad
                                type="submit" 
                                disabled={loading}
                            >
                                {loading && isTogglingId === null ? <Spinner animation="border" size="sm" className="me-2" /> : 'Buscar'}
                            </Button>
                            {/* Botón de Limpiar Búsqueda */}
                            <Button 
                                variant="outline-secondary" 
                                onClick={handleClearSearch}
                                disabled={loading || searchTerm === ''}
                            >
                                Limpiar
                            </Button>
                        </InputGroup>
                    </Form>

                    {/* Mensajes de Error y Loading */}
                    {(error || gestionError) && (
                        <Alert variant="danger">{error || gestionError}</Alert>
                    )}

                    {loading && usuarios.length === 0 && (
                        <div className="text-center p-5">
                            <Spinner animation="border" role="status" className="me-2"/>
                            Cargando usuarios...
                        </div>
                    )}
                    
                    {/* Tabla de Usuarios Clientes */}
                    {!(loading && usuarios.length === 0) && (
                        <TablaUsuarios
                            usuarios={usuarios}
                            onVerInfo={handleVerInfo}
                            onToggleActivo={handleToggleActivo}
                            isTogglingId={isTogglingId}
                        />
                    )}

                    {/* Botón para recargar */}
                    <div className="d-grid gap-2 mt-3">
                        <Button 
                            variant="outline-secondary" 
                            onClick={handleSearchSubmit} // Ahora llama a handleSearchSubmit para recargar con el término actual
                            disabled={loading}
                        >
                            {loading && isTogglingId === null ? 'Recargando...' : 'Recargar Lista'}
                        </Button>
                    </div>

                </Card.Body>
            </Card>

            {/* Modal para ver la info */}
            <ModalInfoUsuario 
                show={showModal}
                handleClose={handleCloseModal}
                usuario={usuarioSeleccionado}
            />
        </>
    );
};

export default GestionUsuarios;