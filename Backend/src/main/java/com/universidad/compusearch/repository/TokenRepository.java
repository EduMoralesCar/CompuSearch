package com.universidad.compusearch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidad.compusearch.entity.TipoToken;
import com.universidad.compusearch.entity.Token;

/**
 * Repositorio JPA para la entidad {@link Token}.
 * Proporciona métodos para buscar tokens por valor, tipo, usuario y dispositivo.
 */
public interface TokenRepository extends JpaRepository<Token, Long> {

    /**
     * Busca un token por su valor y tipo.
     *
     * @param token el valor del token
     * @param tipo  el tipo de token (RESET, REFRESH)
     * @return {@link Optional} conteniendo el token si existe
     */
    Optional<Token> findByTokenAndTipo(String token, TipoToken tipo);

    /**
     * Busca un token por el ID del usuario, IP del dispositivo y tipo de token.
     *
     * @param idUsuario     el ID del usuario
     * @param ipDispositivo la IP o identificador del dispositivo
     * @param tipo          el tipo de token (RESET, REFRESH)
     * @return {@link Optional} conteniendo el token si existe
     */
    Optional<Token> findByUsuario_IdUsuarioAndIpDispositivoAndTipo(Long idUsuario, String ipDispositivo, TipoToken tipo);
}
