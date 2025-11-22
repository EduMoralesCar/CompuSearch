package com.universidad.compusearch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.ProductoAtributo;

public interface ProductoAtributoRepository extends JpaRepository<ProductoAtributo, Long> {

    // Encontrar la lista de atributos de un producto
    List<ProductoAtributo> findByProducto_idProducto(long idProducto);
}
