package com.universidad.compusearch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.DetalleBuild;

public interface DetalleBuildRepository extends JpaRepository<DetalleBuild, Long> {

    // Obtener los detalles de una build por id
    List<DetalleBuild> findByBuild_IdBuild(Long idBuild);
}
