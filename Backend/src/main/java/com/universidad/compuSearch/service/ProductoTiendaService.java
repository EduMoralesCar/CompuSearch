package com.universidad.compusearch.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.dto.ProductoTiendaResponse;
import com.universidad.compusearch.entity.ProductoTienda;
import com.universidad.compusearch.repository.ProductoTiendaRepository;
import com.universidad.compusearch.specification.AlmacenamientoSpecification;
import com.universidad.compusearch.specification.FuentePoderSpecification;
import com.universidad.compusearch.specification.MemoriaSpecification;
import com.universidad.compusearch.specification.PlacaMadreSpecification;
import com.universidad.compusearch.specification.ProcesadorSpecification;
import com.universidad.compusearch.specification.ProductoTiendaSpecification;
import com.universidad.compusearch.specification.RefrigeracionSpecification;
import com.universidad.compusearch.specification.TarjetaVideoSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoTiendaService {

        private static final Logger logger = LoggerFactory.getLogger(ProductoTiendaService.class);
        private final ProductoTiendaRepository productoTiendaRepository;

        public Page<ProductoTienda> filtrarConParametros(
                        String nombreTienda,
                        String marca,
                        BigDecimal precioMax,
                        BigDecimal precioMin,
                        Boolean disponible,
                        Boolean habilitado,
                        int page,
                        int size) {

                logger.info("Aplicando filtros - nombreTienda: {}, marca: {}, precioMin: {}, precioMax: {}, disponible: {}",
                                nombreTienda, marca, precioMin,
                                precioMax, disponible);

                Pageable pageable = PageRequest.of(page, size);

                Specification<ProductoTienda> spec = ProductoTiendaSpecification.porHabilitado(habilitado)
                                .and(ProductoTiendaSpecification.porNombreTienda(nombreTienda))
                                .and(ProductoTiendaSpecification.porMarca(marca))
                                .and(ProductoTiendaSpecification.porRangoPrecio(precioMin, precioMax))
                                .and(ProductoTiendaSpecification.porDisponibilidad(disponible));

                Page<ProductoTienda> productos = productoTiendaRepository.findAll(spec, pageable);

                logger.info("Se encontraron {} productos con los filtros aplicados", productos.getTotalElements());

                return productos;
        }

        public Page<ProductoTienda> filtrarTarjetasVideo(
                        String nombreTienda,
                        String marca,
                        BigDecimal precioMax,
                        BigDecimal precioMin,
                        Boolean disponible,
                        Boolean habilitado,
                        String fabricante,
                        String memoriaVRAM,
                        int page,
                        int size) {

                logger.info("Aplicando filtros - nombreTienda: {}, marca: {}, precioMin: {}, precioMax: {}, disponible: {}, fabricante: {}, memoriaVRAM: {}",
                                nombreTienda, marca, precioMin, precioMax, disponible, fabricante, memoriaVRAM);

                Pageable pageable = PageRequest.of(page, size);

                Specification<ProductoTienda> spec = ProductoTiendaSpecification.porHabilitado(habilitado)
                                .and(ProductoTiendaSpecification.porNombreTienda(nombreTienda))
                                .and(ProductoTiendaSpecification.porMarca(marca))
                                .and(ProductoTiendaSpecification.porRangoPrecio(precioMin, precioMax))
                                .and(ProductoTiendaSpecification.porDisponibilidad(disponible))
                                .and(ProductoTiendaSpecification.porCategoria("Tarjeta de Video"))
                                .and(TarjetaVideoSpecification.porFabricante(fabricante))
                                .and(TarjetaVideoSpecification.porMemoriaVRAM(memoriaVRAM));

                Page<ProductoTienda> productos = productoTiendaRepository.findAll(spec, pageable);

                logger.info("Se encontraron {} productos con los filtros aplicados", productos.getTotalElements());

                return productos;
        }

        public Page<ProductoTienda> filtrarProcesador(
                        String nombreTienda,
                        String marca,
                        BigDecimal precioMax,
                        BigDecimal precioMin,
                        Boolean disponible,
                        Boolean habilitado,
                        String socket,
                        int page,
                        int size) {

                logger.info("Aplicando filtros - nombreTienda: {}, marca: {}, precioMin: {}, precioMax: {}, disponible: {}, socket: {}",
                                nombreTienda, marca, precioMin, precioMax, disponible, socket);

                Pageable pageable = PageRequest.of(page, size);

                Specification<ProductoTienda> spec = ProductoTiendaSpecification.porHabilitado(habilitado)
                                .and(ProductoTiendaSpecification.porNombreTienda(nombreTienda))
                                .and(ProductoTiendaSpecification.porMarca(marca))
                                .and(ProductoTiendaSpecification.porRangoPrecio(precioMin, precioMax))
                                .and(ProductoTiendaSpecification.porDisponibilidad(disponible))
                                .and(ProductoTiendaSpecification.porCategoria("Procesador"))
                                .and(ProcesadorSpecification.porSocket(socket));

                Page<ProductoTienda> productos = productoTiendaRepository.findAll(spec, pageable);

                logger.info("Se encontraron {} productos con los filtros aplicados", productos.getTotalElements());

                return productos;
        }

        public Page<ProductoTienda> filtrarAlmacenamiento(
                        String nombreTienda,
                        String marca,
                        BigDecimal precioMax,
                        BigDecimal precioMin,
                        Boolean disponible,
                        Boolean habilitado,
                        String capacidad,
                        String tipo,
                        int page,
                        int size) {

                logger.info("Aplicando filtros - nombreTienda: {}, marca: {}, precioMin: {}, precioMax: {}, disponible: {}, capacidad: {}, tipo: {}",
                                nombreTienda, marca, precioMin, precioMax, disponible, capacidad, tipo);

                Pageable pageable = PageRequest.of(page, size);

                Specification<ProductoTienda> spec = ProductoTiendaSpecification.porHabilitado(habilitado)
                                .and(ProductoTiendaSpecification.porNombreTienda(nombreTienda))
                                .and(ProductoTiendaSpecification.porMarca(marca))
                                .and(ProductoTiendaSpecification.porRangoPrecio(precioMin, precioMax))
                                .and(ProductoTiendaSpecification.porDisponibilidad(disponible))
                                .and(ProductoTiendaSpecification.porCategoria("Almacenamiento"))
                                .and(AlmacenamientoSpecification.porCapacidad(capacidad))
                                .and(AlmacenamientoSpecification.porTipo(tipo));

                Page<ProductoTienda> productos = productoTiendaRepository.findAll(spec, pageable);

                logger.info("Se encontraron {} productos con los filtros aplicados", productos.getTotalElements());

                return productos;
        }

        public Page<ProductoTienda> filtrarMemoria(
                        String nombreTienda,
                        String marca,
                        BigDecimal precioMax,
                        BigDecimal precioMin,
                        Boolean disponible,
                        Boolean habilitado,
                        String capacidad,
                        String frecuencia,
                        String tipo,
                        int page,
                        int size) {

                logger.info("Aplicando filtros - nombreTienda: {}, marca: {}, precioMin: {}, precioMax: {}, disponible: {}, capacidad: {}, frecuencia: {}, tipo: {}",
                                nombreTienda, marca, precioMin, precioMax, disponible, capacidad, frecuencia, tipo);

                Pageable pageable = PageRequest.of(page, size);

                Specification<ProductoTienda> spec = ProductoTiendaSpecification.porHabilitado(habilitado)
                                .and(ProductoTiendaSpecification.porNombreTienda(nombreTienda))
                                .and(ProductoTiendaSpecification.porMarca(marca))
                                .and(ProductoTiendaSpecification.porRangoPrecio(precioMin, precioMax))
                                .and(ProductoTiendaSpecification.porDisponibilidad(disponible))
                                .and(ProductoTiendaSpecification.porCategoria("Memoria RAM"))
                                .and(MemoriaSpecification.porCapacidad(capacidad))
                                .and(MemoriaSpecification.porFrecuencia(frecuencia))
                                .and(MemoriaSpecification.porTipo(tipo));

                Page<ProductoTienda> productos = productoTiendaRepository.findAll(spec, pageable);

                logger.info("Se encontraron {} productos con los filtros aplicados", productos.getTotalElements());

                return productos;
        }

        public Page<ProductoTienda> filtrarPlacaMadre(
                        String nombreTienda,
                        String marca,
                        BigDecimal precioMax,
                        BigDecimal precioMin,
                        Boolean disponible,
                        Boolean habilitado,
                        String socket,
                        String factor,
                        int page,
                        int size) {

                logger.info("Aplicando filtros - nombreTienda: {}, marca: {}, precioMin: {}, precioMax: {}, disponible: {}, socket: {}, factor: {}",
                                nombreTienda, marca, precioMin, precioMax, disponible, socket, factor);

                Pageable pageable = PageRequest.of(page, size);

                Specification<ProductoTienda> spec = ProductoTiendaSpecification.porHabilitado(habilitado)
                                .and(ProductoTiendaSpecification.porNombreTienda(nombreTienda))
                                .and(ProductoTiendaSpecification.porMarca(marca))
                                .and(ProductoTiendaSpecification.porRangoPrecio(precioMin, precioMax))
                                .and(ProductoTiendaSpecification.porDisponibilidad(disponible))
                                .and(ProductoTiendaSpecification.porCategoria("Placa Madre"))
                                .and(PlacaMadreSpecification.porSocket(socket))
                                .and(PlacaMadreSpecification.porFactor(factor));

                Page<ProductoTienda> productos = productoTiendaRepository.findAll(spec, pageable);

                logger.info("Se encontraron {} productos con los filtros aplicados", productos.getTotalElements());

                return productos;
        }

        public Page<ProductoTienda> filtrarFuentePoder(
                        String nombreTienda,
                        String marca,
                        BigDecimal precioMax,
                        BigDecimal precioMin,
                        Boolean disponible,
                        Boolean habilitado,
                        String certificacion,
                        String potencia,
                        int page,
                        int size) {

                logger.info("Aplicando filtros - nombreTienda: {}, marca: {}, precioMin: {}, precioMax: {}, disponible: {}, certificacion: {}, potencia: {}",
                                nombreTienda, marca, precioMin, precioMax, disponible, certificacion, potencia);

                Pageable pageable = PageRequest.of(page, size);

                Specification<ProductoTienda> spec = ProductoTiendaSpecification.porHabilitado(habilitado)
                                .and(ProductoTiendaSpecification.porNombreTienda(nombreTienda))
                                .and(ProductoTiendaSpecification.porMarca(marca))
                                .and(ProductoTiendaSpecification.porRangoPrecio(precioMin, precioMax))
                                .and(ProductoTiendaSpecification.porDisponibilidad(disponible))
                                .and(ProductoTiendaSpecification.porCategoria("Fuente de Poder"))
                                .and(FuentePoderSpecification.porCertificacion(certificacion))
                                .and(FuentePoderSpecification.porPotencia(potencia));

                Page<ProductoTienda> productos = productoTiendaRepository.findAll(spec, pageable);

                logger.info("Se encontraron {} productos con los filtros aplicados", productos.getTotalElements());

                return productos;
        }

        public Page<ProductoTienda> filtrarRefrigeracion(
                        String nombreTienda,
                        String marca,
                        BigDecimal precioMax,
                        BigDecimal precioMin,
                        Boolean disponible,
                        Boolean habilitado,
                        String tipo,
                        String compatibilidad,
                        int page,
                        int size) {

                logger.info("Aplicando filtros - nombreTienda: {}, marca: {}, precioMin: {}, precioMax: {}, disponible: {}, tipo: {}, compatibilidad: {}",
                                nombreTienda, marca, precioMin, precioMax, disponible, tipo, compatibilidad);

                Pageable pageable = PageRequest.of(page, size);

                Specification<ProductoTienda> spec = ProductoTiendaSpecification.porHabilitado(habilitado)
                                .and(ProductoTiendaSpecification.porNombreTienda(nombreTienda))
                                .and(ProductoTiendaSpecification.porMarca(marca))
                                .and(ProductoTiendaSpecification.porRangoPrecio(precioMin, precioMax))
                                .and(ProductoTiendaSpecification.porDisponibilidad(disponible))
                                .and(ProductoTiendaSpecification.porCategoria("Refrigeración CPU"))
                                .and(RefrigeracionSpecification.porTipo(tipo))
                                .and(RefrigeracionSpecification.porCompatibilidad(compatibilidad));

                Page<ProductoTienda> productos = productoTiendaRepository.findAll(spec, pageable);

                logger.info("Se encontraron {} productos con los filtros aplicados", productos.getTotalElements());

                return productos;
        }

        public Page<ProductoTienda> buscarPorNombreProducto(
                        String nombreProducto,
                        int page,
                        int size) {

                logger.info("Buscando productos por nombre o parecido: '{}'", nombreProducto);

                Pageable pageable = PageRequest.of(page, size);

                Specification<ProductoTienda> spec = ProductoTiendaSpecification
                                .porNombreProducto(nombreProducto);

                Page<ProductoTienda> productos = productoTiendaRepository.findAll(spec, pageable);

                logger.info("Se encontraron {} productos que coinciden con el nombre '{}'",
                                productos.getTotalElements(), nombreProducto);

                return productos;
        }

        public ProductoTiendaResponse mapToResponse(ProductoTienda productoTienda) {
                return new ProductoTiendaResponse(
                                productoTienda.getProducto().getNombre(),
                                productoTienda.getPrecio(),
                                productoTienda.getStock(),
                                productoTienda.getUrlImagen(),
                                productoTienda.getTienda().getNombre());
        }
}
