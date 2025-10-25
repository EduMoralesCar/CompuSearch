package com.universidad.compusearch.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.entity.Etiqueta;
import com.universidad.compusearch.service.EtiquetaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@Slf4j
@RequestMapping("/etiquetas")
@RequiredArgsConstructor
public class EtiquetaController {

    private final EtiquetaService etiquetaService;

    @GetMapping
    public ResponseEntity<List<Etiqueta>> obtenerTodasLasEtiquetas() {
        log.info("Solicitando todas las etiquetas");

        List<Etiqueta> etiquetas = etiquetaService.obtenerTodas();

        log.info("Se retornaron {} etiquetas.", etiquetas.size());
        return ResponseEntity.ok(etiquetas);
    }
}


