package com.universidad.compusearch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.universidad.compusearch.entity.ProductoTienda;
import com.universidad.compusearch.entity.Tienda;

public interface ProductoTiendaRepository extends JpaRepository<ProductoTienda, Long>,
                JpaSpecificationExecutor<ProductoTienda> {

        // Buscar producto por id del prodcuto y id de la tienda
        Optional<ProductoTienda> findByProducto_IdProductoAndTienda_IdUsuario(Long idProducto, Long idTienda);

        // Obtener el rango de precio de una categoria de productos
        @Query("""
                        SELECT MIN(pt.precio), MAX(pt.precio)
                        FROM ProductoTienda pt
                        JOIN pt.producto p
                        JOIN p.categoria c
                        WHERE pt.habilitado = true
                        AND (:nombreCategoria IS NULL OR c.nombre = :nombreCategoria)
                        """)
        Object obtenerRangoPrecioPorCategoria(@Param("nombreCategoria") String nombreCategoria);

        // Obtener las distintas marcas de una categoria de productos
        @Query("""
                        SELECT DISTINCT p.marca
                        FROM ProductoTienda pt
                        JOIN pt.producto p
                        JOIN p.categoria c
                        WHERE pt.habilitado = true
                        AND (:nombreCategoria IS NULL OR c.nombre = :nombreCategoria)
                        """)
        List<String> findDistinctMarcasByCategoria(@Param("nombreCategoria") String nombreCategoria);

        // Obtener productos habilitados de una categoria
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

        // Encontrar producto por nombre y nombre de la tienda
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

        // Encontrar productos por nombre del producto
        @Query("""
                        SELECT pt
                        FROM ProductoTienda pt
                        JOIN pt.producto p
                        JOIN pt.tienda t
                        WHERE pt.habilitado = true
                        AND p.nombre = :nombreProducto
                        """)
        List<ProductoTienda> findByNombreProducto(@Param("nombreProducto") String nombreProducto);

        @Modifying
        @Query("UPDATE ProductoTienda p SET p.habilitado = :habilitado WHERE p.id = :id")
        void actualizarHabilitado(@Param("id") Long id, @Param("habilitado") boolean habilitado);

        Optional<ProductoTienda> findByIdProductoApiAndTienda_Nombre(Long idProductoApi, String nombreTienda);

        List<ProductoTienda> findByTienda(Tienda tienda);

}
