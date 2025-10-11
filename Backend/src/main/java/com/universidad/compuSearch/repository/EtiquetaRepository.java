package com.universidad.compusearch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.Etiqueta;

public interface EtiquetaRepository extends JpaRepository<Etiqueta, Long>{
    List<Etiqueta> findByNombre(String nombre);
    List<Etiqueta> findAll();
}
