package com.universidad.compusearch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.Incidente;

public interface IncidenteRepository extends JpaRepository<Incidente, Long> {

    // Buscar todos los incidentes por el id del usuario
    Page<Incidente> findAllByUsuario_IdUsuario(Long idUsuario, Pageable pageable);
}
