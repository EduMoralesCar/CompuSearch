package com.universidad.compusearch.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.universidad.compusearch.entity.ProductoTienda;

public class ProductoTiendaSpecification {

    public static Specification<ProductoTienda> porNombreTienda(String nombreTienda) {
        return (root, query, cb) -> {
            if (nombreTienda == null)
                return null;
            return cb.equal(root.get("tienda").get("nombre"), nombreTienda);
        };
    }

    public static Specification<ProductoTienda> porMarca(String marca) {
        return (root, query, cb) -> {
            if (marca == null)
                return null;
            return cb.equal(root.get("producto").get("marca"), marca);
        };
    }

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

    public static Specification<ProductoTienda> porCategoria(String categoria) {
        return (root, query, cb) -> {
            if (categoria == null || categoria.isBlank()) {
                return null;
            }
            return cb.equal(root.get("producto").get("categoria").get("nombre"), categoria);
        };
    }

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
