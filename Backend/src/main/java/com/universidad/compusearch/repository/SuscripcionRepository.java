package com.universidad.compusearch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.EstadoSuscripcion;
import com.universidad.compusearch.entity.Suscripcion;
import com.universidad.compusearch.entity.Tienda;

public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long>{
    
    // Obtener suscripcion por tienda y estado
    Optional<Suscripcion> findByTiendaAndEstado(Tienda tienda, EstadoSuscripcion estadoSuscripcion);

}
