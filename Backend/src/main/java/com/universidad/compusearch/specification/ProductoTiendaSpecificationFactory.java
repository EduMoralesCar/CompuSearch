package com.universidad.compusearch.specification;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.universidad.compusearch.entity.ProductoTienda;

/**
 * Fábrica de especificaciones para construir dinámicamente filtros de búsqueda
 * sobre la entidad {@link ProductoTienda}.
 * 
 * <p>Esta clase combina múltiples {@link Specification} dependiendo de los parámetros
 * proporcionados y de la categoría del producto. Permite construir consultas flexibles
 * y reutilizables usando la API de Criteria de Spring Data JPA.</p>
 * 
 * <p>Soporta distintas categorías de productos como tarjetas de video, procesadores,
 * memorias RAM, placas madre, fuentes de poder, refrigeraciones, etc., agregando los
 * filtros específicos para cada tipo de producto.</p>
 * 
 * Ejemplo de uso:
 * <pre>{@code
 * Map<String, String> filtros = Map.of(
 *     "Fabricante GPU", "NVIDIA",
 *     "Memoria VRAM", "8 GB"
 * );
 *
 * Specification<ProductoTienda> spec = ProductoTiendaSpecificationFactory.crearSpec(
 *     "tarjeta-video",
 *     "TechStore",
 *     "ASUS",
 *     new BigDecimal("1000"),
 *     new BigDecimal("3000"),
 *     true,
 *     true,
 *     filtros
 * );
 * }</pre>
 * 
 */
public class ProductoTiendaSpecificationFactory {

    /**
     * Crea una {@link Specification} combinada que filtra productos de tienda
     * según los parámetros proporcionados y la categoría del producto.
     * 
     * <p>La especificación base incluye filtros por tienda, marca, rango de precios,
     * disponibilidad y estado de habilitación. Además, si se proporciona una categoría,
     * se agregan los filtros específicos para dicha categoría usando otras clases de
     * especificación.</p>
     * 
     * @param categoria      nombre de la categoría del producto (por ejemplo, "tarjeta-video",
     *                       "procesador", "memoria", etc.).
     * @param nombreTienda   nombre de la tienda donde se ofrece el producto.
     * @param marca          marca del producto (por ejemplo, "ASUS", "Corsair").
     * @param precioMin      precio mínimo permitido en el filtro.
     * @param precioMax      precio máximo permitido en el filtro.
     * @param disponible     si el producto está disponible en stock.
     * @param habilitado     si el producto está habilitado en el sistema.
     * @param filtrosExtra   mapa con los filtros adicionales específicos de cada categoría
     *                       (por ejemplo, "Fabricante GPU", "Memoria VRAM").
     * @return una {@link Specification} que puede utilizarse en un repositorio
     *         de Spring Data JPA para realizar consultas dinámicas.
     */
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

            case "otros":
                spec = spec.and(ProductoTiendaSpecification.porCategoria("Otros"));
                break;

            default:
                // No se añade filtro extra si no coincide con ninguna categoría
                break;
        }

        return spec;
    }
}
