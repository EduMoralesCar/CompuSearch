package com.universidad.compusearch.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.universidad.compusearch.dto.ProductoTiendaResponse;
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
                        @RequestParam(required = false) Map<String, String> filtrosExtra
        ) {

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
}
