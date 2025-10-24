package com.universidad.compusearch.specification;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.universidad.compusearch.entity.ProductoTienda;

public class ProductoTiendaSpecificationFactory {

    public static Specification<ProductoTienda> crearSpec(
            String categoria,
            String nombreTienda,
            String marca,
            BigDecimal precioMin,
            BigDecimal precioMax,
            Boolean disponible,
            Boolean habilitado,
            Map<String, String> filtrosExtra) {

        Specification<ProductoTienda> spec = ProductoTiendaSpecification.porHabilitado(habilitado)
                .and(ProductoTiendaSpecification.porNombreTienda(nombreTienda))
                .and(ProductoTiendaSpecification.porMarca(marca))
                .and(ProductoTiendaSpecification.porRangoPrecio(precioMin, precioMax))
                .and(ProductoTiendaSpecification.porDisponibilidad(disponible));

        if (categoria == null || categoria.isBlank()) {
            return spec;
        }

        switch (categoria.toLowerCase()) {
            case "tarjeta-video":
                spec = spec.and(ProductoTiendaSpecification.porCategoria("Tarjeta de Video"))
                        .and(TarjetaVideoSpecification.porFabricante(filtrosExtra.get("Fabricante GPU")))
                        .and(TarjetaVideoSpecification.porMemoriaVRAM(filtrosExtra.get("Memoria VRAM")));
                break;

            case "procesador":
                spec = spec.and(ProductoTiendaSpecification.porCategoria("Procesador"))
                        .and(ProcesadorSpecification.porSocket(filtrosExtra.get("Socket CPU")));
                break;

            case "almacenamiento":
                spec = spec.and(ProductoTiendaSpecification.porCategoria("Almacenamiento"))
                        .and(AlmacenamientoSpecification.porCapacidad(filtrosExtra.get("Capacidad Almacenamiento")))
                        .and(AlmacenamientoSpecification.porTipo(filtrosExtra.get("Tipo de Almacenamiento")));
                break;

            case "memoria":
                spec = spec.and(ProductoTiendaSpecification.porCategoria("Memoria RAM"))
                        .and(MemoriaSpecification.porCapacidad(filtrosExtra.get("Capacidad RAM")))
                        .and(MemoriaSpecification.porFrecuencia(filtrosExtra.get("Frecuencia RAM")))
                        .and(MemoriaSpecification.porTipo(filtrosExtra.get("Tipo RAM")));
                break;

            case "placa-madre":
                spec = spec.and(ProductoTiendaSpecification.porCategoria("Placa Madre"))
                        .and(PlacaMadreSpecification.porSocket(filtrosExtra.get("Socket Motherboard")))
                        .and(PlacaMadreSpecification.porFactor(filtrosExtra.get("Factor de Forma Motherboard")));
                break;

            case "fuente-poder":
                spec = spec.and(ProductoTiendaSpecification.porCategoria("Fuente de Poder"))
                        .and(FuentePoderSpecification.porCertificacion(filtrosExtra.get("Certificación PSU")))
                        .and(FuentePoderSpecification.porPotencia(filtrosExtra.get("Potencia PSU")));
                break;

            case "refrigeracion":
                spec = spec.and(ProductoTiendaSpecification.porCategoria("Refrigeración CPU"))
                        .and(RefrigeracionSpecification.porTipo(filtrosExtra.get("Tipo de Enfriamiento")))
                        .and(RefrigeracionSpecification
                                .porCompatibilidad(filtrosExtra.get("Compatibilidad Socket Cooler")));
                break;

            default:
                // No se añade filtro extra si no coincide con ninguna categoría
                break;
        }

        return spec;
    }
}
