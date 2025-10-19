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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/componentes")
@RequiredArgsConstructor
public class ProductoTiendaController {

        private final ProductoTiendaService productoTiendaService;

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

                filtrosExtra.remove("categoria");
                filtrosExtra.remove("nombreTienda");
                filtrosExtra.remove("precioMax");
                filtrosExtra.remove("precioMin");
                filtrosExtra.remove("disponible");
                filtrosExtra.remove("marca");
                filtrosExtra.remove("page");
                filtrosExtra.remove("size");

                log.info("Filtrando categoría={} con parámetros extra={}", categoria, filtrosExtra);

                Page<ProductoTiendaResponse> resultados = productoTiendaService
                                .filtrarPorCategoria(categoria, nombreTienda, marca, precioMax, precioMin, disponible,
                                                true, filtrosExtra, page, size)
                                .map(productoTiendaService::mapToResponse);

                if (resultados.isEmpty()) {
                        log.warn("No se encontraron productos para la categoría {} con filtros {}", categoria,
                                        filtrosExtra);
                        return ResponseEntity.ok(Page.empty());
                }

                log.info("Se encontraron {} productos para la categoría {}", resultados.getTotalElements(),
                                categoria);
                return ResponseEntity.ok(resultados);
        }

        @GetMapping("/buscar")
        public ResponseEntity<Page<ProductoTiendaResponse>> buscarPorNombre(
                        @RequestParam String nombre,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "15") int size) {

                log.info("Buscando productos por nombre: '{}', page={}, size={}", nombre, page, size);

                Page<ProductoTiendaResponse> resultados = productoTiendaService
                                .buscarPorNombreProducto(nombre, page, size)
                                .map(productoTiendaService::mapToResponse);

                if (resultados.isEmpty()) {
                        log.warn("No se encontraron productos con el nombre: '{}'", nombre);
                        return ResponseEntity.ok(Page.empty());
                }

                log.info("Se encontraron {} productos que coinciden con '{}'", resultados.getTotalElements(),
                                nombre);
                return ResponseEntity.ok(resultados);
        }

        @GetMapping("/info")
        public ResponseEntity<ProductoInfoResponse> obtenerInfoProductoPorTienda(
                        @RequestParam String nombreProducto,
                        @RequestParam String nombreTienda) {

                ProductoTienda productoTienda = productoTiendaService
                                .buscarPorNombreProductoEspecifico(nombreProducto, nombreTienda);

                ProductoInfoResponse response = productoTiendaService.mapToInfoProducto(productoTienda);

                return ResponseEntity.ok(response);
        }

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
