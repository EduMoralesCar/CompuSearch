package com.universidad.compusearch.service;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.dto.RangoPrecioResponse;
import com.universidad.compusearch.repository.ProductoTiendaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FiltroService {
    private static final Logger logger = LoggerFactory.getLogger(FiltroService.class);

    private final ProductoTiendaRepository productoTiendaRepository;

    public RangoPrecioResponse obtenerRangoPrecio(String nombreCategoria) {
        Object result = productoTiendaRepository.obtenerRangoPrecioPorCategoria(nombreCategoria);

        if (result == null) {
            logger.warn("No se encontró rango de precios para la categoría '{}'", nombreCategoria);
            return null;
        }

        Object[] valores = (Object[]) result;
        BigDecimal min = (BigDecimal) valores[0];
        BigDecimal max = (BigDecimal) valores[1];

        logger.info("Rango de precios para la categoría '{}': min={}, max={}", nombreCategoria, min, max);

        return new RangoPrecioResponse(min, max);
    }

    public List<String> obtenerMarcas(String nombreCategoria) {
        logger.info("Obteniendo marcas habilitadas para categoría: {}",
                nombreCategoria != null ? nombreCategoria : "TODAS");

        List<String> marcas = productoTiendaRepository.findDistinctMarcasByCategoria(nombreCategoria);

        logger.debug("Total de marcas encontradas: {}", marcas.size());
        logger.trace("Marcas encontradas: {}", marcas);

        return marcas;
    }

    public List<String> obtenerTiendasConProductosHabilitadosPorCategoria(String nombreCategoria) {
        logger.info("Obteniendo tiendas con productos habilitados para categoría: {}",
                nombreCategoria != null ? nombreCategoria : "TODAS");

        List<String> tiendas = productoTiendaRepository.findDistinctTiendasWithHabilitadosByCategoria(nombreCategoria);

        logger.debug("Total de tiendas encontradas: {}", tiendas.size());
        logger.trace("Tiendas encontradas: {}", tiendas);

        return tiendas;
    }

}
