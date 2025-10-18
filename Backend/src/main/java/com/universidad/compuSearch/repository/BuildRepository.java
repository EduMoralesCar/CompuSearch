package com.universidad.compusearch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.universidad.compusearch.entity.Build;

public interface BuildRepository extends JpaRepository<Build, Long> {

    // Obtiene la build por id
    Optional<Build> findByIdBuild(long idBuild);

    // Obtiene una lista de builds por
    // el id del usuario
    @Query("SELECT b FROM Build b WHERE b.usuario.id = :idUsuario")
    List<Build> findAllByUsuarioId(@Param("idUsuario") long idUsuario);
}
