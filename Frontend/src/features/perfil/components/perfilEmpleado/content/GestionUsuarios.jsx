import { useState } from "react";
import { Card, Tabs, Tab, Table, Button, Stack } from "react-bootstrap";
import ModalInfoUsuario from "../modal/ModalInfoUsuario";

//Datos de Ejemplo (Jesus Chambea)
const mockUsuarios = [
    { id: 101, nombre: "Ana García", email: "ana@cliente.com", rol: "usuario", activo: true },
    { id: 102, nombre: "Luis Paz", email: "luis@cliente.com", rol: "usuario", activo: false },
];

const mockEmpleados = [
    { id: 201, nombre: "Carlos Solis", email: "carlos@empleado.com", rol: "empleado", cargo: "Soporte" },
    { id: 202, nombre: "Maria Luna", email: "maria@empleado.com", rol: "empleado", cargo: "Ventas" },
];

const mockAdmins = [
    { id: 301, nombre: "Admin General", email: "admin@compusearch.com", rol: "admin" },
];


const GestionUsuarios = () => {
    // Estado para el modal de info
    const [showModal, setShowModal] = useState(false);
    // Estado para pasar los datos del usuario al modal
    const [usuarioSeleccionado, setUsuarioSeleccionado] = useState(null);

    // Función para abrir el modal y cargar al usuario
    const handleVerInfo = (usuario) => {
        setUsuarioSeleccionado(usuario);
        setShowModal(true);
    };

    // Función para cerrar el modal
    const handleCloseModal = () => {
        setShowModal(false);
        setUsuarioSeleccionado(null);
    };

    // Funciones de acción (solo visuales)
    const handleDeshabilitar = (id) => alert(`(Visual) Deshabilitar usuario ID: ${id}`);
    const handleEditar = (id) => alert(`(Visual) Editar usuario ID: ${id}`);
    const handleEliminar = (id) => alert(`(Visual) Eliminar usuario ID: ${id}`);
    const handleVerProductos = (id) => alert(`(Visual) Ver productos de usuario ID: ${id}`);


    return (
        <>
            <Card>
                <Card.Header as="h5">Gestión de Usuarios</Card.Header>
                <Card.Body>
                    <Tabs defaultActiveKey="usuarios" id="tabs-gestion-usuarios" className="mb-3" fill>
                        
                        {/* Pestaña USUARIOS (Jesus Chambea) */}
                        <Tab eventKey="usuarios" title={`Usuarios (${mockUsuarios.length})`}>
                            <TablaUsuarios
                                usuarios={mockUsuarios}
                                onVerInfo={handleVerInfo}
                                onDeshabilitar={handleDeshabilitar}
                                onVerProductos={handleVerProductos}
                            />
                        </Tab>

                        {/* Pestaña EMPLEADOS (Jesus Chambea) */}
                        <Tab eventKey="empleados" title={`Empleados (${mockEmpleados.length})`}>
                            <TablaUsuarios
                                usuarios={mockEmpleados}
                                onVerInfo={handleVerInfo}
                                onDeshabilitar={handleDeshabilitar}
                                onEditar={handleEditar}
                                onEliminar={handleEliminar}
                            />
                        </Tab>

                        {/* Pestaña ADMINISTRADORES (Jesus Chambea) */}
                        <Tab eventKey="admins" title={`Admins (${mockAdmins.length})`}>
                            <TablaUsuarios
                                usuarios={mockAdmins}
                                onVerInfo={handleVerInfo}
                            />
                        </Tab>

                    </Tabs>
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

// Componente interno para renderizar las tablas (para no repetir código, Jesus Chambea)
const TablaUsuarios = ({ usuarios, ...actions }) => {
    
    // Extraemos las funciones de 'actions' para saber qué botones mostrar
    const { onVerInfo, onDeshabilitar, onEditar, onEliminar, onVerProductos } = actions;

    return (
        <Table striped bordered hover responsive size="sm">
            <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Email</th>
                    <th>Info Adicional</th>
                    <th style={{ width: "250px" }}>Acciones</th>
                </tr>
            </thead>
            <tbody>
                {usuarios.map((user) => (
                    <tr key={user.id}>
                        <td>{user.nombre}</td>
                        <td>{user.email}</td>
                        <td>
                            {user.rol === 'usuario' && `Activo: ${user.activo ? 'Sí' : 'No'}`}
                            {user.rol === 'empleado' && `Cargo: ${user.cargo}`}
                        </td>
                        <td>
                            <Stack direction="horizontal" gap={2}>
                                {onVerInfo && <Button variant="info" size="sm" onClick={() => onVerInfo(user)}>Info</Button>}
                                {onDeshabilitar && <Button variant="secondary" size="sm" onClick={() => onDeshabilitar(user.id)}>Deshabilitar</Button>}
                                {onEditar && <Button variant="warning" size="sm" onClick={() => onEditar(user.id)}>Editar</Button>}
                                {onEliminar && <Button variant="danger" size="sm" onClick={() => onEliminar(user.id)}>Eliminar</Button>}
                                {onVerProductos && <Button variant="success" size="sm" onClick={() => onVerProductos(user.id)}>Productos</Button>}
                            </Stack>
                        </td>
                    </tr>
                ))}
            </tbody>
        </Table>
    );
};

export default GestionUsuarios;