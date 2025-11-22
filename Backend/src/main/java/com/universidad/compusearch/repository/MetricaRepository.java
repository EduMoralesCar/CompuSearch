package com.universidad.compusearch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.Metrica;

public interface MetricaRepository extends JpaRepository<Metrica, Long>{

    // Obtener la metrica por medio del id de producto tienda
    Optional<Metrica> findByProductoTiendaIdProductoTienda(Long idProductoTienda);
}
