import { Table, Button, Spinner, Stack } from "react-bootstrap";

const TablaUsuarios = ({ usuarios, onVerInfo, onToggleActivo, isTogglingId }) => {

    return (
        <Table striped bordered hover responsive className="mt-3">
            <thead>
                <tr>
                    <th style={{ width: "60px" }}>ID</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th style={{ width: "100px" }}>Estado</th>
                    <th style={{ width: "120px" }}>Registro</th>
                    <th className="text-center" style={{ width: "100px" }}>Solicitudes</th>
                    <th className="text-center" style={{ width: "80px" }}>Builds</th>
                    <th className="text-center" style={{ width: "100px" }}>Incidentes</th>
                    <th style={{ width: "250px" }}>Acciones</th>
                </tr>
            </thead>
            <tbody>
                {usuarios.length === 0 ? (
                    <tr>
                        <td colSpan="9" className="text-center py-4">No hay usuarios registrados.</td>
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

                            <td className="px-3">
                                <div className="d-flex justify-content-center align-items-center">
                                    <Button
                                        variant="info"
                                        size="sm"
                                        onClick={() => onVerInfo(user)}
                                        style={{ minWidth: "70px" }}
                                        className="me-3"
                                    >
                                        Info
                                    </Button>

                                    <Button
                                        variant={user.activo ? 'secondary' : 'success'}
                                        size="sm"
                                        onClick={() => onToggleActivo(user.idUsuario, user.activo)}
                                        disabled={isTogglingId === user.idUsuario}
                                        style={{ minWidth: "100px" }}
                                    >
                                        {isTogglingId === user.idUsuario ? (
                                            <Spinner animation="border" size="sm" />
                                        ) : (
                                            user.activo ? 'Deshabilitar' : 'Activar'
                                        )}
                                    </Button>
                                </div>
                            </td>
                        </tr>
                    ))
                )}
            </tbody>
        </Table>
    );
};

export default TablaUsuarios;