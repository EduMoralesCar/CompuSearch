package com.universidad.compusearch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.universidad.compusearch.entity.Producto;
import com.universidad.compusearch.entity.ProductoTienda;
import com.universidad.compusearch.entity.Tienda;

public interface ProductoTiendaRepository extends JpaRepository<ProductoTienda, Long>,
                JpaSpecificationExecutor<ProductoTienda> {

        Optional<ProductoTienda> findByProductoAndTienda(Producto producto, Tienda tienda);

        @Query("""
                        SELECT MIN(pt.precio), MAX(pt.precio)
                        FROM ProductoTienda pt
                        JOIN pt.producto p
                        JOIN p.categoria c
                        WHERE pt.habilitado = true
                        AND (:nombreCategoria IS NULL OR c.nombre = :nombreCategoria)
                        """)
        Object obtenerRangoPrecioPorCategoria(@Param("nombreCategoria") String nombreCategoria);

        @Query("""
                        SELECT DISTINCT p.marca
                        FROM ProductoTienda pt
                        JOIN pt.producto p
                        JOIN p.categoria c
                        WHERE pt.habilitado = true
                        AND (:nombreCategoria IS NULL OR c.nombre = :nombreCategoria)
                        """)
        List<String> findDistinctMarcasByCategoria(@Param("nombreCategoria") String nombreCategoria);
}
