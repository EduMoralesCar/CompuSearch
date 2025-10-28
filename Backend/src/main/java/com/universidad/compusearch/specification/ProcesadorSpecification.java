package com.universidad.compusearch.specification;

import org.springframework.data.jpa.domain.Specification;

import com.universidad.compusearch.entity.ProductoTienda;

import jakarta.persistence.criteria.JoinType;

/**
 * Clase que define especificaciones de filtrado para productos de tipo
 * <strong>Procesador (CPU)</strong>.
 * 
 * <p>Estas {@link Specification Specifications} se utilizan para crear consultas
 * dinámicas en la entidad {@link ProductoTienda}, permitiendo filtrar procesadores
 * según distintos atributos técnicos.</p>
 * 
 * <p>Actualmente incluye la especificación para filtrar por el tipo de
 * <strong>socket</strong> compatible del procesador.</p>
 * 
 * Ejemplo de uso:
 * <pre>{@code
 * Specification<ProductoTienda> spec = ProcesadorSpecification.porSocket("AM5");
 * }</pre>
 * 
 */
public class ProcesadorSpecification {

    /**
     * Crea una especificación que filtra procesadores según el tipo de socket compatible.
     * 
     * <p>Este filtro busca en la relación de atributos del producto, comparando el nombre
     * del atributo con "Socket CPU" y el valor con el socket proporcionado.</p>
     * 
     * @param socket tipo de socket del procesador (por ejemplo, "AM4", "LGA 1700").
     * @return una {@link Specification} que filtra productos con el socket indicado,
     *         o {@code null} si el parámetro es {@code null}.
     */
    public static Specification<ProductoTienda> porSocket(String socket) {
        return (root, query, cb) -> {
            if (socket == null) return null;

            query.distinct(true);

            var joinProducto = root.join("producto", JoinType.INNER);
            var joinProductoAtributos = joinProducto.join("atributos", JoinType.INNER);
            var joinAtributo = joinProductoAtributos.join("atributo", JoinType.INNER);

            return cb.and(
                cb.equal(joinAtributo.get("nombre"), "Socket CPU"),
                cb.equal(joinProductoAtributos.get("valor"), socket)
            );
        };
    }
}
