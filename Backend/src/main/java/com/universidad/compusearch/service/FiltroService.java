package com.universidad.compusearch.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.universidad.compusearch.dto.RangoPrecioResponse;
import com.universidad.compusearch.repository.AtributoRepository;
import com.universidad.compusearch.repository.ProductoTiendaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio encargado de los filtros y rangos de productos.
 * <p>
 * Proporciona métodos para obtener rangos de precios, marcas, tiendas y valores de atributos.
 * </p>
 * 
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FiltroService {

    private final ProductoTiendaRepository productoTiendaRepository;
    private final AtributoRepository atributoRepository;

    /**
     * Obtiene el rango de precios (mínimo y máximo) para una categoría específica.
     *
     * @param nombreCategoria el nombre de la categoría de productos.
     * @return un {@link RangoPrecioResponse} con los valores mínimo y máximo,
     *         o {@code null} si no se encontraron productos para la categoría.
     */
    public RangoPrecioResponse obtenerRangoPrecio(String nombreCategoria) {
        Object result = productoTiendaRepository.obtenerRangoPrecioPorCategoria(nombreCategoria);

        if (result == null) {
            log.warn("No se encontró rango de precios para la categoría '{}'", nombreCategoria);
            return null;
        }

        Object[] valores = (Object[]) result;
        BigDecimal min = (BigDecimal) valores[0];
        BigDecimal max = (BigDecimal) valores[1];

        log.info("Rango de precios para la categoría '{}': min={}, max={}", nombreCategoria, min, max);

        return new RangoPrecioResponse(min, max);
    }

    /**
     * Obtiene los nombres de todas las marcas habilitadas para una categoría determinada.
     *
     * @param nombreCategoria el nombre de la categoría de productos, puede ser {@code null} para todas.
     * @return lista de nombres de marcas distintas.
     */
    public List<String> obtenerMarcas(String nombreCategoria) {
        log.info("Obteniendo marcas habilitadas para categoría: {}",
                nombreCategoria != null ? nombreCategoria : "TODAS");

        List<String> marcas = productoTiendaRepository.findDistinctMarcasByCategoria(nombreCategoria);

        log.debug("Total de marcas encontradas: {}", marcas.size());
        log.trace("Marcas encontradas: {}", marcas);

        return marcas;
    }

    /**
     * Obtiene los nombres de todas las tiendas que tienen productos habilitados para una categoría específica.
     *
     * @param nombreCategoria el nombre de la categoría de productos, puede ser {@code null} para todas.
     * @return lista de nombres de tiendas distintas.
     */
    public List<String> obtenerTiendasConProductosHabilitadosPorCategoria(String nombreCategoria) {
        log.info("Obteniendo tiendas con productos habilitados para categoría: {}",
                nombreCategoria != null ? nombreCategoria : "TODAS");

        List<String> tiendas = productoTiendaRepository.findDistinctTiendasWithHabilitadosByCategoria(nombreCategoria);

        log.debug("Total de tiendas encontradas: {}", tiendas.size());
        log.trace("Tiendas encontradas: {}", tiendas);

        return tiendas;
    }

    /**
     * Obtiene todos los valores distintos de un atributo dado.
     *
     * @param nombreAtributo el nombre del atributo.
     * @return lista de valores distintos del atributo.
     */
    public List<String> obtenerValoresDeAtributosPorNombre(String nombreAtributo) {
        log.info("Obteniendo atributos de {}", nombreAtributo);

        List<String> atributos = atributoRepository.findDistinctValoresByAtributo(nombreAtributo);

        log.debug("Total de atributos encontrados: {}", atributos.size());
        log.trace("Atributos encontrados: {}", atributos);

        return atributos;
    }
}
