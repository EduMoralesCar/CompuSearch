package com.universidad.compusearch.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.universidad.compusearch.dto.RangoPrecioResponse;
import com.universidad.compusearch.repository.AtributoRepository;
import com.universidad.compusearch.repository.ProductoTiendaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FiltroService {

    private final ProductoTiendaRepository productoTiendaRepository;
    private final AtributoRepository atributoRepository;

    // Obtener rango de precio
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

    // Obtener marcas por categoria
    public List<String> obtenerMarcas(String nombreCategoria) {
        log.info("Obteniendo marcas habilitadas para categoría: {}",
                nombreCategoria != null ? nombreCategoria : "TODAS");

        List<String> marcas = productoTiendaRepository.findDistinctMarcasByCategoria(nombreCategoria);

        log.debug("Total de marcas encontradas: {}", marcas.size());
        log.trace("Marcas encontradas: {}", marcas);

        return marcas;
    }

    // Obtener tiendas con productos habilitadas por categoria
    public List<String> obtenerTiendasConProductosHabilitadosPorCategoria(String nombreCategoria) {
        log.info("Obteniendo tiendas con productos habilitados para categoría: {}",
                nombreCategoria != null ? nombreCategoria : "TODAS");

        List<String> tiendas = productoTiendaRepository.findDistinctTiendasWithHabilitadosByCategoria(nombreCategoria);

        log.debug("Total de tiendas encontradas: {}", tiendas.size());
        log.trace("Tiendas encontradas: {}", tiendas);

        return tiendas;
    }

    // Obtener calores de atributos por nombre de atributo
    public List<String> obtenerValoresDeAtributosPorNombre(String nombreAtributo) {
        log.info("Obteniendo atributos de {}", nombreAtributo);

        List<String> atributos = atributoRepository.findDistinctValoresByAtributo(nombreAtributo);

        log.debug("Total de atributos encontrados: {}", atributos.size());
        log.trace("Atributos encontrados: {}", atributos);

        return atributos;
    }
}
