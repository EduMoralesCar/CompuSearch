package com.universidad.compusearch.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.universidad.compusearch.dto.MessageResponse;
import com.universidad.compusearch.dto.SolicitudFormularioRequest;
import com.universidad.compusearch.dto.SolicitudTiendaResponse;
import com.universidad.compusearch.entity.SolicitudTienda;
import com.universidad.compusearch.service.SolicitudTiendaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para manejar solicitudes de creación de tiendas por
 * usuarios.
 *
 * <p>
 * Proporciona endpoints para obtener las solicitudes de un usuario y para
 * enviar nuevas solicitudes.
 * </p>
 *
 * <p>
 * Base URL: <b>/solicitud</b>
 * </p>
 */
@RestController
@RequestMapping("/solicitud")
@RequiredArgsConstructor
@Slf4j
public class SolicitudController {

    private final SolicitudTiendaService solicitudTiendaService;

    /**
     * Obtiene una lista paginada de solicitudes realizadas por un usuario.
     *
     * @param idUsuario ID del usuario del cual se desean obtener las solicitudes
     * @param page      Número de página (opcional, por defecto 0)
     * @param size      Tamaño de página (opcional, por defecto 10)
     * @return Una página de solicitudes de tienda del usuario
     */
    @GetMapping("/{idUsuario}")
    public ResponseEntity<Page<SolicitudTienda>> obtenerSolicitudesPorUsuario(
            @PathVariable Long idUsuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Obteniendo solicitudes de usuario con id: {} | página: {} | tamaño: {}", idUsuario, page, size);
        Page<SolicitudTienda> solicitudes = solicitudTiendaService.obtenerSolicitudesPorUsuario(idUsuario, page, size);
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * Obtiene todas las solicitudes registradas (paginadas).
     *
     * @param page Número de página (por defecto 0)
     * @param size Tamaño de página (por defecto 10)
     * @return Página con todas las solicitudes
     */
    @GetMapping
    public ResponseEntity<Page<SolicitudTiendaResponse>> obtenerTodasLasSolicitudes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Obteniendo todas las solicitudes | página: {} | tamaño: {}", page, size);
        Page<SolicitudTiendaResponse> solicitudes = solicitudTiendaService.obtenerTodasLasSolicitudes(page, size);
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * Crea una nueva solicitud de tienda para un usuario específico.
     *
     * <p>
     * El endpoint recibe los datos del formulario y los registra como una nueva
     * solicitud de tienda.
     * </p>
     *
     * @param idUsuario ID del usuario que envía la solicitud
     * @param request   Objeto que contiene los datos del formulario de la solicitud
     * @return Mensaje indicando que la solicitud fue enviada correctamente
     */
    @PostMapping("/{idUsuario}")
    public ResponseEntity<MessageResponse> crearSolicitud(
            @PathVariable Long idUsuario,
            @RequestBody SolicitudFormularioRequest request) {
        log.info("Recibiendo solicitud de tienda del usuario con ID: {}", idUsuario);

        solicitudTiendaService.crearSolicitud(idUsuario, request.getDatosFormulario());

        log.info("Solicitud de tienda creada correctamente para el usuario con id {}", idUsuario);

        return ResponseEntity.ok(new MessageResponse("Solicitud enviada correctamente"));
    }

    /**
     * Actualiza el estado de una solicitud (por ejemplo: "PENDIENTE", "APROBADA",
     * "RECHAZADA").
     *
     * @param idSolicitud ID de la solicitud
     * @param idEmpleado ID del empleado que hizo el cambio
     * @param nuevoEstado Nuevo estado que se desea asignar
     * @return Mensaje de confirmación
     */
    @PutMapping("/{idSolicitud}/estado")
    public ResponseEntity<MessageResponse> actualizarEstado(
            @PathVariable Long idSolicitud,
            @RequestParam Long idEmpleado,
            @RequestParam String nuevoEstado) {

        log.info("Actualizando estado de solicitud ID: {} a '{}'", idSolicitud, nuevoEstado);
        solicitudTiendaService.actualizarEstadoSolicitud(idSolicitud, nuevoEstado, idEmpleado);
        return ResponseEntity.ok(new MessageResponse("Estado actualizado correctamente"));
    }
}
