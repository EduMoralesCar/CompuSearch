package com.universidad.compusearch.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.universidad.compusearch.dto.ProductoInfoResponse;
import com.universidad.compusearch.dto.ProductoTiendaResponse;
import com.universidad.compusearch.dto.TiendaProductoDisponibleResponse;
import com.universidad.compusearch.entity.ProductoTienda;
import com.universidad.compusearch.service.ProductoTiendaService;
import com.universidad.compusearch.util.FiltroUtils;
import com.universidad.compusearch.util.ProductoTiendaMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para manejar operaciones relacionadas con productos de
 * tienda.
 *
 * <p>
 * Permite filtrar, buscar y obtener información de productos específicos
 * disponibles en distintas tiendas.
 * </p>
 *
 * <p>
 * Base URL: <b>/componentes</b>
 * </p>
 */
@RestController
@Slf4j
@RequestMapping("/componentes")
@RequiredArgsConstructor
public class ProductoTiendaController {

        private final ProductoTiendaService productoTiendaService;

        /**
         * Filtra productos de tienda según categoría, tienda, rango de precio,
         * disponibilidad, marca y filtros adicionales.
         *
         * @param categoria    Nombre de la categoría (opcional)
         * @param nombreTienda Nombre de la tienda (opcional)
         * @param precioMax    Precio máximo (opcional)
         * @param precioMin    Precio mínimo (opcional)
         * @param disponible   Indica si se filtran solo productos disponibles
         *                     (opcional)
         * @param marca        Marca del producto (opcional)
         * @param page         Número de página para paginación (por defecto 0)
         * @param size         Tamaño de página para paginación (por defecto 15)
         * @param filtrosExtra Mapa de filtros adicionales (opcional)
         * @return Página de productos filtrados
         */
        @GetMapping("/filtrar")
        public ResponseEntity<Page<ProductoTiendaResponse>> filtrarComponentes(
                        @RequestParam(required = false) String categoria,
                        @RequestParam(required = false) String nombreTienda,
                        @RequestParam(required = false) BigDecimal precioMax,
                        @RequestParam(required = false) BigDecimal precioMin,
                        @RequestParam(required = false) Boolean disponible,
                        @RequestParam(required = false) String marca,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "15") int size,
                        @RequestParam(required = false) Map<String, String> filtrosExtra) {

                Map<String, String> filtrosLimpios = FiltroUtils.limpiarFiltros(filtrosExtra);
                log.info("Filtrando categoría={} con parámetros extra={}", categoria, filtrosLimpios);

                Page<ProductoTiendaResponse> resultados = productoTiendaService
                                .filtrarPorCategoria(categoria, nombreTienda, marca, precioMax, precioMin, disponible,
                                                true, filtrosLimpios, page, size)
                                .map(ProductoTiendaMapper::mapToResponse);

                if (resultados.isEmpty()) {
                        log.warn("No se encontraron productos para la categoría {} con filtros {}", categoria,
                                        filtrosLimpios);
                        return ResponseEntity.ok(Page.empty());
                }

                log.info("Se encontraron {} productos para la categoría {}", resultados.getTotalElements(),
                                categoria);
                return ResponseEntity.ok(resultados);
        }

        /**
         * Busca productos por nombre.
         *
         * @param nombre Nombre parcial o completo del producto
         * @param page   Número de página para paginación (por defecto 0)
         * @param size   Tamaño de página para paginación (por defecto 15)
         * @return Página de productos encontrados que coinciden con el nombre
         */
        @GetMapping("/buscar")
        public ResponseEntity<Page<ProductoTiendaResponse>> buscarPorNombre(
                        @RequestParam String nombre,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "15") int size) {

                log.info("Buscando productos por nombre: '{}', page={}, size={}", nombre, page, size);

                Page<ProductoTiendaResponse> resultados = productoTiendaService
                                .buscarPorNombreProducto(nombre, page, size)
                                .map(ProductoTiendaMapper::mapToResponse);

                if (resultados.isEmpty()) {
                        log.warn("No se encontraron productos con el nombre: '{}'", nombre);
                        return ResponseEntity.ok(Page.empty());
                }

                log.info("Se encontraron {} productos que coinciden con '{}'", resultados.getTotalElements(),
                                nombre);
                return ResponseEntity.ok(resultados);
        }

        /**
         * Obtiene información detallada de un producto específico de una tienda.
         *
         * @param nombreProducto Nombre del producto
         * @param nombreTienda   Nombre de la tienda
         * @return Información detallada del producto en la tienda
         */
        @GetMapping("/info")
        public ResponseEntity<ProductoInfoResponse> obtenerInfoProductoPorTienda(
                        @RequestParam String nombreProducto,
                        @RequestParam String nombreTienda) {

                ProductoTienda productoTienda = productoTiendaService
                                .buscarPorNombreProductoEspecifico(nombreProducto, nombreTienda);

                ProductoInfoResponse response = ProductoTiendaMapper.mapToInfoProducto(productoTienda);

                return ResponseEntity.ok(response);
        }

        /**
         * Obtiene la lista de tiendas donde un producto específico está disponible.
         *
         * @param nombreProducto Nombre del producto
         * @return Lista de tiendas con el producto disponible
         */
        @GetMapping("/tiendas")
        public List<TiendaProductoDisponibleResponse> obtenerTiendasPorProducto(
                        @RequestParam String nombreProducto) {

                log.info("Tiendas - nombreProducto='{}'", nombreProducto);

                List<TiendaProductoDisponibleResponse> tiendas = productoTiendaService
                                .obtenerTiendasPorNombreProducto(nombreProducto);

                log.info("Tiendas encontradas para el producto '{}'", tiendas.size(), nombreProducto);

                return tiendas;
        }
}
