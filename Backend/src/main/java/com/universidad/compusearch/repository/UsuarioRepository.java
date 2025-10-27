package com.universidad.compusearch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.Usuario;

/**
 * Repositorio JPA para la entidad {@link Usuario}.
 * Proporciona métodos de consulta por email y nombre de usuario.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Obtiene un usuario por su email.
     *
     * @param email el email del usuario
     * @return {@link Optional} conteniendo el usuario si existe
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Obtiene un usuario por su nombre de usuario.
     *
     * @param username el nombre de usuario
     * @return {@link Optional} conteniendo el usuario si existe
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Verifica si existe un usuario con el email dado.
     *
     * @param email el email a verificar
     * @return true si existe un usuario con ese email, false en caso contrario
     */
    boolean existsByEmail(String email);
}
