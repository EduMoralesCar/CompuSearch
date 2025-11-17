import { useState, useEffect } from "react";
import { Card, Button, Table, Stack, Spinner, Alert, Pagination } from "react-bootstrap";
import ModalGestionEtiqueta from "../modal/ModalGestionEtiqueta";
import ModalConfirmacion from "../../auxiliar/ModalConfirmacion";
import useEtiquetas from "../../../../navigation/hooks/useEtiquetas";

const PAGE_SIZE = 10; // Tamaño fijo de la página para el controlador

const GestionEtiquetas = () => {
    // 🧩 Estado local para controlar la página actual (0-based index)
    const [currentPage, setCurrentPage] = useState(0); 

    const {
        etiquetasPaginadas, // ✅ Usar esta lista para la tabla
        totalPages,         // ✅ Total de páginas
        // number,           // Se puede usar si quieres sincronizar el estado del hook
        loading,
        error,
        cargarEtiquetasPaginadas, // ✅ Función para cargar la página
        crearEtiqueta,
        actualizarEtiqueta,
        eliminarEtiqueta,
    } = useEtiquetas();

    const [showModal, setShowModal] = useState(false);
    const [etiquetaSeleccionada, setEtiquetaSeleccionada] = useState(null);

    // 🧩 Estados para el modal de confirmación
    const [showConfirm, setShowConfirm] = useState(false);
    const [etiquetaAEliminar, setEtiquetaAEliminar] = useState(null);
    const [eliminando, setEliminando] = useState(false);

    // ✅ Estados para feedback
    const [mensaje, setMensaje] = useState("");
    const [tipoMensaje, setTipoMensaje] = useState("success");


    // 🔄 EFECTO: Carga inicial y recarga al cambiar de página
    useEffect(() => {
        // Cargamos la página actual con el tamaño y ordenamiento deseado
        // Ordenamos por 'nombre' ascendente por defecto.
        cargarEtiquetasPaginadas(currentPage, PAGE_SIZE, 'nombre,asc'); 
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [currentPage]); // Se dispara cada vez que cambia la página actual


    // Abrir modal en modo crear
    const handleCrear = () => {
        setEtiquetaSeleccionada(null);
        setMensaje("");
        setShowModal(true);
    };

    // Abrir modal en modo editar
    const handleEditar = (etiqueta) => {
        setEtiquetaSeleccionada(etiqueta);
        setMensaje("");
        setShowModal(true);
    };

    // Mostrar modal de confirmación
    const confirmarEliminar = (etiqueta) => {
        setEtiquetaAEliminar(etiqueta);
        setShowConfirm(true);
    };

    // Ejecutar eliminación
    const handleEliminarConfirmado = async () => {
        if (!etiquetaAEliminar) return;

        setEliminando(true);
        setMensaje("");
        try {
            await eliminarEtiqueta(etiquetaAEliminar.idEtiqueta);
            
            // ✅ LÓGICA DE RECARGA: Si se eliminó el último elemento de la página,
            // retrocede a la página anterior (mínimo 0).
            const newPage = etiquetasPaginadas.length === 1 && currentPage > 0
                ? currentPage - 1
                : currentPage;
            
            // Recargar la lista con la página ajustada
            await cargarEtiquetasPaginadas(newPage, PAGE_SIZE, 'nombre,asc');
            setCurrentPage(newPage); // Aseguramos que el estado local refleje la nueva página
            
            setTipoMensaje("success");
            setMensaje(`Etiqueta "${etiquetaAEliminar.nombre}" eliminada correctamente.`);
        // eslint-disable-next-line no-unused-vars
        } catch (err) {
            setTipoMensaje("danger");
            setMensaje("Error al eliminar la etiqueta.");
        } finally {
            setEliminando(false);
            setShowConfirm(false);
            setEtiquetaAEliminar(null);
        }
    };

    // Cerrar modal principal
    const handleCloseModal = () => {
        setShowModal(false);
    };

    // Guardar desde el modal (crear o actualizar)
    const handleGuardar = async (nombreEtiqueta) => {
        try {
            if (etiquetaSeleccionada) {
                await actualizarEtiqueta(etiquetaSeleccionada.idEtiqueta, nombreEtiqueta);
                setTipoMensaje("success");
                setMensaje("Etiqueta actualizada correctamente.");
            } else {
                await crearEtiqueta(nombreEtiqueta);
                setTipoMensaje("success");
                setMensaje("Etiqueta creada correctamente.");
            }
            
            // ✅ RECARGA: Recargar la página actual para reflejar el cambio o la adición
            // Si es un nuevo elemento, se cargará en la página que corresponda
            await cargarEtiquetasPaginadas(currentPage, PAGE_SIZE, 'nombre,asc');

        // eslint-disable-next-line no-unused-vars
        } catch (err) {
            setTipoMensaje("danger");
            setMensaje("Error al guardar la etiqueta.");
        } finally {
            setShowModal(false);
        }
    };

    // Helper para la tabla: calcula el índice real del elemento en la lista total
    const getIndex = (index) => currentPage * PAGE_SIZE + index + 1;

    return (
        <>
            <Card>
                <Card.Header as="h5" className="d-flex justify-content-between align-items-center">
                    Gestión de Etiquetas
                    <Button variant="primary" onClick={handleCrear}>
                        Crear Nueva
                    </Button>
                </Card.Header>

                <Card.Body>
                    {loading && (
                        <div className="text-center">
                            <Spinner animation="border" role="status" />
                        </div>
                    )}

                    {/* Mostrar mensaje de éxito o error */}
                    {mensaje && <Alert variant={tipoMensaje}>{mensaje}</Alert>}

                    {/* Si hay error global del hook */}
                    {!mensaje && error && <Alert variant="danger">{error}</Alert>}

                    {!loading && etiquetasPaginadas.length === 0 && (
                        <Alert variant="info">No hay etiquetas registradas.</Alert>
                    )}

                    {!loading && etiquetasPaginadas.length > 0 && (
                        <>
                            <Table striped bordered hover responsive>
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Nombre</th>
                                        <th style={{ width: "150px" }}>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {/* ✅ USAR etiquetasPaginadas en lugar de etiquetas */}
                                    {etiquetasPaginadas.map((et, index) => (
                                        <tr key={et.idEtiqueta}>
                                            {/* Muestra el índice global correcto */}
                                            <td>{getIndex(index)}</td> 
                                            <td>{et.nombre}</td>
                                            <td>
                                                <Stack direction="horizontal" gap={2}>
                                                    <Button
                                                        variant="warning"
                                                        size="sm"
                                                        onClick={() => handleEditar(et)}
                                                    >
                                                        Editar
                                                    </Button>
                                                    <Button
                                                        variant="danger"
                                                        size="sm"
                                                        onClick={() => confirmarEliminar(et)}
                                                    >
                                                        Eliminar
                                                    </Button>
                                                </Stack>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </Table>

                            {/* ✅ COMPONENTE DE PAGINACIÓN */}
                            {totalPages > 1 && (
                                <div className="d-flex justify-content-center mt-4">
                                    <Pagination>
                                        <Pagination.First 
                                            onClick={() => setCurrentPage(0)} 
                                            disabled={currentPage === 0} 
                                        />
                                        <Pagination.Prev 
                                            onClick={() => setCurrentPage(currentPage - 1)} 
                                            disabled={currentPage === 0} 
                                        />

                                        {/* Renderiza un rango limitado de botones de página */}
                                        {[...Array(totalPages)].map((_, index) => (
                                            <Pagination.Item 
                                                key={index} 
                                                active={index === currentPage}
                                                onClick={() => setCurrentPage(index)}
                                            >
                                                {index + 1}
                                            </Pagination.Item>
                                        ))}

                                        <Pagination.Next 
                                            onClick={() => setCurrentPage(currentPage + 1)} 
                                            disabled={currentPage === totalPages - 1} 
                                        />
                                        <Pagination.Last 
                                            onClick={() => setCurrentPage(totalPages - 1)} 
                                            disabled={currentPage === totalPages - 1} 
                                        />
                                    </Pagination>
                                </div>
                            )}
                        </>
                    )}
                </Card.Body>
            </Card>

            {/* Modal para crear/editar */}
            <ModalGestionEtiqueta
                show={showModal}
                handleClose={handleCloseModal}
                handleGuardar={handleGuardar}
                etiqueta={etiquetaSeleccionada}
            />

            {/* Modal de confirmación */}
            <ModalConfirmacion
                show={showConfirm}
                onHide={() => setShowConfirm(false)}
                titulo="Eliminar Etiqueta"
                mensaje={`¿Estás seguro de eliminar la etiqueta "${
                    etiquetaAEliminar?.nombre || ""
                }"? Se eliminará de las tiendas que la contengan.`}
                onConfirmar={handleEliminarConfirmado}
                textoConfirmar={eliminando ? "Eliminando..." : "Eliminar"}
                variantConfirmar="danger"
                disabled={eliminando}
            />
        </>
    );
};

export default GestionEtiquetas;