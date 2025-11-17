package com.universidad.compusearch.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.entity.Etiqueta;
import com.universidad.compusearch.service.EtiquetaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@Slf4j
@RequestMapping("/etiquetas")
@RequiredArgsConstructor
public class EtiquetaController {

    private final EtiquetaService etiquetaService;

    @GetMapping("/todas")
    public ResponseEntity<List<Etiqueta>> obtenerTodasLasEtiquetas() {
        log.info("Solicitando todas las etiquetas");

        List<Etiqueta> etiquetas = etiquetaService.obtenerTodas();

        log.info("Se retornaron {} etiquetas.", etiquetas.size());
        return ResponseEntity.ok(etiquetas);
    }

    @GetMapping
    public ResponseEntity<Page<Etiqueta>> obtenerEtiquetasPaginadas(
            Pageable pageable) {

        log.info("Solicitando etiquetas paginadas. Página: {}, Tamaño: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<Etiqueta> etiquetasPage = etiquetaService.obtenerTodasPaginadas(pageable);

        log.info("Se retornó la página {} con {} etiquetas (Total: {}).",
                etiquetasPage.getNumber(), etiquetasPage.getNumberOfElements(),
                etiquetasPage.getTotalElements());

        return ResponseEntity.ok(etiquetasPage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Etiqueta> actualizarEtiqueta(@PathVariable Long id, @RequestBody String nombreEtiqueta) {
        log.info("Solicitando actualización de etiqueta con id={}", id);
        Etiqueta actualizada = etiquetaService.actualizar(id, nombreEtiqueta);
        log.info("Etiqueta con id={} actualizada correctamente", id);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEtiqueta(@PathVariable Long id) {
        log.info("Solicitando eliminación de etiqueta con id={}", id);
        etiquetaService.eliminar(id);
        log.info("Etiqueta con id={} eliminada correctamente", id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping
    public ResponseEntity<Etiqueta> crearEtiqueta(@RequestBody String nombreEtiqueta) {
        log.info("Solicitando creación de nueva etiqueta: {}", nombreEtiqueta);

        Etiqueta nuevaEtiqueta = etiquetaService.crear(nombreEtiqueta);

        log.info("Etiqueta creada correctamente con id={}", nuevaEtiqueta.getIdEtiqueta());
        return ResponseEntity.status(201).body(nuevaEtiqueta);
    }

}
