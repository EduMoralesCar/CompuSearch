package com.universidad.compusearch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.Tienda;
import java.util.List;


public interface TiendaRepository extends JpaRepository<Tienda, Long>{
    List<Tienda> findByVerificado(boolean verificado);
}
