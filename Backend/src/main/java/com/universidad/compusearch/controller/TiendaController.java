package com.universidad.compusearch.controller;

import org.springframework.web.bind.annotation.RequestMapping;
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
        log.info("GET /tiendas/verificadas - solicitando tiendas verificadas");

        List<TiendaResponse> tiendasResponse = tiendaService.obtenerTiendasVerificadas()
                .stream()
                .map(Mapper::mapToTienda)
                .toList();

        log.info("Se retornaron {} tiendas verificadas.", tiendasResponse.size());
        return ResponseEntity.ok(tiendasResponse);
    }

    @GetMapping
    public ResponseEntity<Page<TiendaInfoResponse>> getAllTiendas(Pageable pageable) {
        Page<TiendaInfoResponse> tiendas = tiendaService.findAllTiendas(pageable);
        return ResponseEntity.ok(tiendas);
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<TiendaDetallesResponse> getTiendaById(@PathVariable Long idUsuario) {
        try {
            TiendaDetallesResponse tienda = tiendaService.findTiendaById(idUsuario);
            return ResponseEntity.ok(tienda);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{idUsuario}/estado")
    public ResponseEntity<TiendaDetallesResponse> actualizarEstado(@PathVariable Long idUsuario, @RequestBody EstadoResponse estadoDTO) {
        try {
            TiendaDetallesResponse tiendaActualizada = tiendaService.actualizarEstado(idUsuario, estadoDTO.isActivo());
            return ResponseEntity.ok(tiendaActualizada);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{idUsuario}/verificacion")
    public ResponseEntity<TiendaDetallesResponse> actualizarVerificacion(@PathVariable Long idUsuario, @RequestBody VerificacionResponse verificacionDTO) {
        try {
            TiendaDetallesResponse tiendaActualizada = tiendaService.actualizarVerificacion(idUsuario, verificacionDTO.isVerificado());
            return ResponseEntity.ok(tiendaActualizada);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
