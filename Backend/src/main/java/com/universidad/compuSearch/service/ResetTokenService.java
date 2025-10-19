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
public class ResetTokenService {

    private final TokenRepository tokenRepository;
    private final JwtConfigHelper jwtConfigHelper;

    private static final Logger logger = LoggerFactory.getLogger(ResetTokenService.class);

    // Crea o actualiza el token de reseteo según el estado del existente
    public Token createOrUpdateResetToken(Usuario usuario, String dispositivo) {
        logger.info("Procesando token de reseteo para usuario {} en dispositivo {}", usuario.getIdUsuario(), dispositivo);

        return findByUsuarioAndDispositivo(usuario, dispositivo)
                .map(existingToken -> {
                    if (existingToken.isExpired() || existingToken.getEstado() == EstadoToken.REVOCADO) {
                        return updateResetToken(existingToken);
                    }
                    return existingToken;
                })
                .orElseGet(() -> {
                    logger.info("No se encontró token de reseteo, creando nuevo para usuario {}", usuario.getIdUsuario());
                    return createResetToken(usuario, dispositivo);
                });
    }

    // Actualiza el token de reseteo
    private Token updateResetToken(Token existingToken) {
        logger.info("Actualizando token de reseteo ID={} para usuario {}", existingToken.getIdToken(), existingToken.getUsuario().getIdUsuario());

        existingToken.setToken(UUID.randomUUID().toString());
        existingToken.setFechaCreacion(Instant.now());
        existingToken.setFechaExpiracion(Instant.now().plusMillis(jwtConfigHelper.getResetTokenExpiration()));
        existingToken.setEstado(EstadoToken.ACTIVO);

        return save(existingToken);
    }

    // Crea un nuevo token de reseteo
    private Token createResetToken(Usuario usuario, String dispositivo) {
        logger.info("Creando nuevo token de reseteo para usuario {} en dispositivo {}", usuario.getIdUsuario(), dispositivo);

        Token resetToken = new Token();
        resetToken.setUsuario(usuario);
        resetToken.setIpDispositivo(dispositivo);
        resetToken.setToken(UUID.randomUUID().toString());
        resetToken.setFechaCreacion(Instant.now());
        resetToken.setFechaExpiracion(Instant.now().plusMillis(jwtConfigHelper.getResetTokenExpiration()));
        resetToken.setTipo(TipoToken.RESET);
        resetToken.setEstado(EstadoToken.ACTIVO);

        return save(resetToken);
    }

    // Valida que el token esté activo y no expirado
    public Token validateAndGetResetToken(String token) {
        logger.debug("Validando token de reseteo: {}", token);

        return findByToken(token)
                .filter(t -> !t.isExpired() && t.getEstado() == EstadoToken.ACTIVO)
                .orElseThrow(() -> TokenException.invalid("Reset"));
    }

    // Revoca el token de reseteo
    public void revokeResetToken(String token) {
        logger.warn("Revocando token de reseteo: {}", token);

        Token resetToken = findByToken(token)
                .orElseThrow(() -> TokenException.invalid("Reset"));

        resetToken.setEstado(EstadoToken.REVOCADO);
        save(resetToken);
    }

    // Guarda el token
    public Token save(Token token) {
        logger.debug("Guardando token de reseteo ID={}", token.getIdToken());
        return tokenRepository.save(token);
    }

    // Busca por token y tipo
    public Optional<Token> findByToken(String token) {
        logger.debug("Buscando token de reseteo: {}", token);
        return tokenRepository.findByTokenAndTipo(token, TipoToken.RESET);
    }

    // Busca por usuario, dispositivo y tipo
    public Optional<Token> findByUsuarioAndDispositivo(Usuario usuario, String dispositivo) {
        logger.debug("Buscando token de reseteo para usuario {} en dispositivo {}", usuario.getIdUsuario(), dispositivo);
        return tokenRepository.findByUsuarioAndIpDispositivoAndTipo(usuario, dispositivo, TipoToken.RESET);
    }
}