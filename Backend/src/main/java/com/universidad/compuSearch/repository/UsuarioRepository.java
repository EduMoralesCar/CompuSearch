package com.universidad.compuSearch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compuSearch.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    Optional<Usuario> findByEmail(String email);
}
