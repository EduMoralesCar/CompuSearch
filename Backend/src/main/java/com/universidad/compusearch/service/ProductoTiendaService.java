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
import com.universidad.compusearch.specification.ProductoTiendaSpecification;
import com.universidad.compusearch.specification.ProductoTiendaSpecificationFactory;
import com.universidad.compusearch.util.ProductoTiendaMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio para gestionar productos de tiendas.
 * <p>
 * Permite filtrar, buscar y obtener información de productos por categoría,
 * tienda,
 * nombre, atributos adicionales y otros criterios.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoTiendaService {

        private final ProductoTiendaRepository productoTiendaRepository;

        /**
         * Filtra productos por categoría y otros criterios opcionales.
         *
         * @param categoria    nombre de la categoría
         * @param nombreTienda nombre de la tienda (opcional)
         * @param marca        marca del producto (opcional)
         * @param precioMax    precio máximo (opcional)
         * @param precioMin    precio mínimo (opcional)
         * @param disponible   si se busca solo productos disponibles (opcional)
         * @param habilitado   si se busca solo productos habilitados (opcional)
         * @param filtrosExtra mapa con filtros adicionales (atributos)
         * @param page         número de página
         * @param size         tamaño de página
         * @return página de productos filtrados
         */
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

        /**
         * Busca productos por nombre parcial o completo.
         *
         * @param nombreProducto nombre o parte del nombre del producto
         * @param page           número de página
         * @param size           tamaño de página
         * @return página de productos que coinciden con el nombre
         */
        public Page<ProductoTienda> buscarPorNombreProducto(String nombreProducto, int page, int size) {
                log.info("Buscando productos por nombre o parecido: '{}'", nombreProducto);

                Pageable pageable = PageRequest.of(page, size);
                Specification<ProductoTienda> spec = ProductoTiendaSpecification.porNombreProducto(nombreProducto);

                Page<ProductoTienda> productos = productoTiendaRepository.findAll(spec, pageable);
                log.info("Se encontraron {} productos que coinciden con el nombre '{}'",
                                productos.getTotalElements(), nombreProducto);

                return productos;
        }

        /**
         * Busca un producto específico en una tienda específica.
         *
         * @param nombreProducto nombre del producto
         * @param nombreTienda   nombre de la tienda
         * @return producto de la tienda
         * @throws ProductoTiendaException si no se encuentra el producto en la tienda
         */
        public ProductoTienda buscarPorNombreProductoEspecifico(String nombreProducto, String nombreTienda) {
                log.info("Buscando producto '{}' en la tienda '{}'", nombreProducto, nombreTienda);

                return productoTiendaRepository.findByNombreProductoAndNombreTienda(nombreProducto, nombreTienda)
                                .orElseThrow(() -> ProductoTiendaException.notFoundProductoOrShop());
        }

        /**
         * Obtiene todas las tiendas donde un producto específico está disponible.
         *
         * @param nombreProducto nombre del producto
         * @return lista de tiendas con disponibilidad del producto
         */
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

        /**
         * Obtiene productos habilitados de una categoría para construir builds.
         *
         * @param categoria nombre de la categoría
         * @param page      número de página
         * @param size      tamaño de página
         * @return página de productos mapeados a ProductoBuildResponse
         */
        public Page<ProductoBuildResponse> obtenerProductosBuilds(String categoria, int page, int size) {
                log.info("Buscando productos para build de categoría: {}", categoria);

                Pageable pageable = PageRequest.of(page, size);

                Specification<ProductoTienda> spec = ProductoTiendaSpecification.porHabilitado(true)
                                .and(ProductoTiendaSpecification.porCategoria(categoria));

                Page<ProductoTienda> productos = productoTiendaRepository.findAll(spec, pageable);

                return productos.map(ProductoTiendaMapper::mapToProductoBuildResponse);
        }

        /**
         * Obtiene un producto de tienda por su ID.
         *
         * @param idProductoTienda ID del producto en la tienda
         * @return producto de la tienda
         * @throws ProductoTiendaException si no se encuentra el producto
         */
        public ProductoTienda obtenerPorId(long idProductoTienda) {
                log.info("Obteniendo producto tienda con id: {}", idProductoTienda);
                return productoTiendaRepository.findById(idProductoTienda)
                                .orElseThrow(() -> ProductoTiendaException.notFoundProductoOrShop());
        }
}
