package com.universidad.compusearch.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.universidad.compusearch.dto.CategoriaRequest;
import com.universidad.compusearch.entity.Categoria;
import com.universidad.compusearch.service.CategoriaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para manejar operaciones relacionadas con las categorías.
 *
 * <p>
 * Proporciona endpoints para obtener, actualizar y eliminar categorías
 * disponibles en la aplicación.
 * </p>
 *
 * <p>
 * Base URL: <b>/categorias</b>
 * </p>
 */
@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
@Slf4j
public class CategoriaController {

    /** Servicio encargado de la lógica de negocio de categorías */
    private final CategoriaService categoriaService;

    /**
     * Obtiene todas las categorías disponibles.
     *
     * <p>
     * Endpoint: <b>GET /categorias</b>
     * </p>
     *
     * @return ResponseEntity con la lista de categorías y estado HTTP 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Categoria>> obtenerTodasLasCategorias() {
        log.info("Solicitando todas las categorias");

        List<Categoria> categorias = categoriaService.obtenerTodos();

        log.info("Se retornaron {} categorias.", categorias.size());
        return ResponseEntity.ok(categorias);
    }

    /**
     * Actualiza una categoría existente según su ID.
     *
     * <p>
     * Endpoint: <b>PUT /categorias/{id}</b>
     * </p>
     *
     * @param id        Identificador de la categoría a actualizar
     * @param categoria Objeto con los datos de la categoría a actualizar
     * @return ResponseEntity con la categoría actualizada y estado HTTP 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable Long id, @RequestBody CategoriaRequest categoria) {
        log.info("Solicitando actualización de la categoría con id={}", id);

        Categoria categoriaActualizada = categoriaService.actualizar(id, categoria);

        log.info("Categoría con id={} actualizada correctamente.", id);
        return ResponseEntity.ok(categoriaActualizada);
    }

    /**
     * Elimina una categoría existente según su ID.
     *
     * <p>
     * Endpoint: <b>DELETE /categorias/{id}</b>
     * </p>
     *
     * @param id Identificador de la categoría a eliminar
     * @return ResponseEntity con estado HTTP 204 NO CONTENT si la eliminación fue
     *         exitosa
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        log.info("Solicitando eliminación de la categoría con id={}", id);

        categoriaService.eliminar(id);

        log.info("Categoría con id={} eliminada correctamente.", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Crea una nueva categoría en el sistema.
     *
     * <p>
     * Endpoint: <b>POST /categorias</b>
     * </p>
     *
     * @param categoria Objeto con los datos de la nueva categoría
     * @return ResponseEntity con la categoría creada y estado HTTP 201 CREATED
     */
    @PostMapping
    public ResponseEntity<Categoria> crearCategoria(@RequestBody CategoriaRequest categoria) {
        log.info("Solicitando creación de una nueva categoría con nombre={}", categoria.getNombre());

        Categoria categoriaCreada = categoriaService.crear(categoria);

        log.info("Categoría creada correctamente con id={}", categoriaCreada.getIdCategoria());
        return ResponseEntity.status(201).body(categoriaCreada);
    }
}
