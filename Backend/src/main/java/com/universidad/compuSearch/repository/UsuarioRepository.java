package com.universidad.compusearch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

    // Obtener el usuario por email
    Optional<Usuario> findByEmail(String email);

    // Obtener el usuario por su nombre de usuario
    Optional<Usuario> findByUsername(String username);
}