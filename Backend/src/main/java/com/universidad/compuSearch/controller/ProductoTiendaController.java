package com.universidad.compusearch.controller;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.dto.ProductoTiendaResponse;
import com.universidad.compusearch.service.ProductoTiendaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/productosTiendas")
@RequiredArgsConstructor
public class ProductoTiendaController {

        private static final Logger logger = LoggerFactory.getLogger(ProductoTiendaController.class);
        private final ProductoTiendaService productoTiendaService;

        @GetMapping
        public ResponseEntity<Page<ProductoTiendaResponse>> filtrarProductos(
                        @RequestParam(required = false) String nombreTienda,
                        @RequestParam(required = false) BigDecimal precioMax,
                        @RequestParam(required = false) BigDecimal precioMin,
                        @RequestParam(required = false) Boolean disponible,
                        @RequestParam(required = false) String marca,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "15") int size) {

                logger.info(
                                "Solicitud de filtrado general - nombreTienda={}, marca={}, precioMax={}, precioMin={}, disponible={}, page={}, size={}",
                                nombreTienda, marca, precioMax, precioMin, disponible, page, size);

                Page<ProductoTiendaResponse> productos = productoTiendaService
                                .filtrarConParametros(nombreTienda, marca, precioMax, precioMin, disponible, true, page,
                                                size)
                                .map(productoTiendaService::mapToResponse);

                if (productos.isEmpty()) {
                        logger.warn("No se encontraron productos con los filtros aplicados");
                        return ResponseEntity.ok(Page.empty());
                }

                logger.info("Se encontraron {} productos con los filtros aplicados", productos.getTotalElements());
                return ResponseEntity.ok(productos);
        }

        @GetMapping("/tarjeta-video")
        public ResponseEntity<Page<ProductoTiendaResponse>> filtrarTarjetasVideo(
                        @RequestParam(required = false) String nombreTienda,
                        @RequestParam(required = false) BigDecimal precioMax,
                        @RequestParam(required = false) BigDecimal precioMin,
                        @RequestParam(required = false) Boolean disponible,
                        @RequestParam(required = false) String marca,
                        @RequestParam(required = false) String fabricante,
                        @RequestParam(required = false) String memoriaVRAM,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "15") int size) {

                logger.info(
                                "Filtrando tarjetas de video - nombreTienda={}, marca={}, fabricante={}, memoriaVRAM={}, precioMin={}, precioMax={}, disponible={}, page={}, size={}",
                                nombreTienda, marca, fabricante, memoriaVRAM, precioMin, precioMax, disponible, page,
                                size);

                Page<ProductoTiendaResponse> resultados = productoTiendaService.filtrarTarjetasVideo(
                                nombreTienda, marca, precioMax, precioMin, disponible, true, fabricante, memoriaVRAM,
                                page, size)
                                .map(productoTiendaService::mapToResponse);

                if (resultados.isEmpty()) {
                        logger.warn("No se encontraron tarjetas de video con los filtros aplicados");
                        return ResponseEntity.ok(Page.empty());
                }

                logger.info("Se encontraron {} tarjetas de video", resultados.getTotalElements());
                return ResponseEntity.ok(resultados);
        }

        @GetMapping("/procesador")
        public ResponseEntity<Page<ProductoTiendaResponse>> filtrarProcesador(
                        @RequestParam(required = false) String nombreTienda,
                        @RequestParam(required = false) BigDecimal precioMax,
                        @RequestParam(required = false) BigDecimal precioMin,
                        @RequestParam(required = false) Boolean disponible,
                        @RequestParam(required = false) String marca,
                        @RequestParam(required = false) String socket,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "15") int size) {

                logger.info(
                                "Filtrando tarjetas de video - nombreTienda={}, marca={}, socket={}, precioMin={}, precioMax={}, disponible={}, page={}, size={}",
                                nombreTienda, marca, socket, precioMin, precioMax, disponible, page, size);

                Page<ProductoTiendaResponse> resultados = productoTiendaService.filtrarProcesador(
                                nombreTienda, marca, precioMax, precioMin, disponible, true, socket, page, size)
                                .map(productoTiendaService::mapToResponse);

                if (resultados.isEmpty()) {
                        logger.warn("No se encontraron procesadores con los filtros aplicados");
                        return ResponseEntity.ok(Page.empty());
                }

                logger.info("Se encontraron {} procesadores", resultados.getTotalElements());
                return ResponseEntity.ok(resultados);
        }

        @GetMapping("/almacenamiento")
        public ResponseEntity<Page<ProductoTiendaResponse>> filtrarAlmacenamiento(
                        @RequestParam(required = false) String nombreTienda,
                        @RequestParam(required = false) BigDecimal precioMax,
                        @RequestParam(required = false) BigDecimal precioMin,
                        @RequestParam(required = false) Boolean disponible,
                        @RequestParam(required = false) String marca,
                        @RequestParam(required = false) String capacidad,
                        @RequestParam(required = false) String tipo,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "15") int size) {

                logger.info(
                                "Filtrando Almacenamiento - nombreTienda={}, marca={}, tipo={}, capacidad={}, precioMin={}, precioMax={}, disponible={}, page={}, size={}",
                                nombreTienda, marca, tipo, capacidad, precioMin, precioMax, disponible, page, size);

                Page<ProductoTiendaResponse> resultados = productoTiendaService.filtrarAlmacenamiento(
                                nombreTienda, marca, precioMax, precioMin, disponible, true, capacidad, tipo, page,
                                size)
                                .map(productoTiendaService::mapToResponse);

                if (resultados.isEmpty()) {
                        logger.warn("No se encontraron almacenamientos con los filtros aplicados");
                        return ResponseEntity.ok(Page.empty());
                }

                logger.info("Se encontraron {} almacenamientos", resultados.getTotalElements());
                return ResponseEntity.ok(resultados);
        }

        @GetMapping("/memoria")
        public ResponseEntity<Page<ProductoTiendaResponse>> filtrarMemoria(
                        @RequestParam(required = false) String nombreTienda,
                        @RequestParam(required = false) BigDecimal precioMax,
                        @RequestParam(required = false) BigDecimal precioMin,
                        @RequestParam(required = false) Boolean disponible,
                        @RequestParam(required = false) String marca,
                        @RequestParam(required = false) String capacidad,
                        @RequestParam(required = false) String frecuencia,
                        @RequestParam(required = false) String tipo,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "15") int size) {

                logger.info(
                                "Filtrando Memoria - nombreTienda={}, marca={}, tipo={}, capacidad={}, frecuencia={}, precioMin={}, precioMax={}, disponible={}, page={}, size={}",
                                nombreTienda, marca, tipo, capacidad, frecuencia, precioMin, precioMax, disponible,
                                page, size);

                Page<ProductoTiendaResponse> resultados = productoTiendaService.filtrarMemoria(
                                nombreTienda, marca, precioMax, precioMin, disponible, true, capacidad, frecuencia,
                                tipo, page, size)
                                .map(productoTiendaService::mapToResponse);

                if (resultados.isEmpty()) {
                        logger.warn("No se encontraron memorias con los filtros aplicados");
                        return ResponseEntity.ok(Page.empty());
                }

                logger.info("Se encontraron {} memorias", resultados.getTotalElements());
                return ResponseEntity.ok(resultados);
        }

        @GetMapping("/placa-madre")
        public ResponseEntity<Page<ProductoTiendaResponse>> filtrarPlacaMadre(
                        @RequestParam(required = false) String nombreTienda,
                        @RequestParam(required = false) BigDecimal precioMax,
                        @RequestParam(required = false) BigDecimal precioMin,
                        @RequestParam(required = false) Boolean disponible,
                        @RequestParam(required = false) String marca,
                        @RequestParam(required = false) String socket,
                        @RequestParam(required = false) String factor,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "15") int size) {

                logger.info(
                                "Filtrando Placa Madre - nombreTienda={}, marca={}, socket={}, factor={}, precioMin={}, precioMax={}, disponible={}, page={}, size={}",
                                nombreTienda, marca, socket, factor, precioMin, precioMax, disponible, page, size);

                Page<ProductoTiendaResponse> resultados = productoTiendaService.filtrarPlacaMadre(
                                nombreTienda, marca, precioMax, precioMin, disponible, true, socket, factor, page, size)
                                .map(productoTiendaService::mapToResponse);

                if (resultados.isEmpty()) {
                        logger.warn("No se encontraron placas madres con los filtros aplicados");
                        return ResponseEntity.ok(Page.empty());
                }

                logger.info("Se encontraron {} placas madres", resultados.getTotalElements());
                return ResponseEntity.ok(resultados);
        }

        @GetMapping("/fuente-poder")
        public ResponseEntity<Page<ProductoTiendaResponse>> filtrarFuentePoder(
                        @RequestParam(required = false) String nombreTienda,
                        @RequestParam(required = false) BigDecimal precioMax,
                        @RequestParam(required = false) BigDecimal precioMin,
                        @RequestParam(required = false) Boolean disponible,
                        @RequestParam(required = false) String marca,
                        @RequestParam(required = false) String certificacion,
                        @RequestParam(required = false) String potencia,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "15") int size) {

                logger.info(
                                "Filtrando Placa Madre - nombreTienda={}, marca={}, certificacion={}, potencia={}, precioMin={}, precioMax={}, disponible={}, page={}, size={}",
                                nombreTienda, marca, certificacion, potencia, precioMin, precioMax, disponible, page,
                                size);

                Page<ProductoTiendaResponse> resultados = productoTiendaService.filtrarFuentePoder(
                                nombreTienda, marca, precioMax, precioMin, disponible, true, certificacion, potencia,
                                page, size)
                                .map(productoTiendaService::mapToResponse);

                if (resultados.isEmpty()) {
                        logger.warn("No se encontraron placas madres con los filtros aplicados");
                        return ResponseEntity.ok(Page.empty());
                }

                logger.info("Se encontraron {} placas madres", resultados.getTotalElements());
                return ResponseEntity.ok(resultados);
        }

        @GetMapping("/refrigeracion")
        public ResponseEntity<Page<ProductoTiendaResponse>> filtrarRefrigeracion(
                        @RequestParam(required = false) String nombreTienda,
                        @RequestParam(required = false) BigDecimal precioMax,
                        @RequestParam(required = false) BigDecimal precioMin,
                        @RequestParam(required = false) Boolean disponible,
                        @RequestParam(required = false) String marca,
                        @RequestParam(required = false) String tipo,
                        @RequestParam(required = false) String compatibilidad,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "15") int size) {

                logger.info(
                                "Filtrando Refrigeracion - nombreTienda={}, marca={}, tipo={}, compatibilidad={}, precioMin={}, precioMax={}, disponible={}, page={}, size={}",
                                nombreTienda, marca, tipo, compatibilidad, precioMin, precioMax, disponible, page,
                                size);

                Page<ProductoTiendaResponse> resultados = productoTiendaService.filtrarRefrigeracion(
                                nombreTienda, marca, precioMax, precioMin, disponible, true, tipo, compatibilidad, page,
                                size)
                                .map(productoTiendaService::mapToResponse);

                if (resultados.isEmpty()) {
                        logger.warn("No se encontraron refrigeraciones con los filtros aplicados");
                        return ResponseEntity.ok(Page.empty());
                }

                logger.info("Se encontraron {} refrigeraciones", resultados.getTotalElements());
                return ResponseEntity.ok(resultados);
        }

        @GetMapping("/buscar")
        public ResponseEntity<Page<ProductoTiendaResponse>> buscarPorNombre(
                        @RequestParam String nombre,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "15") int size) {

                logger.info("Buscando productos por nombre: '{}', page={}, size={}", nombre, page, size);

                Page<ProductoTiendaResponse> resultados = productoTiendaService
                                .buscarPorNombreProducto(nombre, page, size)
                                .map(productoTiendaService::mapToResponse);

                if (resultados.isEmpty()) {
                        logger.warn("No se encontraron productos con el nombre: '{}'", nombre);
                        return ResponseEntity.ok(Page.empty());
                }

                logger.info("Se encontraron {} productos que coinciden con '{}'", resultados.getTotalElements(),
                                nombre);
                return ResponseEntity.ok(resultados);
        }

}
