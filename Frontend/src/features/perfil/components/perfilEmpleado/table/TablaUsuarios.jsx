import { Table, Button, Spinner, Stack } from "react-bootstrap";

const TablaUsuarios = ({ usuarios, onVerInfo, onToggleActivo, isTogglingId }) => {

    return (
        <Table striped bordered hover responsive size="sm" className="mt-3">
            <thead>
                <tr>
                    <th>ID</th>
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
                            <td>
                                <small className="text-muted">
                                    {new Date(user.fechaRegistro).toLocaleDateString()}
                                </small>
                            </td>
                            <td className="text-center">{user.cantidadSolicitudes || 0}</td>
                            <td className="text-center">{user.cantidadBuilds || 0}</td>
                            <td className="text-center">{user.cantidadIncidentes || 0}</td>

                            <td>
                                <Stack direction="horizontal" gap={2}>
                                    <Button variant="info" size="sm" onClick={() => onVerInfo(user)}>Info</Button>

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

export default TablaUsuarios;