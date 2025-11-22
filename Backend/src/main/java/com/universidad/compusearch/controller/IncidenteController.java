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

@RestController
@Slf4j
@RequestMapping("/incidentes")
@RequiredArgsConstructor
public class IncidenteController {

    private final IncidenteService incidenteService;

    @GetMapping("/{id}")
    public ResponseEntity<Page<Incidente>> obtenerIncidentesUsuario(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Obteniendo incidentes de usuario con id: {} | página: {} | tamaño: {}", id, page, size);
        Page<Incidente> incidentes = incidenteService.obtenerIncidentesPorUsuario(id, page, size);
        return ResponseEntity.ok(incidentes);
    }

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

    @GetMapping
    public ResponseEntity<Page<IncidenteResponse>> obtenerTodosIncidentes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Obteniendo todas las solicitudes | página: {} | tamaño: {}", page, size);

        Page<IncidenteResponse> incidentes = incidenteService.obtenerTodosLosIncidentes(page, size);
        return ResponseEntity.ok(incidentes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> eliminarIncidente(@PathVariable Long id) {
        log.info("Solicitud para eliminar el incidente con id: {}", id);

        incidenteService.eliminarIncidentePorId(id);

        log.info("Incidente con id {} eliminado correctamente", id);
        return ResponseEntity.ok(new MessageResponse("Incidente eliminado exitosamente"));
    }

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
