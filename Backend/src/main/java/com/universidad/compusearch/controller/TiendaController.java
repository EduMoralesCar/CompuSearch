package com.universidad.compusearch.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.dto.EstadoResponse;
import com.universidad.compusearch.dto.TiendaDetallesResponse;
import com.universidad.compusearch.dto.TiendaInfoResponse;
import com.universidad.compusearch.dto.TiendaResponse;
import com.universidad.compusearch.dto.VerificacionResponse;
import com.universidad.compusearch.service.TiendaService;
import com.universidad.compusearch.util.Mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/tiendas")
@RequiredArgsConstructor
@Slf4j
public class TiendaController {

    private final TiendaService tiendaService;

    @GetMapping("/verificadas")
    public ResponseEntity<List<TiendaResponse>> obtenerTiendasVerificadas() {
        log.info("GET /tiendas/verificadas - Solicitando tiendas verificadas");

        List<TiendaResponse> tiendasResponse = tiendaService.obtenerTiendasVerificadas()
                .stream()
                .map(Mapper::mapToTienda)
                .toList();

        log.info("Se retornaron {} tiendas verificadas", tiendasResponse.size());
        return ResponseEntity.ok(tiendasResponse);
    }

    @GetMapping
    public ResponseEntity<Page<TiendaInfoResponse>> getAllTiendas(
            @RequestParam(required = false) String nombre,
            Pageable pageable) {

        log.info("GET /tiendas - Consultando tiendas con filtro nombre='{}' y pageable={}", nombre, pageable);

        Page<TiendaInfoResponse> tiendas = tiendaService.findAllTiendas(pageable, nombre);

        log.info("Consulta completada. Total elementos: {}, Total páginas: {}",
                tiendas.getTotalElements(), tiendas.getTotalPages());

        return ResponseEntity.ok(tiendas);
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<TiendaDetallesResponse> getTiendaById(@PathVariable Long idUsuario) {
        log.info("GET /tiendas/{} - Solicitando tienda por ID de usuario", idUsuario);

        TiendaDetallesResponse tienda = tiendaService.findTiendaById(idUsuario);
        log.info("Tienda encontrada para usuario {}", idUsuario);
        return ResponseEntity.ok(tienda);
    }

    @PutMapping("/{idUsuario}/estado")
    public ResponseEntity<TiendaDetallesResponse> actualizarEstado(
            @PathVariable Long idUsuario,
            @RequestBody EstadoResponse estadoDTO) {

        log.info("PUT /tiendas/{}/estado - Actualizando estado a {}", idUsuario, estadoDTO.isActivo());

        try {
            TiendaDetallesResponse tiendaActualizada = tiendaService.actualizarEstado(idUsuario, estadoDTO.isActivo());

            log.info("Estado actualizado correctamente para el usuario {}", idUsuario);
            return ResponseEntity.ok(tiendaActualizada);

        } catch (NoSuchElementException e) {
            log.warn("No se pudo actualizar estado. Tienda no encontrada para usuario {}", idUsuario);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{idUsuario}/verificacion")
    public ResponseEntity<TiendaDetallesResponse> actualizarVerificacion(
            @PathVariable Long idUsuario,
            @RequestBody VerificacionResponse verificacionDTO) {

        log.info("PUT /tiendas/{}/verificacion - Actualizando verificación a {}",
                idUsuario, verificacionDTO.isVerificado());

        try {
            TiendaDetallesResponse tiendaActualizada = tiendaService.actualizarVerificacion(idUsuario,
                    verificacionDTO.isVerificado());

            log.info("Verificación actualizada correctamente para el usuario {}", idUsuario);
            return ResponseEntity.ok(tiendaActualizada);

        } catch (NoSuchElementException e) {
            log.warn("No se pudo actualizar verificación. Tienda no encontrada para usuario {}", idUsuario);
            return ResponseEntity.notFound().build();
        }
    }

    
}
