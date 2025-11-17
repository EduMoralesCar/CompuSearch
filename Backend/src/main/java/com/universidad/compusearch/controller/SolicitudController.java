package com.universidad.compusearch.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.universidad.compusearch.dto.MessageResponse;
import com.universidad.compusearch.dto.SolicitudTiendaResponse;
import com.universidad.compusearch.entity.SolicitudTienda;
import com.universidad.compusearch.service.SolicitudTiendaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/solicitud")
@RequiredArgsConstructor
@Slf4j
public class SolicitudController {

    private final SolicitudTiendaService solicitudTiendaService;

    @GetMapping("/{idUsuario}")
    public ResponseEntity<Page<SolicitudTienda>> obtenerSolicitudesPorUsuario(
            @PathVariable Long idUsuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Obteniendo solicitudes de usuario con id: {} | página: {} | tamaño: {}", idUsuario, page, size);
        Page<SolicitudTienda> solicitudes = solicitudTiendaService.obtenerSolicitudesPorUsuario(idUsuario, page, size);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping
    public ResponseEntity<Page<SolicitudTiendaResponse>> obtenerTodasLasSolicitudes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Obteniendo todas las solicitudes | página: {} | tamaño: {}", page, size);
        Page<SolicitudTiendaResponse> solicitudes = solicitudTiendaService.obtenerTodasLasSolicitudes(page, size);
        return ResponseEntity.ok(solicitudes);
    }

    @PostMapping("/{idUsuario}")
    public ResponseEntity<MessageResponse> crearSolicitud(
            @PathVariable Long idUsuario,
            @RequestBody Map<String, Object> datosFormulario) {
        log.info("Recibiendo solicitud de tienda del usuario con ID: {}", idUsuario);

        solicitudTiendaService.crearSolicitud(idUsuario, datosFormulario);

        log.info("Solicitud de tienda creada correctamente para el usuario con id {}", idUsuario);

        return ResponseEntity.ok(new MessageResponse("Solicitud enviada correctamente"));
    }

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
