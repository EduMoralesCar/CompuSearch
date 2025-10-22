package com.universidad.compusearch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.universidad.compusearch.entity.ProductoTienda;

public interface ProductoTiendaRepository extends JpaRepository<ProductoTienda, Long>,
                JpaSpecificationExecutor<ProductoTienda> {

        // Obtener un producto de una tienda por el id del usuario (tienda)
        // y el id del producto
        Optional<ProductoTienda> findByProducto_IdProductoAndTienda_IdUsuario(Long idProducto, Long idTienda);

        // Obtener un objeto con el precio maximo y minimo
        // de una categoria de productos
        @Query("""
                        SELECT MIN(pt.precio), MAX(pt.precio)
                        FROM ProductoTienda pt
                        JOIN pt.producto p
                        JOIN p.categoria c
                        WHERE pt.habilitado = true
                        AND (:nombreCategoria IS NULL OR c.nombre = :nombreCategoria)
                        """)
        Object obtenerRangoPrecioPorCategoria(@Param("nombreCategoria") String nombreCategoria);

        // Obtener una lista del nombre de las distintas marcas
        // por categoria
        @Query("""
                        SELECT DISTINCT p.marca
                        FROM ProductoTienda pt
                        JOIN pt.producto p
                        JOIN p.categoria c
                        WHERE pt.habilitado = true
                        AND (:nombreCategoria IS NULL OR c.nombre = :nombreCategoria)
                        """)
        List<String> findDistinctMarcasByCategoria(@Param("nombreCategoria") String nombreCategoria);

        // Obtener una lista del nombre todas las tiendas
        // habilitadas por categoria
        @Query("""
                        SELECT DISTINCT t.nombre
                        FROM ProductoTienda pt
                        JOIN pt.tienda t
                        JOIN pt.producto p
                        JOIN p.categoria c
                        WHERE pt.habilitado = true
                        AND (:nombreCategoria IS NULL OR c.nombre = :nombreCategoria)
                        """)
        List<String> findDistinctTiendasWithHabilitadosByCategoria(@Param("nombreCategoria") String nombreCategoria);

        @Query("""
                        SELECT pt
                        FROM ProductoTienda pt
                        JOIN pt.producto p
                        JOIN pt.tienda t
                        WHERE pt.habilitado = true
                        AND p.nombre = :nombreProducto
                        AND t.nombre = :nombreTienda
                        """)
        Optional<ProductoTienda> findByNombreProductoAndNombreTienda(
                        @Param("nombreProducto") String nombreProducto,
                        @Param("nombreTienda") String nombreTienda);

        @Query("""
                        SELECT pt
                        FROM ProductoTienda pt
                        JOIN pt.producto p
                        JOIN pt.tienda t
                        WHERE pt.habilitado = true
                        AND p.nombre = :nombreProducto
                        """)
        List<ProductoTienda> findByNombreProducto(@Param("nombreProducto") String nombreProducto);

}
