package com.universidad.compusearch.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.dto.ProductoTiendaResponse;
import com.universidad.compusearch.entity.ProductoTienda;
import com.universidad.compusearch.repository.ProductoTiendaRepository;
import com.universidad.compusearch.specification.*;

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

                // Base común para todos los productos
                Specification<ProductoTienda> spec = ProductoTiendaSpecification.porHabilitado(habilitado)
                                .and(ProductoTiendaSpecification.porNombreTienda(nombreTienda))
                                .and(ProductoTiendaSpecification.porMarca(marca))
                                .and(ProductoTiendaSpecification.porRangoPrecio(precioMin, precioMax))
                                .and(ProductoTiendaSpecification.porDisponibilidad(disponible));

                if (categoria == null || categoria.isBlank()) {
                        log.info("Sin categoría especificada, devolviendo todos los productos con filtros comunes");
                        return productoTiendaRepository.findAll(spec, pageable);
                }
                // Selecciona los filtros específicos según la categoría
                switch (categoria.toLowerCase()) {
                        case "tarjeta-video":
                                spec = spec.and(ProductoTiendaSpecification.porCategoria("Tarjeta de Video"))
                                                .and(TarjetaVideoSpecification
                                                                .porFabricante(filtrosExtra.get("fabricante")))
                                                .and(TarjetaVideoSpecification
                                                                .porMemoriaVRAM(filtrosExtra.get("memoriaVRAM")));
                                break;

                        case "procesador":
                                spec = spec.and(ProductoTiendaSpecification.porCategoria("Procesador"))
                                                .and(ProcesadorSpecification.porSocket(filtrosExtra.get("socket")));
                                break;

                        case "almacenamiento":
                                spec = spec.and(ProductoTiendaSpecification.porCategoria("Almacenamiento"))
                                                .and(AlmacenamientoSpecification
                                                                .porCapacidad(filtrosExtra.get("capacidad")))
                                                .and(AlmacenamientoSpecification.porTipo(filtrosExtra.get("tipo")));
                                break;

                        case "memoria":
                                spec = spec.and(ProductoTiendaSpecification.porCategoria("Memoria RAM"))
                                                .and(MemoriaSpecification.porCapacidad(filtrosExtra.get("capacidad")))
                                                .and(MemoriaSpecification.porFrecuencia(filtrosExtra.get("frecuencia")))
                                                .and(MemoriaSpecification.porTipo(filtrosExtra.get("tipo")));
                                break;

                        case "placa-madre":
                                spec = spec.and(ProductoTiendaSpecification.porCategoria("Placa Madre"))
                                                .and(PlacaMadreSpecification.porSocket(filtrosExtra.get("socket")))
                                                .and(PlacaMadreSpecification.porFactor(filtrosExtra.get("factor")));
                                break;

                        case "fuente-poder":
                                spec = spec.and(ProductoTiendaSpecification.porCategoria("Fuente de Poder"))
                                                .and(FuentePoderSpecification
                                                                .porCertificacion(filtrosExtra.get("certificacion")))
                                                .and(FuentePoderSpecification
                                                                .porPotencia(filtrosExtra.get("potencia")));
                                break;

                        case "refrigeracion":
                                spec = spec.and(ProductoTiendaSpecification.porCategoria("Refrigeración CPU"))
                                                .and(RefrigeracionSpecification.porTipo(filtrosExtra.get("tipo")))
                                                .and(RefrigeracionSpecification
                                                                .porCompatibilidad(filtrosExtra.get("compatibilidad")));
                                break;

                        default:
                                log.warn("Categoría no reconocida: {}", categoria);
                                // No se añade filtro extra si no coincide con ninguna categoría
                                break;
                }

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

        // Conversión de entidad a DTO de respuesta
        public ProductoTiendaResponse mapToResponse(ProductoTienda productoTienda) {
                return new ProductoTiendaResponse(
                                productoTienda.getIdProductoTienda(),
                                productoTienda.getProducto().getNombre(),
                                productoTienda.getPrecio(),
                                productoTienda.getStock(),
                                productoTienda.getUrlImagen(),
                                productoTienda.getTienda().getNombre());
        }
}
