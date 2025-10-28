package com.universidad.compusearch.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.dto.IncidenciaRequest;
import com.universidad.compusearch.dto.IncidenteResponse;
import com.universidad.compusearch.dto.MessageResponse;
import com.universidad.compusearch.entity.Incidente;
import com.universidad.compusearch.service.IncidenteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para manejar las operaciones relacionadas con incidentes.
 *
 * <p>
 * Permite crear incidentes y obtener los incidentes de un usuario específico.
 * </p>
 *
 * <p>
 * Base URL: <b>/incidentes</b>
 * </p>
 */
@RestController
@Slf4j
@RequestMapping("/incidentes")
@RequiredArgsConstructor
public class IncidenteController {

    private final IncidenteService incidenteService;

    /**
     * Obtiene los incidentes de un usuario específico de manera paginada.
     *
     * Endpoint: GET /incidentes/{@literal /incidentes/{id}?page={page}&size={size}}
     *
     * @param id   ID del usuario
     * @param page Número de página (opcional, por defecto 0)
     * @param size Tamaño de la página (opcional, por defecto 10)
     * @return ResponseEntity con la página de incidentes del usuario
     */
    @GetMapping("/{id}")
    public ResponseEntity<Page<Incidente>> obtenerIncidentesUsuario(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Obteniendo incidentes de usuario con id: {} | página: {} | tamaño: {}", id, page, size);
        Page<Incidente> incidentes = incidenteService.obtenerIncidentesPorUsuario(id, page, size);
        return ResponseEntity.ok(incidentes);
    }

    /**
     * Crea un nuevo incidente para un usuario.
     *
     * Endpoint: POST /incidentes
     *
     * @param request Objeto IncidenciaResponse que contiene:
     *                <ul>
     *                <li>idUsuario: ID del usuario</li>
     *                <li>titulo: Título del incidente</li>
     *                <li>descripcion: Descripción del incidente</li>
     *                </ul>
     * @return ResponseEntity con el incidente creado y código HTTP 201
     */
    @PostMapping
    public ResponseEntity<MessageResponse> crearIncidente(@RequestBody IncidenciaRequest request) {
        log.info("Creando nuevo incidente para usuario: {}", request.getIdUsuario());

        Incidente nuevoIncidente = incidenteService.crearIncidente(
                request.getIdUsuario(),
                request.getTitulo(),
                request.getDescripcion());
        ;

        log.info("Incidente con titulo {} creado", nuevoIncidente.getTitulo());

        return ResponseEntity.ok(new MessageResponse("Incidencia creada con exito"));
    }

    /**
     * Obtiene todas las incidencias de manera paginada.
     *
     * Endpoint: GET {@literal /incidentes?page={page}&size={size}}
     *
     * @param page Número de página (opcional, por defecto 0)
     * @param size Tamaño de la página (opcional, por defecto 10)
     * @return ResponseEntity con la página de incidencias
     */
    @GetMapping
    public ResponseEntity<Page<IncidenteResponse>> obtenerSolicitudesPaginadas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Obteniendo todas las solicitudes | página: {} | tamaño: {}", page, size);

        Page<IncidenteResponse> incidentes = incidenteService.obtenerTodosLosIncidentes(page, size);
        return ResponseEntity.ok(incidentes);
    }

    /**
     * Elimina una incidencia específica por su ID.
     *
     * <p>
     * Endpoint: DELETE /incidentes/{id}
     * </p>
     *
     * @param id ID del incidente a eliminar
     * @return ResponseEntity con un mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> eliminarIncidente(@PathVariable Long id) {
        log.info("Solicitud para eliminar el incidente con id: {}", id);

        incidenteService.eliminarIncidentePorId(id);

        log.info("Incidente con id {} eliminado correctamente", id);
        return ResponseEntity.ok(new MessageResponse("Incidente eliminado exitosamente"));
    }

    /**
     * Marca un incidente como revisado o no revisado.
     *
     * @param idIncidente ID del incidente que se desea actualizar.
     * @param revisado    Estado del campo revisado (true = revisado, false = no
     *                    revisado).
     * @return Mensaje de confirmación y el estado actualizado.
     */
    @PutMapping("/{idIncidente}/revisado")
    public ResponseEntity<MessageResponse> actualizarEstadoRevisado(
            @PathVariable Long idIncidente,
            @RequestParam boolean revisado) {

        log.info("Solicitud para actualizar el estado 'revisado' del incidente con ID: {} a: {}", idIncidente,
                revisado);

        incidenteService.actualizarEstadoRevisado(idIncidente, revisado);
        log.info("Incidente con ID {} actualizado correctamente (revisado = {})", idIncidente, revisado);
        return ResponseEntity.ok(new MessageResponse("Estado de revisión del incidente cambiado."));
    }
}
