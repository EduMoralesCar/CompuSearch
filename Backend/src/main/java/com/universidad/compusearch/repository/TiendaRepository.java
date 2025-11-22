package com.universidad.compusearch.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.universidad.compusearch.entity.Tienda;

public interface TiendaRepository extends JpaRepository<Tienda, Long> {

    // Obtener todas las tiendas verificadas
    List<Tienda> findByVerificado(boolean verificado);

    // Verificar si existe una tienda por su id
    boolean existsByIdUsuario(Long idUsuario);

    // Obtener las tiendas desde x fecha hasta hoy
    List<Tienda> findByFechaAfiliacionGreaterThanEqual(LocalDateTime fechaInicio);

    // Obtener tiendas con mas productos
    @Query("SELECT t FROM Tienda t JOIN t.productos pt GROUP BY t ORDER BY COUNT(pt) DESC")
    List<Tienda> findTopNTiendasByProductoCount(Pageable pageable);

    // Obtiene las tiendas y el total de redirecciones
    @Query("SELECT t.id, t.nombre, SUM(m.visitas) AS totalVisitas " +
            "FROM Tienda t " +
            "JOIN t.productos pt " +
            "JOIN pt.metricas m " +
            "GROUP BY t.id, t.nombre " +
            "ORDER BY totalVisitas DESC")
    List<Object[]> findTiendasByTotalVisitas(Pageable pageable);

    // Obtener tiendas por nombre
    Page<Tienda> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

}
