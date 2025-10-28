package com.universidad.compusearch.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.universidad.compusearch.entity.ProductoTienda;

/**
 * Clase de utilidades que define diversas {@link Specification Specifications}
 * para filtrar entidades {@link ProductoTienda} según diferentes criterios.
 * 
 * <p>Estas especificaciones permiten construir consultas dinámicas utilizando
 * la API de Criteria de Spring Data JPA. Cada método devuelve una especificación
 * que puede combinarse con otras usando los métodos {@code and()} o {@code or()}.</p>
 * 
 * <p>Ejemplo de uso:</p>
 * <pre>{@code
 * Specification<ProductoTienda> spec = ProductoTiendaSpecification
 *     .porNombreTienda("TechStore")
 *     .and(ProductoTiendaSpecification.porMarca("ASUS"))
 *     .and(ProductoTiendaSpecification.porRangoPrecio(new BigDecimal("500"), new BigDecimal("2000")));
 * }</pre>
 * 
 */
public class ProductoTiendaSpecification {

    /**
     * Crea una especificación que filtra los productos por el nombre de la tienda.
     * 
     * @param nombreTienda nombre de la tienda (por ejemplo, "CompuWorld").
     * @return una {@link Specification} que filtra los productos que pertenecen a la tienda indicada,
     *         o {@code null} si el parámetro es {@code null}.
     */
    public static Specification<ProductoTienda> porNombreTienda(String nombreTienda) {
        return (root, query, cb) -> {
            if (nombreTienda == null)
                return null;
            return cb.equal(root.get("tienda").get("nombre"), nombreTienda);
        };
    }

    /**
     * Crea una especificación que filtra los productos por su marca.
     * 
     * @param marca nombre de la marca del producto (por ejemplo, "ASUS", "Gigabyte").
     * @return una {@link Specification} que filtra los productos por marca,
     *         o {@code null} si el parámetro es {@code null}.
     */
    public static Specification<ProductoTienda> porMarca(String marca) {
        return (root, query, cb) -> {
            if (marca == null)
                return null;
            return cb.equal(root.get("producto").get("marca"), marca);
        };
    }

    /**
     * Crea una especificación que filtra los productos dentro de un rango de precios.
     * 
     * <p>Si ambos valores son proporcionados, se usa {@code BETWEEN}. Si solo uno está definido,
     * se usa una comparación con {@code >=} o {@code <=} según corresponda.</p>
     * 
     * @param precioMin precio mínimo (puede ser {@code null}).
     * @param precioMax precio máximo (puede ser {@code null}).
     * @return una {@link Specification} que filtra los productos por rango de precios,
     *         o {@code null} si ambos valores son {@code null}.
     */
    public static Specification<ProductoTienda> porRangoPrecio(BigDecimal precioMin, BigDecimal precioMax) {
        return (root, query, cb) -> {
            if (precioMin != null && precioMax != null) {
                return cb.between(root.get("precio"), precioMin, precioMax);
            } else if (precioMin != null) {
                return cb.greaterThanOrEqualTo(root.get("precio"), precioMin);
            } else if (precioMax != null) {
                return cb.lessThanOrEqualTo(root.get("precio"), precioMax);
            } else {
                return null;
            }
        };
    }

    /**
     * Crea una especificación que filtra los productos según su estado de habilitación.
     * 
     * @param habilitado indica si el producto está habilitado ({@code true}) o no ({@code false}).
     * @return una {@link Specification} que filtra los productos por el campo {@code habilitado},
     *         o {@code null} si el parámetro es {@code null}.
     */
    public static Specification<ProductoTienda> porHabilitado(Boolean habilitado) {
        return (root, query, cb) -> {
            if (habilitado == null) {
                return null;
            }
            if (habilitado) {
                return cb.isTrue(root.get("habilitado"));
            } else {
                return cb.isFalse(root.get("habilitado"));
            }
        };
    }

    /**
     * Crea una especificación que filtra los productos según su disponibilidad en stock.
     * 
     * @param disponible si {@code true}, filtra productos con stock mayor que cero;
     *                   si {@code false}, filtra productos agotados.
     * @return una {@link Specification} que filtra los productos por disponibilidad,
     *         o {@code null} si el parámetro es {@code null}.
     */
    public static Specification<ProductoTienda> porDisponibilidad(Boolean disponible) {
        return (root, query, cb) -> {
            if (disponible == null)
                return null;
            if (disponible) {
                return cb.greaterThan(root.get("stock"), 0);
            } else {
                return cb.lessThanOrEqualTo(root.get("stock"), 0);
            }
        };
    }

    /**
     * Crea una especificación que filtra los productos por el nombre de su categoría.
     * 
     * @param categoria nombre de la categoría (por ejemplo, "Procesador", "Memoria RAM").
     * @return una {@link Specification} que filtra los productos que pertenecen a la categoría dada,
     *         o {@code null} si el parámetro es nulo o está en blanco.
     */
    public static Specification<ProductoTienda> porCategoria(String categoria) {
        return (root, query, cb) -> {
            if (categoria == null || categoria.isBlank()) {
                return null;
            }
            return cb.equal(root.get("producto").get("categoria").get("nombre"), categoria);
        };
    }

    /**
     * Crea una especificación que filtra los productos por coincidencia parcial de su nombre.
     * 
     * <p>Realiza una búsqueda que no distingue entre mayúsculas y minúsculas usando
     * {@code LIKE} y el patrón {@code %nombreProducto%}.</p>
     * 
     * @param nombreProducto nombre (o parte del nombre) del producto a buscar.
     * @return una {@link Specification} que filtra los productos cuyo nombre contiene
     *         el texto indicado, o {@code null} si el parámetro es nulo o vacío.
     */
    public static Specification<ProductoTienda> porNombreProducto(String nombreProducto) {
        return (root, query, cb) -> {
            if (nombreProducto == null || nombreProducto.isBlank()) {
                return null;
            }
            String patron = "%" + nombreProducto.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("producto").get("nombre")), patron);
        };
    }
}
