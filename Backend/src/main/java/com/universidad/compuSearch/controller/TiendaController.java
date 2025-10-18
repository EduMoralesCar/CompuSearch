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
            .map(tiendaService::mapToTienda)
            .toList();

        log.info("Se retornaron {} tiendas verificadas.", tiendasResponse.size());
        return ResponseEntity.ok(tiendasResponse);
    }
}

