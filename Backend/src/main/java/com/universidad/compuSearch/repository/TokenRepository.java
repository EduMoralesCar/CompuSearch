package com.universidad.compusearch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.TipoToken;
import com.universidad.compusearch.entity.Token;
import com.universidad.compusearch.entity.Usuario;

public interface TokenRepository extends JpaRepository<Token, Long>{
    Optional<Token> findByTokenAndTipo(String token, TipoToken tipo);
    Optional<Token> findByUsuarioAndIpDispositivoAndTipo(Usuario usuario, String ipDispositivo, TipoToken tipo);
}