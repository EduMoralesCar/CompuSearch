package com.universidad.compusearch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long>{

    // Obtine un producto por su nombre
    Optional<Producto> findByNombre(String nombre);

    // Obtiene una lista de productos por categoria
    List<Producto> findByCategoria_IdCategoria(long idCategoria);
}
