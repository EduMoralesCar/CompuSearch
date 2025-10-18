package com.universidad.compusearch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.ProductoAtributo;

public interface ProductoAtributoRepository extends JpaRepository<ProductoAtributo, Long>{

    // Obtiene una lista de producto atributo por
    // id del producto
    List<ProductoAtributo> findByProducto_idProducto(long idProducto);
}
