package com.universidad.compusearch.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.dto.MessageResponse;
import com.universidad.compusearch.dto.SuscripcionActualResponse;
import com.universidad.compusearch.dto.SuscripcionResponse;
import com.universidad.compusearch.service.SuscripcionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/suscripciones")
@Slf4j
@RequiredArgsConstructor
public class SuscripcionController {

    private final SuscripcionService suscripcionService;

    @GetMapping("/actual/{idTienda}")
    public ResponseEntity<SuscripcionActualResponse> obtenerSuscripcionActual(@PathVariable Long idTienda) {
        log.info("GET /suscripciones/actual/{}", idTienda);

        SuscripcionActualResponse sus = suscripcionService.obtenerSuscripcionActual(idTienda);

        if (sus == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(sus);
    }

    @GetMapping("/{idTienda}")
    public ResponseEntity<Page<SuscripcionResponse>> obtenerSuscripciones(
            @PathVariable Long idTienda,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("GET /suscripciones/{} (page: {}, size: {})", idTienda, page, size);

        Page<SuscripcionResponse> suscripciones = suscripcionService.obtenerSuscripcionesPorUsuario(idTienda, page,
                size);

        if (suscripciones.isEmpty()) {
            log.info("No se encontraron suscripciones para la tienda {}", idTienda);
            return ResponseEntity.noContent().build();
        }

        log.info("Retornando {} suscripciones para la tienda {}", suscripciones.getNumberOfElements(), idTienda);
        return ResponseEntity.ok(suscripciones);
    }

    @PostMapping("/crear")
    public ResponseEntity<MessageResponse> crearSuscripcion(
            @RequestParam Long idPlan,
            @RequestParam Long idTienda,
            @RequestParam(defaultValue = "true") boolean aceptarPago) {
        suscripcionService.asociarSuscripcion(idPlan, idTienda, aceptarPago);
        return ResponseEntity.ok(new MessageResponse("Suscripción creada exitosamente."));
    }

    @PostMapping("/cancelar/{idTienda}")
    public ResponseEntity<MessageResponse> cancelarSuscripcion(@PathVariable Long idTienda) {
        suscripcionService.cancelarSuscripcion(idTienda);
        return ResponseEntity.ok(new MessageResponse("Cancelación programada correctamente."));
    }

    @PostMapping("/cambiar")
    public ResponseEntity<MessageResponse> cambiarSuscripcion(
            @RequestParam Long idPlanNuevo,
            @RequestParam Long idTienda,
            @RequestParam(defaultValue = "true") boolean aceptarPago) {
        suscripcionService.cambiarSuscripcion(idPlanNuevo, idTienda, aceptarPago);
        return ResponseEntity.ok(new MessageResponse("Cambio de plan programado exitosamente."));
    }
}
