package com.universidad.compusearch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.universidad.compusearch.entity.Atributo;

/**
 * Repositorio para la entidad {@link Atributo}.
 * Proporciona operaciones CRUD, consultas por nombre y obtención de valores distintos.
 */
public interface AtributoRepository extends JpaRepository<Atributo, Long>,
        JpaSpecificationExecutor<Atributo> {

    /**
     * Busca un atributo por su nombre exacto.
     *
     * @param nombre Nombre del atributo a buscar.
     * @return {@link Optional} que contiene el atributo si existe, o vacío si no.
     */
    Optional<Atributo> findByNombre(String nombre);
    
    /**
     * Obtiene una lista de valores distintos asociados a un atributo,
     * considerando únicamente los productos habilitados.
     *
     * @param nombreAtributo Nombre del atributo.
     * @return Lista de valores distintos del atributo.
     */
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
