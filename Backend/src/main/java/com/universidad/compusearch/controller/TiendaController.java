package com.universidad.compusearch.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.dto.TiendaResponse;
import com.universidad.compusearch.service.TiendaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador REST para manejar operaciones relacionadas con las tiendas.
 *
 * <p>
 * Proporciona endpoints para obtener información de tiendas verificadas.
 * </p>
 *
 * <p>
 * Base URL: <b>/tiendas</b>
 * </p>
 */
@RestController
@RequestMapping("/tiendas")
@RequiredArgsConstructor
@Slf4j
public class TiendaController {

    private final TiendaService tiendaService;

    /**
     * Obtiene la lista de tiendas verificadas.
     *
     * <p>
     * Este endpoint retorna todas las tiendas que han sido marcadas como verificadas.
     * Cada tienda se devuelve en forma de {@link TiendaResponse}.
     * </p>
     *
     * @return Lista de tiendas verificadas
     */
    @GetMapping("/verificadas")
    public ResponseEntity<List<TiendaResponse>> obtenerTiendasVerificadas() {
        log.info("GET /tiendas/verificadas - solicitando tiendas verificadas");

        List<TiendaResponse> tiendasResponse = tiendaService.obtenerTiendasVerificadas()
                .stream()
                .map(tiendaService::mapToTienda)
                .toList();

        log.info("Se retornaron {} tiendas verificadas.", tiendasResponse.size());
        return ResponseEntity.ok(tiendasResponse);
    }
}
