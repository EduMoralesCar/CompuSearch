package com.universidad.compusearch.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.dto.RangoPrecioResponse;
import com.universidad.compusearch.service.CategoriaService;
import com.universidad.compusearch.service.FiltroService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/filtro")
@RequiredArgsConstructor
@Slf4j
public class FiltroController {

    private final FiltroService filtroService;
    private final CategoriaService categoriaService;

    @GetMapping("/categorias")
    public ResponseEntity<List<String>> obtenerCategorias() {
        log.info("Solicitud recibida para obtener categorías habilitadas");

        List<String> categorias = categoriaService.obtenerTodasLosNombres();

        if (categorias.isEmpty()) {
            log.warn("No se encontraron categorías habilitadas");
            return ResponseEntity.noContent().build();
        }

        log.debug("Categorías encontradas: {}", categorias.size());
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/marcas")
    public ResponseEntity<List<String>> obtenerMarcas(
            @RequestParam(required = false) String categoria) {

        log.info("Solicitud recibida para obtener marcas (categoria = {})", categoria);

        List<String> marcas = filtroService.obtenerMarcas(categoria);

        if (marcas.isEmpty()) {
            log.warn("No se encontraron marcas para la categoría: {}", categoria);
            return ResponseEntity.noContent().build();
        }

        log.debug("Marcas encontradas: {}", marcas.size());
        return ResponseEntity.ok(marcas);
    }

    @GetMapping("/precios")
    public ResponseEntity<RangoPrecioResponse> obtenerRangoPrecios(
            @RequestParam(required = false) String categoria) {

        log.info("Solicitud recibida para obtener rango de precios (categoria = {})", categoria);

        var rango = filtroService.obtenerRangoPrecio(categoria);

        if (rango == null) {
            log.warn("No se encontró rango de precios para la categoría: {}", categoria);
            return ResponseEntity.noContent().build();
        }

        log.debug("Rango de precios → min: {}, max: {}", rango.getPrecioMin(), rango.getPrecioMax());
        return ResponseEntity.ok(rango);
    }

    @GetMapping("/tiendas")
    public ResponseEntity<List<String>> obtenerTiendasPorCategoria(
            @RequestParam(required = false) String categoria) {

        log.info("Solicitud recibida para obtener tiendas (categoria = {})", categoria);

        List<String> tiendas = filtroService.obtenerTiendasConProductosHabilitadosPorCategoria(categoria);

        if (tiendas.isEmpty()) {
            log.warn("No se encontraron tiendas para la categoría: {}", categoria);
            return ResponseEntity.noContent().build();
        }

        log.debug("Tiendas encontradas: {}", tiendas.size());
        return ResponseEntity.ok(tiendas);
    }

    @GetMapping("/valores")
    public ResponseEntity<List<String>> obtenerAtributosPorNombre(
            @RequestParam(required = true) String nombreAtributo) {

        log.info("Solicitud recibida para obtener atributos (atributo = {})", nombreAtributo);

        List<String> atributos = filtroService.obtenerValoresDeAtributosPorNombre(nombreAtributo);

        if (atributos.isEmpty()) {
            log.warn("No se encontraron atributos para el atributo: {}", nombreAtributo);
            return ResponseEntity.noContent().build();
        }

        log.debug("Atributos encontrados: {}", atributos.size());
        return ResponseEntity.ok(atributos);
    }
}
