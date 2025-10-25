package com.universidad.compusearch.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.dto.ProductoBuildResponse;
import com.universidad.compusearch.dto.TiendaProductoDisponibleResponse;
import com.universidad.compusearch.entity.ProductoTienda;
import com.universidad.compusearch.exception.ProductoTiendaException;
import com.universidad.compusearch.repository.ProductoTiendaRepository;
import com.universidad.compusearch.specification.*;
import com.universidad.compusearch.util.ProductoTiendaMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Servicio de los productos de tiendas
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoTiendaService {

        private final ProductoTiendaRepository productoTiendaRepository;

        // Método principal: decide qué filtros aplicar según la categoría recibida.
        public Page<ProductoTienda> filtrarPorCategoria(
                        String categoria,
                        String nombreTienda,
                        String marca,
                        BigDecimal precioMax,
                        BigDecimal precioMin,
                        Boolean disponible,
                        Boolean habilitado,
                        Map<String, String> filtrosExtra,
                        int page,
                        int size) {
                Pageable pageable = PageRequest.of(page, size);

                Specification<ProductoTienda> spec = ProductoTiendaSpecificationFactory.crearSpec(
                                categoria, nombreTienda, marca, precioMin, precioMax,
                                disponible, habilitado, filtrosExtra);

                Page<ProductoTienda> productos = productoTiendaRepository.findAll(spec, pageable);

                log.info("Se encontraron {} productos para la categoría '{}' con filtros {}",
                                productos.getTotalElements(), categoria, filtrosExtra);

                return productos;
        }

        // Búsqueda general por nombre de producto
        public Page<ProductoTienda> buscarPorNombreProducto(String nombreProducto, int page, int size) {
                log.info("Buscando productos por nombre o parecido: '{}'", nombreProducto);

                Pageable pageable = PageRequest.of(page, size);
                Specification<ProductoTienda> spec = ProductoTiendaSpecification.porNombreProducto(nombreProducto);

                Page<ProductoTienda> productos = productoTiendaRepository.findAll(spec, pageable);
                log.info("Se encontraron {} productos que coinciden con el nombre '{}'",
                                productos.getTotalElements(), nombreProducto);

                return productos;
        }

        // Búsqueda de un producto específico en una tienda específica
        public ProductoTienda buscarPorNombreProductoEspecifico(String nombreProducto, String nombreTienda) {
                log.info("Buscando producto '{}' en la tienda '{}'", nombreProducto, nombreTienda);

                return productoTiendaRepository.findByNombreProductoAndNombreTienda(nombreProducto, nombreTienda)
                                .orElseThrow(() -> ProductoTiendaException.notFoundProductoOrShop());
        }

        public List<TiendaProductoDisponibleResponse> obtenerTiendasPorNombreProducto(String nombreProducto) {
                List<ProductoTienda> productosTienda = productoTiendaRepository.findByNombreProducto(nombreProducto);

                return productosTienda.stream()
                                .map(pt -> new TiendaProductoDisponibleResponse(
                                                pt.getTienda().getNombre(),
                                                pt.getPrecio(),
                                                pt.getStock(),
                                                pt.getUrlProducto()))
                                .collect(Collectors.toList());
        }

        public Page<ProductoBuildResponse> obtenerProductosBuilds(
                        String categoria,
                        int page,
                        int size) {

                log.info("Buscanco productos para build de categoria: ", categoria);
                Pageable pageable = PageRequest.of(page, size);

                Specification<ProductoTienda> spec = ProductoTiendaSpecification.porHabilitado(true)
                                .and(ProductoTiendaSpecification.porCategoria(categoria));

                Page<ProductoTienda> productos = productoTiendaRepository.findAll(spec, pageable);

                return productos.map(ProductoTiendaMapper::mapToProductoBuildResponse);
        }

        public ProductoTienda obtenerPorId(long idProductoTienda) {
                log.info("Obteniendo producto tienda con id: {}", idProductoTienda);
                return productoTiendaRepository.findById(idProductoTienda).orElseThrow(
                                () -> ProductoTiendaException.notFoundProductoOrShop());

        }

}
