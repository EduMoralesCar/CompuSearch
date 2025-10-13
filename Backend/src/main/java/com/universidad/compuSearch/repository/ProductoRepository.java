package com.universidad.compusearch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.Categoria;
import com.universidad.compusearch.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long>{
    Optional<Producto> findByNombre(String nombre);
    List<Producto> findByCategoria(Categoria categoria);
    Optional<Producto> findByIdProducto(Long idProducto);
}
