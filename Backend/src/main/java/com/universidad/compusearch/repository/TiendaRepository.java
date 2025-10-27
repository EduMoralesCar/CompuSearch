package com.universidad.compusearch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.universidad.compusearch.entity.Tienda;

/**
 * Repositorio JPA para la entidad {@link Tienda}.
 *
 * <p>
 * Proporciona métodos para realizar operaciones CRUD y consultas personalizadas
 * sobre las tiendas registradas en el sistema.
 * </p>
 */
@Repository
public interface TiendaRepository extends JpaRepository<Tienda, Long> {

    /**
     * Obtiene todas las tiendas según su estado de verificación.
     *
     * <p>
     * Este método permite filtrar las tiendas que han sido verificadas o no.
     * </p>
     *
     * @param verificado true si se buscan tiendas verificadas, false si se buscan no verificadas
     * @return lista de {@link Tienda} que cumplen con el criterio de verificación
     */
    List<Tienda> findByVerificado(boolean verificado);
}
