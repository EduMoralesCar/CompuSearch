package com.universidad.compusearch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.universidad.compusearch.entity.Build;

public interface BuildRepository extends JpaRepository<Build, Long> {

    // Obtener todas las builds del usuario
    @Query("SELECT b FROM Build b WHERE b.usuario.id = :idUsuario")
    Page<Build> findAllByUsuarioId(Long idUsuario, Pageable pageable);
}
