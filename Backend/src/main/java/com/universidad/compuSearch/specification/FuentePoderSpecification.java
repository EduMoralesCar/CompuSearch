package com.universidad.compusearch.specification;

import org.springframework.data.jpa.domain.Specification;

import com.universidad.compusearch.entity.ProductoTienda;

import jakarta.persistence.criteria.JoinType;

public class FuentePoderSpecification {
    public static Specification<ProductoTienda> porPotencia(String potencia) {
        return (root, query, cb) -> {
            if (potencia == null) return null;

            query.distinct(true);

            var joinProducto = root.join("producto", JoinType.INNER);
            var joinProductoAtributos = joinProducto.join("atributos", JoinType.INNER);
            var joinAtributo = joinProductoAtributos.join("atributo", JoinType.INNER);

            return cb.and(
                cb.equal(joinAtributo.get("nombre"), "Potencia"),
                cb.equal(joinProductoAtributos.get("valor"), potencia)
            );
        };
    }

    public static Specification<ProductoTienda> porCertificacion(String certificacion) {
        return (root, query, cb) -> {
            if (certificacion == null) return null;

            query.distinct(true);

            var joinProducto = root.join("producto", JoinType.INNER);
            var joinProductoAtributos = joinProducto.join("atributos", JoinType.INNER);
            var joinAtributo = joinProductoAtributos.join("atributo", JoinType.INNER);

            return cb.and(
                cb.equal(joinAtributo.get("nombre"), "Certificación"),
                cb.equal(joinProductoAtributos.get("valor"), certificacion)
            );
        };
    }
}
