package com.universidad.compusearch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.SolicitudTienda;

public interface SolicitudTiendaRepository extends JpaRepository<SolicitudTienda, Long> {

    // Obtener todas las solicitudes del usuario por su id
    Page<SolicitudTienda> findByUsuario_IdUsuario(Long idUsuario, Pageable pageable);
}
