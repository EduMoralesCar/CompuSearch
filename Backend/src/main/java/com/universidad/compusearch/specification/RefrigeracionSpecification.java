package com.universidad.compusearch.specification;

import org.springframework.data.jpa.domain.Specification;
import com.universidad.compusearch.entity.ProductoTienda;
import jakarta.persistence.criteria.JoinType;

/**
 * Clase que define las especificaciones (filtros dinámicos) para realizar
 * búsquedas de productos de tipo "Refrigeración" en la base de datos.
 * 
 * <p>Estas especificaciones permiten filtrar productos por sus atributos,
 * tales como el tipo de enfriamiento o la compatibilidad del socket del cooler.
 * Utiliza la API de Criteria de JPA junto con {@link Specification}.</p>
 * 
 * <p>Cada método devuelve una {@link Specification} que puede combinarse con otras
 * para construir consultas más complejas.</p>
 * 
 * Ejemplo de uso:
 * <pre>{@code
 * Specification<ProductoTienda> spec = RefrigeracionSpecification
 *     .porTipo("Líquida")
 *     .and(RefrigeracionSpecification.porCompatibilidad("AM4"));
 * }</pre>
 * 
 */
public class RefrigeracionSpecification {

    /**
     * Crea una especificación para filtrar productos por su tipo de enfriamiento.
     * 
     * @param tipo el tipo de enfriamiento del producto (por ejemplo, "Aire", "Líquida").
     * @return una {@link Specification} que filtra los productos cuyo atributo
     *         "Tipo de Enfriamiento" coincide con el valor proporcionado;
     *         {@code null} si el parámetro es {@code null}.
     */
    public static Specification<ProductoTienda> porTipo(String tipo) {
        return (root, query, cb) -> {
            if (tipo == null)
                return null;

            query.distinct(true);

            var joinProducto = root.join("producto", JoinType.INNER);
            var joinProductoAtributos = joinProducto.join("atributos", JoinType.INNER);
            var joinAtributo = joinProductoAtributos.join("atributo", JoinType.INNER);

            return cb.and(
                    cb.equal(joinAtributo.get("nombre"), "Tipo de Enfriamiento"),
                    cb.equal(joinProductoAtributos.get("valor"), tipo));
        };
    }

    /**
     * Crea una especificación para filtrar productos por su compatibilidad de socket.
     * 
     * @param compatibilidad el tipo de compatibilidad del socket (por ejemplo, "AM4", "LGA1700").
     * @return una {@link Specification} que filtra los productos cuyo atributo
     *         "Compatibilidad Socket Cooler" coincide con el valor proporcionado;
     *         {@code null} si el parámetro es {@code null}.
     */
    public static Specification<ProductoTienda> porCompatibilidad(String compatibilidad) {
        return (root, query, cb) -> {
            if (compatibilidad == null)
                return null;

            query.distinct(true);

            var joinProducto = root.join("producto", JoinType.INNER);
            var joinProductoAtributos = joinProducto.join("atributos", JoinType.INNER);
            var joinAtributo = joinProductoAtributos.join("atributo", JoinType.INNER);

            return cb.and(
                    cb.equal(joinAtributo.get("nombre"), "Compatibilidad Socket Cooler"),
                    cb.equal(joinProductoAtributos.get("valor"), compatibilidad));
        };
    }
}
