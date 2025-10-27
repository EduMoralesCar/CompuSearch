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

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
@Slf4j
public class ProductoController {

    private final ProductoService productoService;

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
