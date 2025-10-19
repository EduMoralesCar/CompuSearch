package com.universidad.compusearch.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.dto.TiendaResponse;
import com.universidad.compusearch.service.TiendaService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/tiendas")
@RequiredArgsConstructor
public class TiendaController {

    private static final Logger logger = LoggerFactory.getLogger(TiendaController.class);
    private final TiendaService tiendaService;

    @GetMapping("/verificadas")
    public ResponseEntity<List<TiendaResponse>> obtenerTiendasVerificadas() {
        logger.info("GET /tiendas/verificadas - solicitando tiendas verificadas");

        List<TiendaResponse> tiendasResponse = tiendaService.obtenerTiendasVerificadas()
            .stream()
            .map(tiendaService::mapToTienda)
            .toList();

        logger.info("Se retornaron {} tiendas verificadas.", tiendasResponse.size());
        return ResponseEntity.ok(tiendasResponse);
    }
}

