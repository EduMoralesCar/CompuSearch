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

/**
 * Controlador REST para manejar los filtros de búsqueda de productos.
 *
 * <p>
 * Permite obtener listas de categorías, marcas, rango de precios, tiendas y valores de atributos.
 * </p>
 *
 * <p>
 * Base URL: <b>/filtro</b>
 * </p>
 */
@RestController
@RequestMapping("/filtro")
@RequiredArgsConstructor
@Slf4j
public class FiltroController {

    private final FiltroService filtroService;
    private final CategoriaService categoriaService;

    /**
     * Obtiene todas las categorías habilitadas.
     *
     * Endpoint: GET /filtro/categorias
     *
     * @return ResponseEntity con la lista de nombres de categorías o 204 si no hay categorías
     */
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

    /**
     * Obtiene todas las marcas disponibles para una categoría específica (opcional).
     *
     * Endpoint: GET /filtro/marcas
     *
     * @param categoria Nombre de la categoría (opcional)
     * @return ResponseEntity con la lista de marcas o 204 si no hay marcas disponibles
     */
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

    /**
     * Obtiene el rango de precios (mínimo y máximo) de los productos de una categoría específica.
     *
     * Endpoint: GET /filtro/precios
     *
     * @param categoria Nombre de la categoría (opcional)
     * @return ResponseEntity con un objeto RangoPrecioResponse o 204 si no se encuentra rango
     */
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

    /**
     * Obtiene la lista de tiendas que tienen productos habilitados para una categoría específica.
     *
     * Endpoint: GET /filtro/tiendas
     *
     * @param categoria Nombre de la categoría (opcional)
     * @return ResponseEntity con la lista de tiendas o 204 si no se encuentran tiendas
     */
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

    /**
     * Obtiene los valores disponibles para un atributo específico.
     *
     * Endpoint: GET /filtro/valores
     *
     * @param nombreAtributo Nombre del atributo (requerido)
     * @return ResponseEntity con la lista de valores del atributo o 204 si no hay valores
     */
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
