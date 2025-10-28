package com.universidad.compusearch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.Empleado;

/**
 * Repositorio para la entidad {@link Empleado}.
 * Proporciona operaciones CRUD y consultas específicas relacionadas a los empleados.
 */
public interface EmpleadoRepository extends JpaRepository<Empleado, Long>{
    
}
