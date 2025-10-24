package com.universidad.compusearch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.DetalleBuild;

public interface DetalleBuildRespository extends JpaRepository<DetalleBuild, Long> {

    // Obtener una lista de los detalles 
    // de una build por su id
    List<DetalleBuild> findByBuild_IdBuild(Long idBuild);
}
