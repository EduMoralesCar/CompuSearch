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

import com.universidad.compusearch.dto.MessageResponse;
import com.universidad.compusearch.dto.SolicitudFormularioRequest;
import com.universidad.compusearch.entity.SolicitudTienda;
import com.universidad.compusearch.service.SolicitudTiendaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/solicitud")
@RequiredArgsConstructor
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

    @PostMapping("/{idUsuario}")
    public ResponseEntity<MessageResponse> crearSolicitud(
            @PathVariable Long idUsuario,
            @RequestBody SolicitudFormularioRequest request) {
        log.info("📩 Recibiendo solicitud de tienda del usuario con ID: {}", idUsuario);

        solicitudTiendaService.crearSolicitud(idUsuario, request.getDatosFormulario());

        log.info("Solicitud de tienda creada correctamente para el usuario con id{}", idUsuario);

        return ResponseEntity.ok(new MessageResponse("Solicitud enviada correctamente"));
    }
}
