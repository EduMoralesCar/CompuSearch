package com.universidad.compusearch.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.entity.Producto;
import com.universidad.compusearch.service.ProductoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controlador REST para manejar operaciones relacionadas con productos.
 *
 * <p>
 * Permite obtener la información de un producto por su ID.
 * </p>
 *
 * <p>
 * Base URL: <b>/productos</b>
 * </p>
 */
@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
@Slf4j
public class ProductoController {

    private final ProductoService productoService;

    /**
     * Obtiene un producto por su ID.
     *
     * Endpoint: GET /productos/{idProducto}
     *
     * @param idProducto ID del producto a obtener
     * @return ResponseEntity con el producto encontrado o con un estado 404 si no se encuentra
     */
    @GetMapping("/{idProducto}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long idProducto) {
        log.debug("Solicitud para obtener producto con ID: {}", idProducto);

        return productoService.obtenerPorId(idProducto)
                .map(producto -> {
                    log.info("Producto encontrado con ID: {}", idProducto);
                    return ResponseEntity.ok(producto);
                })
                .orElseGet(() -> {
                    log.warn("No se encontró un producto con ID: {}", idProducto);
                    return ResponseEntity.notFound().build();
                });
    }
}
