package com.universidad.compusearch.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.Plan;

public interface PlanRepository extends JpaRepository<Plan, Long>{

    // Buscar plan por nombre
    Optional<Plan> findByNombre(String nombre);

    // Buscar planes por el nombre
    Page<Plan> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    // Verificar si un plan existe por nombre
    boolean existsByNombreIgnoreCase(String nombre);

    // Buscar planes por nombre y por su estado de activo
    Page<Plan> findByNombreContainingIgnoreCaseAndActivo(String nombre, boolean activo, Pageable pageable);

    // Buscar plantes por su estado activo
    Page<Plan> findByActivo(boolean activo, Pageable pageable);

    // Verificar si un plan existe por su nombre y estado
    boolean existsByNombreIgnoreCaseAndActivo(String nombre, boolean activo);
}
