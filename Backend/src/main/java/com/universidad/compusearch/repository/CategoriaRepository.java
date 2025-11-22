package com.universidad.compusearch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.universidad.compusearch.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Buscar categoria por nombre
    Optional<Categoria> findByNombre(String nombre);

    // Obtener todos los nombres de la categorias
    @Query("SELECT c.nombre FROM Categoria c")
    List<String> findAllNombres();
}
