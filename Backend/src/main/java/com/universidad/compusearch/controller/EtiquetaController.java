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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controlador REST para manejar operaciones relacionadas con las etiquetas.
 *
 * <p>
 * Proporciona endpoints para obtener, actualizar y eliminar etiquetas
 * disponibles en la aplicación.
 * </p>
 *
 * <p>
 * Base URL: <b>/etiquetas</b>
 * </p>
 */
@RestController
@Slf4j
@RequestMapping("/etiquetas")
@RequiredArgsConstructor
public class EtiquetaController {

    /** Servicio encargado de la lógica de negocio de etiquetas */
    private final EtiquetaService etiquetaService;

    /**
     * Obtiene todas las etiquetas disponibles.
     *
     * <p>
     * Endpoint: <b>GET /etiquetas</b>
     * </p>
     *
     * @return ResponseEntity con la lista de etiquetas y estado HTTP 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Etiqueta>> obtenerTodasLasEtiquetas() {
        log.info("Solicitando todas las etiquetas");

        List<Etiqueta> etiquetas = etiquetaService.obtenerTodas();

        log.info("Se retornaron {} etiquetas.", etiquetas.size());
        return ResponseEntity.ok(etiquetas);
    }

    /**
     * Actualiza una etiqueta existente según su ID.
     *
     * <p>
     * Endpoint: <b>PUT /etiquetas/{id}</b>
     * </p>
     *
     * @param id       Identificador de la etiqueta a actualizar
     * @param nombreEtiqueta Datos de la etiqueta para actualizar
     * @return ResponseEntity con la etiqueta actualizada y estado HTTP 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<Etiqueta> actualizarEtiqueta(@PathVariable Long id, @RequestBody String nombreEtiqueta) {
        log.info("Solicitando actualización de etiqueta con id={}", id);
        Etiqueta actualizada = etiquetaService.actualizar(id, nombreEtiqueta);
        log.info("Etiqueta con id={} actualizada correctamente", id);
        return ResponseEntity.ok(actualizada);
    }

    /**
     * Elimina una etiqueta existente según su ID.
     *
     * <p>
     * Endpoint: <b>DELETE /etiquetas/{id}</b>
     * </p>
     *
     * @param id Identificador de la etiqueta a eliminar
     * @return ResponseEntity con estado HTTP 204 No Content si la eliminación fue
     *         exitosa
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEtiqueta(@PathVariable Long id) {
        log.info("Solicitando eliminación de etiqueta con id={}", id);
        etiquetaService.eliminar(id);
        log.info("Etiqueta con id={} eliminada correctamente", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Crea una nueva etiqueta.
     *
     * <p>
     * Endpoint: <b>POST /etiquetas</b>
     * </p>
     *
     * @param nombreEtiqueta Nombre de la nueva etiqueta
     * @return ResponseEntity con la etiqueta creada y estado HTTP 201 Created
     */
    @PostMapping
    public ResponseEntity<Etiqueta> crearEtiqueta(@RequestBody String nombreEtiqueta) {
        log.info("Solicitando creación de nueva etiqueta: {}", nombreEtiqueta);

        Etiqueta nuevaEtiqueta = etiquetaService.crear(nombreEtiqueta);

        log.info("Etiqueta creada correctamente con id={}", nuevaEtiqueta.getIdEtiqueta());
        return ResponseEntity.status(201).body(nuevaEtiqueta);
    }

}
