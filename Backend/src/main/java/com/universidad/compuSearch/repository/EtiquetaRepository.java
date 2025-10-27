package com.universidad.compusearch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.Etiqueta;

public interface EtiquetaRepository extends JpaRepository<Etiqueta, Long>{

    // Obtiene todas los nombres de 
    // las etiquetas
    Optional<Etiqueta> findByNombre(String nombre);
}
