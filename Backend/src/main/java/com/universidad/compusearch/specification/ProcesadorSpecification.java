package com.universidad.compusearch.specification;

import org.springframework.data.jpa.domain.Specification;

import com.universidad.compusearch.entity.ProductoTienda;

import jakarta.persistence.criteria.JoinType;

// Especificaciones del procesador
public class ProcesadorSpecification {
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
