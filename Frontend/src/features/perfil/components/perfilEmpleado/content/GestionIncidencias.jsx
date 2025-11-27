import { useEffect, useState } from "react";
import { Card, Table, Button, Spinner, Alert, Modal } from "react-bootstrap";
import { useIncidentes } from "../../../hooks/useIncidentes";
import ModalConfirmacion from "../../../components/auxiliar/ModalConfirmacion";
import HeaderBase from "../auxiliar/HeaderBase";
import PaginacionBase from "../auxiliar/PaginacionBase";

const PAGE_SIZE = 10;

const GestionIncidencias = () => {
  const {
    respuesta: incidencias,
    loading,
    error,
    totalPages,
    obtenerTodosIncidentes,
    eliminarIncidente,
    actualizarRevisado,
  } = useIncidentes();

  const [paginaActual, setPaginaActual] = useState(0);
  const [incidenteSeleccionado, setIncidenteSeleccionado] = useState(null);
  const [showConfirm, setShowConfirm] = useState(false);
  const [incidenteAEliminar, setIncidenteAEliminar] = useState(null);
  const [eliminando, setEliminando] = useState(false);
  const [mensajeExito, setMensajeExito] = useState("");

  useEffect(() => {
    obtenerTodosIncidentes(paginaActual, PAGE_SIZE);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [paginaActual]);

  // ---- HANDLERS ----
  const handleEliminar = (incidente) => {
    setIncidenteAEliminar(incidente);
    setShowConfirm(true);
  };

  const handleEliminarConfirmado = async () => {
    if (!incidenteAEliminar) return;
    setEliminando(true);

    try {
      await eliminarIncidente(incidenteAEliminar.idIncidente);
      setMensajeExito(`La incidencia "${incidenteAEliminar.titulo}" fue eliminada correctamente.`);
      setShowConfirm(false);
      obtenerTodosIncidentes(paginaActual, PAGE_SIZE);
      setTimeout(() => setMensajeExito(""), 3000);
    } catch (err) {
      console.error("Error al eliminar el incidente:", err);
    } finally {
      setEliminando(false);
    }
  };

  const handleVer = (incidente) => setIncidenteSeleccionado(incidente);
  const handleCerrarModal = () => setIncidenteSeleccionado(null);

  const handleActualizarRevisado = async (incidente) => {
    try {
      const nuevoEstado = !incidente.revisado;
      await actualizarRevisado(incidente.idIncidente, nuevoEstado);
      setMensajeExito(
        `El incidente "${incidente.titulo}" fue marcado como ${nuevoEstado ? "revisado" : "no revisado"}.`
      );
      setTimeout(() => setMensajeExito(""), 3000);
    } catch (err) {
      console.error("Error al actualizar revisado:", err);
    }
  };

  const mostrarTabla = !loading && !error && incidencias.length > 0;
  const mostrarSinRegistros = !loading && !error && incidencias.length === 0;

  return (
    <Card className="shadow-lg border-0">
      <HeaderBase title="Gestión de Incidencias">
        <div style={{ width: "40px", height: "37px" }}></div>
      </HeaderBase>

      <Card.Body>
        {mensajeExito && <Alert variant="success" className="text-center">{mensajeExito}</Alert>}
        {loading && (
          <div className="text-center my-4">
            <Spinner animation="border" role="status" />
            <p className="mt-2">Cargando incidencias...</p>
          </div>
        )}
        {error && <Alert variant="danger">{error}</Alert>}
        {mostrarSinRegistros && <Alert variant="info" className="text-center">No hay incidencias registradas.</Alert>}

        {mostrarTabla && (
          <Table striped bordered hover responsive>
            <thead>
              <tr>
                <th style={{ width: "180px" }}>Reportado por</th>
                <th style={{ width: "160px" }}>Fecha</th>
                <th>Título</th>
                <th style={{ width: "120px" }}>Revisado</th>
                <th style={{ width: "200px" }}>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {incidencias.map((inc) => (
                <tr key={inc.idIncidente}>
                  <td>{inc.nombreUsuario || "Desconocido"}</td>
                  <td>{inc.fechaCreacion ? new Date(inc.fechaCreacion).toLocaleDateString() : "Sin fecha"}</td>
                  <td>{inc.titulo}</td>
                  <td className="text-center">
                    <Button
                      variant={inc.revisado ? "success" : "secondary"}
                      size="sm"
                      onClick={() => handleActualizarRevisado(inc)}
                    >
                      {inc.revisado ? "Sí" : "No"}
                    </Button>
                  </td>
                  <td>
                    <div className="d-flex gap-2 justify-content-center">
                      <Button variant="info" size="sm" onClick={() => handleVer(inc)}>Ver</Button>
                      <Button variant="danger" size="sm" onClick={() => handleEliminar(inc)}>Eliminar</Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        )}
      </Card.Body>

      <Card.Footer>
        <PaginacionBase
          page={paginaActual}
          totalPages={totalPages}
          loading={loading}
          onPageChange={setPaginaActual}
        />
      </Card.Footer>

      {/* Modal de detalles */}
      <Modal show={!!incidenteSeleccionado} onHide={handleCerrarModal} centered>
        <Modal.Header closeButton>
          <Modal.Title>Detalles del Incidente</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {incidenteSeleccionado && (
            <>
              <p><strong>Reportado por:</strong> {incidenteSeleccionado.nombreUsuario}</p>
              <p><strong>Título:</strong> {incidenteSeleccionado.titulo}</p>
              <p><strong>Descripción:</strong> {incidenteSeleccionado.descripcion}</p>
              <p><strong>Fecha de creación:</strong> {incidenteSeleccionado.fechaCreacion ? new Date(incidenteSeleccionado.fechaCreacion).toLocaleString() : "Sin fecha"}</p>
              <p><strong>Revisado:</strong> {incidenteSeleccionado.revisado ? "Sí" : "No"}</p>
            </>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCerrarModal}>Cerrar</Button>
        </Modal.Footer>
      </Modal>

      {/* Modal de confirmación de eliminación */}
      <ModalConfirmacion
        show={showConfirm}
        onHide={() => setShowConfirm(false)}
        titulo="Eliminar Incidencia"
        mensaje={`¿Estás seguro de eliminar la incidencia "${incidenteAEliminar?.titulo || ""}"? Esta acción no se puede deshacer.`}
        onConfirmar={handleEliminarConfirmado}
        textoConfirmar={eliminando ? "Eliminando..." : "Eliminar"}
        variantConfirmar="danger"
        disabled={eliminando}
      />
    </Card>
  );
};

export default GestionIncidencias;
