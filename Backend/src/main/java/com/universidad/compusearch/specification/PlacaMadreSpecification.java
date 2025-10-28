package com.universidad.compusearch.specification;

import org.springframework.data.jpa.domain.Specification;

import com.universidad.compusearch.entity.ProductoTienda;

import jakarta.persistence.criteria.JoinType;

/**
 * Clase que define especificaciones de filtrado para productos de tipo
 * <strong>Placa Madre (Motherboard)</strong>.
 * 
 * <p>Estas {@link Specification Specifications} permiten construir consultas
 * dinámicas sobre la entidad {@link ProductoTienda}, filtrando placas madre
 * según características técnicas específicas.</p>
 * 
 * <p>Actualmente, incluye filtros por tipo de <strong>socket</strong> y por
 * <strong>factor de forma</strong>.</p>
 * 
 * Ejemplo de uso:
 * <pre>{@code
 * Specification<ProductoTienda> spec =
 *     Specification.where(PlacaMadreSpecification.porSocket("AM5"))
 *                  .and(PlacaMadreSpecification.porFactor("ATX"));
 * }</pre>
 * 
 */
public class PlacaMadreSpecification {

    /**
     * Crea una especificación que filtra placas madre según el tipo de socket compatible.
     * 
     * <p>Busca dentro de los atributos del producto el campo con nombre
     * <strong>"Socket Motherboard"</strong> y compara su valor con el socket indicado.</p>
     * 
     * @param socket tipo de socket de la placa madre (por ejemplo, "AM4", "LGA 1700").
     * @return una {@link Specification} que filtra por socket, o {@code null} si el
     *         parámetro es {@code null}.
     */
    public static Specification<ProductoTienda> porSocket(String socket) {
        return (root, query, cb) -> {
            if (socket == null) return null;

            query.distinct(true);

            var joinProducto = root.join("producto", JoinType.INNER);
            var joinProductoAtributos = joinProducto.join("atributos", JoinType.INNER);
            var joinAtributo = joinProductoAtributos.join("atributo", JoinType.INNER);

            return cb.and(
                cb.equal(joinAtributo.get("nombre"), "Socket Motherboard"),
                cb.equal(joinProductoAtributos.get("valor"), socket)
            );
        };
    }

    /**
     * Crea una especificación que filtra placas madre según su factor de forma.
     * 
     * <p>Busca dentro de los atributos del producto el campo con nombre
     * <strong>"Factor de Forma Motherboard"</strong> y compara su valor con el factor
     * proporcionado.</p>
     * 
     * @param factor factor de forma de la placa madre (por ejemplo, "ATX", "Micro ATX", "Mini ITX").
     * @return una {@link Specification} que filtra por factor de forma, o {@code null} si
     *         el parámetro es {@code null}.
     */
    public static Specification<ProductoTienda> porFactor(String factor) {
        return (root, query, cb) -> {
            if (factor == null) return null;

            query.distinct(true);

            var joinProducto = root.join("producto", JoinType.INNER);
            var joinProductoAtributos = joinProducto.join("atributos", JoinType.INNER);
            var joinAtributo = joinProductoAtributos.join("atributo", JoinType.INNER);

            return cb.and(
                cb.equal(joinAtributo.get("nombre"), "Factor de Forma Motherboard"),
                cb.equal(joinProductoAtributos.get("valor"), factor)
            );
        };
    }
}
