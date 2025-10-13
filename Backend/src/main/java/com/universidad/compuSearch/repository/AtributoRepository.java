package com.universidad.compusearch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.universidad.compusearch.entity.Atributo;

public interface AtributoRepository extends JpaRepository<Atributo, Long>,
        JpaSpecificationExecutor<Atributo> {
            
    Optional<Atributo> findByNombre(String nombre);
}
