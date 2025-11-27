package com.universidad.compusearch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Obtener un producto por su nombre
    Optional<Producto> findByNombre(String nombre);

    // Obtener una lista de productos por el id de categoria
    List<Producto> findByCategoria_IdCategoria(long idCategoria);

    // Verificar si existe una categoria por su id
    boolean existsByCategoria_IdCategoria(Long idCategoria);
}
