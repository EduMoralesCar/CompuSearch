package com.universidad.compusearch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.TipoToken;
import com.universidad.compusearch.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {

    // Obtener token por tipo
    Optional<Token> findByTokenAndTipo(String token, TipoToken tipo);

    // Obtener token por id de usuario, dispositivo y ip
    Optional<Token> findByUsuario_IdUsuarioAndIpDispositivoAndTipo(Long idUsuario, String ipDispositivo, TipoToken tipo);
}
