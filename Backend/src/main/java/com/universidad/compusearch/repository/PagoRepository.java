package com.universidad.compusearch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.Pago;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    Page<Pago> findBySuscripcionTiendaIdUsuario(Long idTienda, Pageable pageable);
}
