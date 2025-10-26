package com.universidad.compusearch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.Incidente;

public interface IncidenteRepository extends JpaRepository<Incidente, Long> {
    Page<Incidente> findAllByUsuario_IdUsuario(Long idUsuario, Pageable pageable);
}
