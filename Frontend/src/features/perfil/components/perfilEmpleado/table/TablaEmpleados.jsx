import { Table, Spinner, Badge, Button, Stack, Form } from "react-bootstrap";

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

export default TablaEmpleados;