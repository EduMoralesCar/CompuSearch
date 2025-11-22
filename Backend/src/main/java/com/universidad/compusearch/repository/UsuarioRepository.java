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

    /**
     * Obtiene una página de usuarios filtrados por tipo de usuario.
     *
     * @param tipoUsuario el tipo de usuario a filtrar
     * @param pageable    configuración de paginación
     * @return página de usuarios del tipo especificado
     */
    org.springframework.data.domain.Page<Usuario> findByTipoUsuario(
            com.universidad.compusearch.entity.TipoUsuario tipoUsuario,
            org.springframework.data.domain.Pageable pageable);

    /**
     * Obtiene una página de empleados filtrados por rol.
     *
     * @param rol      el rol del empleado a filtrar
     * @param pageable configuración de paginación
     * @return página de empleados con el rol especificado
     */
    @org.springframework.data.jpa.repository.Query("SELECT e FROM Empleado e WHERE e.rol = :rol")
    org.springframework.data.domain.Page<Usuario> findEmpleadosByRol(
            @org.springframework.data.repository.query.Param("rol") com.universidad.compusearch.entity.Rol rol,
            org.springframework.data.domain.Pageable pageable);

    /**
     * Obtiene una página de empleados excluyendo un rol específico.
     *
     * @param rol      el rol a excluir
     * @param pageable configuración de paginación
     * @return página de empleados sin el rol especificado
     */
    @org.springframework.data.jpa.repository.Query("SELECT e FROM Empleado e WHERE e.rol != :rol")
    org.springframework.data.domain.Page<Usuario> findEmpleadosByRolNot(
            @org.springframework.data.repository.query.Param("rol") com.universidad.compusearch.entity.Rol rol,
            org.springframework.data.domain.Pageable pageable);
}
