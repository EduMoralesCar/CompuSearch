package com.universidad.compuSearch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compuSearch.entity.EstadoToken;
import com.universidad.compuSearch.entity.RefreshToken;
import com.universidad.compuSearch.entity.Usuario;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUsuarioAndEstado(Usuario usuario, EstadoToken estado);
    Optional<RefreshToken> findByUsuarioAndDispositivoAndEstado(Usuario usuario, String dispositivo, EstadoToken estado);
}
