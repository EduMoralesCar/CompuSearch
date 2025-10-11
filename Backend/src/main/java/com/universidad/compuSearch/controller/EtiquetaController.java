package com.universidad.compusearch.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.entity.Etiqueta;
import com.universidad.compusearch.service.EtiquetaService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/etiquetas")
@RequiredArgsConstructor
public class EtiquetaController {

    private static final Logger logger = LoggerFactory.getLogger(EtiquetaController.class);
    private final EtiquetaService etiquetaService;

    @GetMapping
    public ResponseEntity<List<Etiqueta>> obtenerTodasLasEtiquetas() {
        logger.info("GET /etiqueta - solicitando todas las etiquetas");

        List<Etiqueta> etiquetas = etiquetaService.obtenerTodas();

        logger.info("Se retornaron {} etiquetas.", etiquetas.size());
        return ResponseEntity.ok(etiquetas);
    }
}


