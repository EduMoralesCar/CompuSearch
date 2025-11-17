package com.universidad.compusearch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.Tienda;

public interface TiendaRepository extends JpaRepository<Tienda, Long> {

    // Obtener todas las tiendas verificadas
    List<Tienda> findByVerificado(boolean verificado);

    // Verificar si existe una tienda por su id
    boolean existsByIdUsuario(Long idUsuario);
}
