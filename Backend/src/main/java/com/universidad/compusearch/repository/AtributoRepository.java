package com.universidad.compusearch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.universidad.compusearch.entity.Atributo;

public interface AtributoRepository extends JpaRepository<Atributo, Long>,
        JpaSpecificationExecutor<Atributo> {

    // Buscar por nombre
    Optional<Atributo> findByNombre(String nombre);

    // Buscar diferentes valores de atributos
    @Query("""
                SELECT DISTINCT pa.valor
                FROM ProductoTienda pt
                JOIN pt.producto p
                JOIN p.atributos pa
                JOIN pa.atributo a
                WHERE a.nombre = :nombreAtributo
                AND pt.habilitado = true
            """)
    List<String> findDistinctValoresByAtributo(@Param("nombreAtributo") String nombreAtributo);
}
