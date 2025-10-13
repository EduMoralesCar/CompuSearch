package com.universidad.compusearch.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.dto.RangoPrecioResponse;
import com.universidad.compusearch.service.CategoriaService;
import com.universidad.compusearch.service.FiltroService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/filtro")
@RequiredArgsConstructor
public class FiltroController {

    private static final Logger logger = LoggerFactory.getLogger(FiltroController.class);

    private final FiltroService filtroService;
    private final CategoriaService categoriaService;

    @GetMapping("/categorias")
    public ResponseEntity<List<String>> obtenerCategorias() {
        logger.info("Solicitud recibida para obtener categorías habilitadas");

        List<String> categorias = categoriaService.obtenerTodas();

        if (categorias.isEmpty()) {
            logger.warn("No se encontraron categorías habilitadas");
            return ResponseEntity.noContent().build();
        }

        logger.debug("Categorías encontradas: {}", categorias.size());
        return ResponseEntity.ok(categorias);
    }
    
    @GetMapping("/marcas")
    public ResponseEntity<List<String>> obtenerMarcas(
            @RequestParam(required = false) String categoria) {

        logger.info("Solicitud recibida para obtener marcas (categoria = {})", categoria);

        List<String> marcas = filtroService.obtenerMarcas(categoria);

        if (marcas.isEmpty()) {
            logger.warn("No se encontraron marcas para la categoría: {}", categoria);
            return ResponseEntity.noContent().build();
        }

        logger.debug("Marcas encontradas: {}", marcas.size());
        return ResponseEntity.ok(marcas);
    }

    @GetMapping("/precios")
    public ResponseEntity<RangoPrecioResponse> obtenerRangoPrecios(
            @RequestParam(required = false) String categoria) {

        logger.info("Solicitud recibida para obtener rango de precios (categoria = {})", categoria);

        var rango = filtroService.obtenerRangoPrecio(categoria);

        if (rango == null) {
            logger.warn("No se encontró rango de precios para la categoría: {}", categoria);
            return ResponseEntity.noContent().build();
        }

        logger.debug("Rango de precios → min: {}, max: {}", rango.getPrecioMin(), rango.getPrecioMax());
        return ResponseEntity.ok(rango);
    }
}
