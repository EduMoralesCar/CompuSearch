package com.universidad.compusearch.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.universidad.compusearch.dto.CategoriaRequest;
import com.universidad.compusearch.entity.Categoria;
import com.universidad.compusearch.service.CategoriaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
@Slf4j
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping("/todas")
    public ResponseEntity<List<Categoria>> obtenerTodasLasCategorias() {
        log.info("Solicitando todas las categorias");

        List<Categoria> categorias = categoriaService.obtenerTodos();

        log.info("Se retornaron {} categorias.", categorias.size());
        return ResponseEntity.ok(categorias);
    }

    @GetMapping
    public ResponseEntity<Page<Categoria>> obtenerTodasCategoriasPaginadas(
            Pageable pageable) {

        log.info("Solicitando categorias paginadas. Página: {}, Tamaño: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<Categoria> categoriasPage = categoriaService.obtenerTodosPaginados(pageable);

        log.info("Se retornó la página {} con {} categorías (Total: {}).",
                categoriasPage.getNumber(), categoriasPage.getNumberOfElements(),
                categoriasPage.getTotalElements());

        return ResponseEntity.ok(categoriasPage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable Long id,
            @RequestBody CategoriaRequest categoria) {
        log.info("Solicitando actualización de la categoría con id={}", id);

        Categoria categoriaActualizada = categoriaService.actualizar(id, categoria);

        log.info("Categoría con id={} actualizada correctamente.", id);
        return ResponseEntity.ok(categoriaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        log.info("Solicitando eliminación de la categoría con id={}", id);

        categoriaService.eliminar(id);

        log.info("Categoría con id={} eliminada correctamente.", id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Categoria> crearCategoria(@RequestBody CategoriaRequest categoria) {
        log.info("Solicitando creación de una nueva categoría con nombre={}", categoria.getNombre());

        Categoria categoriaCreada = categoriaService.crear(categoria);

        log.info("Categoría creada correctamente con id={}", categoriaCreada.getIdCategoria());
        return ResponseEntity.status(201).body(categoriaCreada);
    }
}
