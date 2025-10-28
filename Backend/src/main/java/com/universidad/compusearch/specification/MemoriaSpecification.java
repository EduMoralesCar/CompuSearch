package com.universidad.compusearch.specification;

import org.springframework.data.jpa.domain.Specification;

import com.universidad.compusearch.entity.ProductoTienda;

import jakarta.persistence.criteria.JoinType;

/**
 * Clase que define especificaciones de filtrado para productos de tipo
 * <strong>Memoria RAM</strong>.
 * 
 * <p>Estas {@link Specification Specifications} permiten construir consultas
 * dinámicas sobre la entidad {@link ProductoTienda}, aplicando filtros por
 * atributos técnicos específicos de las memorias RAM.</p>
 * 
 * <p>Actualmente, incluye filtros por <strong>frecuencia</strong>,
 * <strong>tipo</strong> y <strong>capacidad</strong>.</p>
 * 
 * Ejemplo de uso:
 * <pre>{@code
 * Specification<ProductoTienda> spec =
 *     Specification.where(MemoriaSpecification.porTipo("DDR5"))
 *                  .and(MemoriaSpecification.porFrecuencia("6000 MHz"))
 *                  .and(MemoriaSpecification.porCapacidad("32 GB"));
 * }</pre>
 * 
 */
public class MemoriaSpecification {

    /**
     * Crea una especificación que filtra memorias RAM según su frecuencia.
     * 
     * <p>Busca dentro de los atributos del producto el campo con nombre
     * <strong>"Frecuencia RAM"</strong> y compara su valor con la frecuencia proporcionada.</p>
     * 
     * @param frecuencia frecuencia de la memoria (por ejemplo, "3200 MHz", "6000 MHz").
     * @return una {@link Specification} que filtra por frecuencia, o {@code null} si el
     *         parámetro es {@code null}.
     */
    public static Specification<ProductoTienda> porFrecuencia(String frecuencia) {
        return (root, query, cb) -> {
            if (frecuencia == null) return null;

            query.distinct(true);

            var joinProducto = root.join("producto", JoinType.INNER);
            var joinProductoAtributos = joinProducto.join("atributos", JoinType.INNER);
            var joinAtributo = joinProductoAtributos.join("atributo", JoinType.INNER);

            return cb.and(
                cb.equal(joinAtributo.get("nombre"), "Frecuencia RAM"),
                cb.equal(joinProductoAtributos.get("valor"), frecuencia)
            );
        };
    }

    /**
     * Crea una especificación que filtra memorias RAM según su tipo.
     * 
     * <p>Busca dentro de los atributos del producto el campo con nombre
     * <strong>"Tipo RAM"</strong> y compara su valor con el tipo indicado.</p>
     * 
     * @param tipo tipo de memoria (por ejemplo, "DDR4", "DDR5").
     * @return una {@link Specification} que filtra por tipo, o {@code null} si el
     *         parámetro es {@code null}.
     */
    public static Specification<ProductoTienda> porTipo(String tipo) {
        return (root, query, cb) -> {
            if (tipo == null) return null;

            query.distinct(true);

            var joinProducto = root.join("producto", JoinType.INNER);
            var joinProductoAtributos = joinProducto.join("atributos", JoinType.INNER);
            var joinAtributo = joinProductoAtributos.join("atributo", JoinType.INNER);

            return cb.and(
                cb.equal(joinAtributo.get("nombre"), "Tipo RAM"),
                cb.equal(joinProductoAtributos.get("valor"), tipo)
            );
        };
    }

    /**
     * Crea una especificación que filtra memorias RAM según su capacidad.
     * 
     * <p>Busca dentro de los atributos del producto el campo con nombre
     * <strong>"Capacidad RAM"</strong> y compara su valor con la capacidad proporcionada.</p>
     * 
     * @param capacidad capacidad de la memoria (por ejemplo, "8 GB", "16 GB", "32 GB").
     * @return una {@link Specification} que filtra por capacidad, o {@code null} si el
     *         parámetro es {@code null}.
     */
    public static Specification<ProductoTienda> porCapacidad(String capacidad) {
        return (root, query, cb) -> {
            if (capacidad == null) return null;

            query.distinct(true);

            var joinProducto = root.join("producto", JoinType.INNER);
            var joinProductoAtributos = joinProducto.join("atributos", JoinType.INNER);
            var joinAtributo = joinProductoAtributos.join("atributo", JoinType.INNER);

            return cb.and(
                cb.equal(joinAtributo.get("nombre"), "Capacidad RAM"),
                cb.equal(joinProductoAtributos.get("valor"), capacidad)
            );
        };
    }
}
