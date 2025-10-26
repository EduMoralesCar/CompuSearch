package com.universidad.compusearch.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.dto.IncidenciaResponse;
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
    public ResponseEntity<IncidenciaResponse> crearIncidente(@RequestBody IncidenciaResponse request) {
        log.info("Creando nuevo incidente para usuario: {}", request.getIdUsuario());

        Incidente nuevoIncidente = incidenteService.crearIncidente(
                request.getIdUsuario(),
                request.getTitulo(),
                request.getDescripcion());

        IncidenciaResponse response = new IncidenciaResponse(
                nuevoIncidente.getUsuario().getIdUsuario(),
                nuevoIncidente.getTitulo(),
                nuevoIncidente.getDescripcion());

        return ResponseEntity.status(201).body(response);
    }
}
