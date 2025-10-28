package com.universidad.compusearch.specification;

import org.springframework.data.jpa.domain.Specification;

import com.universidad.compusearch.entity.ProductoTienda;

import jakarta.persistence.criteria.JoinType;

/**
 * Clase que define especificaciones de filtrado para productos de tipo
 * <strong>Almacenamiento</strong> (HDD, SSD, NVMe, etc.).
 * 
 * Estas {@link Specification Specifications} se utilizan para construir
 * consultas dinámicas sobre la entidad {@link ProductoTienda}, aplicando filtros
 * según las características técnicas del dispositivo de almacenamiento.
 * 
 * Actualmente incluye filtros por <strong>tipo</strong> de almacenamiento
 * y por <strong>capacidad</strong>.
 * 
 * Ejemplo de uso:
 * <pre>{@code
 * Specification<ProductoTienda> spec =
 *     Specification.where(AlmacenamientoSpecification.porTipo("SSD"))
 *                  .and(AlmacenamientoSpecification.porCapacidad("1TB"));
 * }</pre>
 * 
 */
public class AlmacenamientoSpecification {

    /**
     * Crea una especificación que filtra los dispositivos de almacenamiento
     * según su tipo (por ejemplo, HDD, SSD o NVMe).
     * 
     * <p>Busca en los atributos del producto el campo con nombre
     * <strong>"Tipo de Almacenamiento"</strong> y compara su valor con
     * el tipo proporcionado.</p>
     * 
     * @param tipo tipo de almacenamiento (por ejemplo, "SSD", "HDD", "NVMe").
     * @return una {@link Specification} que filtra productos por tipo,
     *         o {@code null} si el parámetro es {@code null}.
     */
    public static Specification<ProductoTienda> porTipo(String tipo) {
        return (root, query, cb) -> {
            if (tipo == null) return null;

            query.distinct(true);

            var joinProducto = root.join("producto", JoinType.INNER);
            var joinProductoAtributos = joinProducto.join("atributos", JoinType.INNER);
            var joinAtributo = joinProductoAtributos.join("atributo", JoinType.INNER);

            return cb.and(
                cb.equal(joinAtributo.get("nombre"), "Tipo de Almacenamiento"),
                cb.equal(joinProductoAtributos.get("valor"), tipo)
            );
        };
    }

    /**
     * Crea una especificación que filtra los dispositivos de almacenamiento
     * según su capacidad.
     * 
     * <p>Busca en los atributos del producto el campo con nombre
     * <strong>"Capacidad Almacenamiento"</strong> y compara su valor
     * con la capacidad proporcionada.</p>
     * 
     * @param capacidad capacidad del dispositivo (por ejemplo, "500GB", "1TB").
     * @return una {@link Specification} que filtra productos por capacidad,
     *         o {@code null} si el parámetro es {@code null}.
     */
    public static Specification<ProductoTienda> porCapacidad(String capacidad) {
        return (root, query, cb) -> {
            if (capacidad == null) return null;

            query.distinct(true);

            var joinProducto = root.join("producto", JoinType.INNER);
            var joinProductoAtributos = joinProducto.join("atributos", JoinType.INNER);
            var joinAtributo = joinProductoAtributos.join("atributo", JoinType.INNER);

            return cb.and(
                cb.equal(joinAtributo.get("nombre"), "Capacidad Almacenamiento"),
                cb.equal(joinProductoAtributos.get("valor"), capacidad)
            );
        };
    }
}
