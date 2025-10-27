import { Card, Table, Button } from "react-bootstrap";

//  Datos de Ejemplo ( Jesus borra esta webada) 
const mockIncidencias = [
    {
        id: 501,
        usuarioReporta: "ana@cliente.com",
        fecha: "2025-10-24",
        descripcion: "El producto 'Teclado Mecánico RGB' tiene la descripción incorrecta, muestra fotos de un mouse."
    },
    {
        id: 502,
        usuarioReporta: "luis@cliente.com",
        fecha: "2025-10-23",
        descripcion: "El botón de 'comprar ahora' no funciona en la página de la Laptop 'OmegaTech X1'."
    },
    {
        id: 503,
        usuarioReporta: "carlos@empleado.com",
        fecha: "2025-10-22",
        descripcion: "No puedo actualizar el stock del producto ID: 88A. Me da error 500."
    }
];

const GestionIncidencias = () => {

    // Función de acción (solo visual - Jesus Chambea)
    const handleEliminar = (id) => {
        alert(`(Visual) Incidencia ID ${id} ELIMINADA`);
    };

    return (
        <Card>
            <Card.Header as="h5">Gestión de Incidencias</Card.Header>
            <Card.Body>
                <Table striped bordered hover responsive>
                    <thead>
                        <tr>
                            <th style={{ width: "180px" }}>Reportado por</th>
                            <th style={{ width: "120px" }}>Fecha</th>
                            <th>Descripción de la Incidencia</th>
                            <th style={{ width: "120px" }}>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        {mockIncidencias.map((inc) => (
                            <tr key={inc.id}>
                                <td>{inc.usuarioReporta}</td>
                                <td>{inc.fecha}</td>
                                <td>{inc.descripcion}</td>
                                <td>
                                    <Button 
                                        variant="danger" 
                                        size="sm"
                                        onClick={() => handleEliminar(inc.id)}
                                    >
                                        Eliminar
                                    </Button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>

                {/* Mensaje si no hay incidencias */}
                {mockIncidencias.length === 0 && (
                    <p className="text-muted text-center">No hay incidencias reportadas.</p>
                )}
            </Card.Body>
        </Card>
    );
};

export default GestionIncidencias;