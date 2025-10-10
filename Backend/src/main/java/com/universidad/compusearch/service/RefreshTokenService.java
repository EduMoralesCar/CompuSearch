package com.universidad.compusearch.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.EstadoToken;
import com.universidad.compusearch.entity.TipoToken;
import com.universidad.compusearch.entity.Token;
import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.exception.TokenException;
import com.universidad.compusearch.jwt.JwtConfigHelper;
import com.universidad.compusearch.repository.TokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final TokenRepository tokenRepository;
    private final JwtConfigHelper jwtConfigHelper;

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);

    // Crea o actualiza el token de refresco según el estado del existente
    public Token createOrUpdateRefreshToken(Usuario usuario, String dispositivo) {
        logger.info("Procesando token de refresco para usuario {} en dispositivo {}", usuario.getIdUsuario(), dispositivo);

        return findByUsuarioAndDispositivo(usuario, dispositivo)
                .map(existingToken -> {
                    if (existingToken.isExpired() || existingToken.getEstado() == EstadoToken.REVOCADO) {
                        return updateRefreshToken(existingToken);
                    }
                    return existingToken;
                })
                .orElseGet(() -> createRefreshToken(usuario, dispositivo));
    }

    // Actualiza el token de refresco
    private Token updateRefreshToken(Token existingToken) {
        logger.info("Actualizando token de refresco ID={} para usuario {}", existingToken.getIdToken(), existingToken.getUsuario().getIdUsuario());

        existingToken.setToken(UUID.randomUUID().toString());
        existingToken.setFechaCreacion(Instant.now());
        existingToken.setFechaExpiracion(Instant.now().plusMillis(jwtConfigHelper.getRefreshTokenExpiration()));
        existingToken.setEstado(EstadoToken.ACTIVO);

        return save(existingToken);
    }

    // Crea un nuevo token de refresco
    private Token createRefreshToken(Usuario usuario, String dispositivo) {
        logger.info("Creando nuevo token de refresco para usuario {} en dispositivo {}", usuario.getIdUsuario(), dispositivo);

        Token refreshToken = new Token();
        refreshToken.setUsuario(usuario);
        refreshToken.setIpDispositivo(dispositivo);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setFechaCreacion(Instant.now());
        refreshToken.setFechaExpiracion(Instant.now().plusMillis(jwtConfigHelper.getRefreshTokenExpiration()));
        refreshToken.setTipo(TipoToken.REFRESH);
        refreshToken.setEstado(EstadoToken.ACTIVO);

        return save(refreshToken);
    }

    // Valida que el token esté activo y no expirado
    public Token validateAndGetRefreshToken(String token) {
        logger.debug("Validando token de refresco: {}", token);

        return findByToken(token)
                .filter(t -> !t.isExpired() && t.getEstado() == EstadoToken.ACTIVO)
                .orElseThrow(() -> TokenException.invalid("Refresh"));
    }

    // Revoca el token de refresco
    public void revokeRefreshToken(String token) {
        logger.warn("Revocando token de refresco: {}", token);

        Token refreshToken = findByToken(token)
                .orElseThrow(() -> TokenException.invalid("Refresh"));

        refreshToken.setEstado(EstadoToken.REVOCADO);
        save(refreshToken);
    }

    // Guarda el token
    public Token save(Token token) {
        logger.debug("Guardando token de refresco ID={}", token.getIdToken());
        return tokenRepository.save(token);
    }

    // Busca por token y tipo
    public Optional<Token> findByToken(String token) {
        logger.debug("Buscando token de refresco: {}", token);
        return tokenRepository.findByTokenAndTipo(token, TipoToken.REFRESH);
    }

    // Busca por usuario, dispositivo y tipo
    public Optional<Token> findByUsuarioAndDispositivo(Usuario usuario, String dispositivo) {
        logger.debug("Buscando token de refresco para usuario {} en dispositivo {}", usuario.getIdUsuario(), dispositivo);
        return tokenRepository.findByUsuarioAndIpDispositivoAndTipo(usuario, dispositivo, TipoToken.REFRESH);
    }
}