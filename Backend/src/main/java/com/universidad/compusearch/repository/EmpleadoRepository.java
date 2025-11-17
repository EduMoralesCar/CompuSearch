package com.universidad.compusearch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    // Bucar empleados por nombre
    Page<Empleado> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
}
