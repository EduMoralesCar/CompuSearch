package com.universidad.compusearch.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.universidad.compusearch.entity.TipoUsuario;
import com.universidad.compusearch.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Obtener usuario por email
    Optional<Usuario> findByEmail(String email);

    // Obtener usuario por nombre de usuario
    Optional<Usuario> findByUsername(String username);

    // Verificar si el emial existe
    boolean existsByEmail(String email);

    // Verificar si el nombre de usuario existe
    boolean existsByUsername(String username);

    // Obtener los usuarios por nombre 
    Page<Usuario> findByUsernameContainingIgnoreCase(String username, Pageable pageable);

    // Obtener usuario por el tipo de usuario
    Page<Usuario> findByTipoUsuario(TipoUsuario tipoUsuario, Pageable pageable);

    // Obtener usuarios por tipo y nombre de usuario
    Page<Usuario> findByUsernameContainingIgnoreCaseAndTipoUsuario(
            String username, TipoUsuario tipoUsuario, Pageable pageable);
    
    // Actualizar el estado del usuario
    @Modifying
    @Query("UPDATE Usuario u SET u.activo = :activo WHERE u.id = :id")
    void actualizarActivo(@Param("id") Long id, @Param("activo") boolean activo);
}
