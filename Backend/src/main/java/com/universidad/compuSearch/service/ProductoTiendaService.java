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

import com.universidad.compusearch.dto.DetalleAtributoResponse;
import com.universidad.compusearch.dto.ProductoBuildResponse;
import com.universidad.compusearch.dto.ProductoInfoResponse;
import com.universidad.compusearch.dto.ProductoTiendaResponse;
import com.universidad.compusearch.dto.TiendaProductoDisponibleResponse;
import com.universidad.compusearch.entity.ProductoAtributo;
import com.universidad.compusearch.entity.ProductoTienda;
import com.universidad.compusearch.exception.ProductoTiendaException;
import com.universidad.compusearch.repository.ProductoTiendaRepository;
import com.universidad.compusearch.specification.*;

import io.jsonwebtoken.lang.Collections;
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
                                                                .porFabricante(filtrosExtra.get("Fabricante GPU")))
                                                .and(TarjetaVideoSpecification
                                                                .porMemoriaVRAM(filtrosExtra.get("Memoria VRAM")));
                                break;

                        case "procesador":
                                spec = spec.and(ProductoTiendaSpecification.porCategoria("Procesador"))
                                                .and(ProcesadorSpecification.porSocket(filtrosExtra.get("Socket CPU")));
                                break;

                        case "almacenamiento":
                                spec = spec.and(ProductoTiendaSpecification.porCategoria("Almacenamiento"))
                                                .and(AlmacenamientoSpecification
                                                                .porCapacidad(filtrosExtra
                                                                                .get("Capacidad Almacenamiento")))
                                                .and(AlmacenamientoSpecification
                                                                .porTipo(filtrosExtra.get("Tipo de Almacenamiento")));
                                break;

                        case "memoria":
                                spec = spec.and(ProductoTiendaSpecification.porCategoria("Memoria RAM"))
                                                .and(MemoriaSpecification
                                                                .porCapacidad(filtrosExtra.get("Capacidad RAM")))
                                                .and(MemoriaSpecification
                                                                .porFrecuencia(filtrosExtra.get("Frecuencia RAM")))
                                                .and(MemoriaSpecification.porTipo(filtrosExtra.get("Tipo RAM")));
                                break;

                        case "placa-madre":
                                spec = spec.and(ProductoTiendaSpecification.porCategoria("Placa Madre"))
                                                .and(PlacaMadreSpecification
                                                                .porSocket(filtrosExtra.get("Socket Motherboard")))
                                                .and(PlacaMadreSpecification.porFactor(
                                                                filtrosExtra.get("Factor de Forma Motherboard")));
                                break;

                        case "fuente-poder":
                                spec = spec.and(ProductoTiendaSpecification.porCategoria("Fuente de Poder"))
                                                .and(FuentePoderSpecification
                                                                .porCertificacion(
                                                                                filtrosExtra.get("Certificación PSU")))
                                                .and(FuentePoderSpecification
                                                                .porPotencia(filtrosExtra.get("Potencia PSU")));
                                break;

                        case "refrigeracion":
                                spec = spec.and(ProductoTiendaSpecification.porCategoria("Refrigeración CPU"))
                                                .and(RefrigeracionSpecification
                                                                .porTipo(filtrosExtra.get("Tipo de Enfriamiento")))
                                                .and(RefrigeracionSpecification
                                                                .porCompatibilidad(filtrosExtra
                                                                                .get("Compatibilidad Socket Cooler")));
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

        // Búsqueda de un producto específico en una tienda específica
        public ProductoTienda buscarPorNombreProductoEspecifico(String nombreProducto, String nombreTienda) {
                log.info("Buscando producto '{}' en la tienda '{}'", nombreProducto, nombreTienda);

                return productoTiendaRepository.findByNombreProductoAndNombreTienda(nombreProducto, nombreTienda)
                                .orElseThrow(() -> ProductoTiendaException.notFoundProductoOrShop());
        }

        public ProductoInfoResponse mapToInfoProducto(ProductoTienda productoTienda) {
                ProductoInfoResponse dto = new ProductoInfoResponse();
                dto.setNombreProducto(productoTienda.getProducto().getNombre());
                dto.setMarca(productoTienda.getProducto().getMarca());
                dto.setModelo(productoTienda.getProducto().getModelo());
                dto.setDescripcion(productoTienda.getProducto().getDescripcion());
                dto.setUrlImagen(productoTienda.getUrlImagen());
                dto.setNombreTienda(productoTienda.getTienda().getNombre());
                dto.setStock(productoTienda.getStock());
                dto.setPrecio(productoTienda.getPrecio());
                dto.setUrlProducto(productoTienda.getUrlProducto());

                if (productoTienda.getProducto().getAtributos() != null) {
                        dto.setAtributos(matToDetalleAtributo(productoTienda.getProducto().getAtributos()));
                } else {
                        dto.setAtributos(Collections.emptyList());
                }

                return dto;
        }

        public List<DetalleAtributoResponse> matToDetalleAtributo(List<ProductoAtributo> atributos) {
                return atributos.stream()
                                .map(atributo -> {
                                        DetalleAtributoResponse dto = new DetalleAtributoResponse();
                                        dto.setNombreAtributo(atributo.getAtributo().getNombre());
                                        dto.setValor(atributo.getValor());
                                        return dto;
                                })
                                .collect(Collectors.toList());
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

                return productos.map(this::mapToProductoBuildResponse);
        }

        private ProductoBuildResponse mapToProductoBuildResponse(ProductoTienda productoTienda) {
                ProductoBuildResponse response = new ProductoBuildResponse();
                response.setIdProductoTienda(productoTienda.getIdProductoTienda());
                response.setNombreProducto(productoTienda.getProducto().getNombre());
                response.setPrecio(productoTienda.getPrecio());
                response.setStock(productoTienda.getStock());
                response.setNombreTienda(productoTienda.getTienda().getNombre());
                response.setUrlProducto(productoTienda.getUrlProducto());

                if (productoTienda.getProducto().getAtributos() != null) {
                        List<DetalleAtributoResponse> atributos = productoTienda.getProducto()
                                        .getAtributos()
                                        .stream()
                                        .map(attr -> new DetalleAtributoResponse(attr.getAtributo().getNombre(), attr.getValor()))
                                        .collect(Collectors.toList());
                        response.setDetalles(atributos);
                } else {
                        response.setDetalles(Collections.emptyList());
                }

                return response;
        }

        

}
