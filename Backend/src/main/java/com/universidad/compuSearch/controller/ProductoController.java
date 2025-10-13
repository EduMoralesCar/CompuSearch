package com.universidad.compusearch.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.entity.Producto;
import com.universidad.compusearch.service.ProductoService;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {

    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    private final ProductoService productoService;

    @GetMapping("/{idProducto}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long idProducto) {
        logger.debug("Solicitud para obtener producto con ID: {}", idProducto);

        return productoService.obtenerPorId(idProducto)
                .map(producto -> {
                    logger.info("Producto encontrado con ID: {}", idProducto);
                    return ResponseEntity.ok(producto);
                })
                .orElseGet(() -> {
                    logger.warn("No se encontró un producto con ID: {}", idProducto);
                    return ResponseEntity.notFound().build();
                });
    }
}
