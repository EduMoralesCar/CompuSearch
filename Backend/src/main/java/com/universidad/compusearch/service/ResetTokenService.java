package com.universidad.compusearch.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.EstadoToken;
import com.universidad.compusearch.entity.TipoToken;
import com.universidad.compusearch.entity.Token;
import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.exception.TokenException;
import com.universidad.compusearch.jwt.JwtConfigHelper;
import com.universidad.compusearch.repository.TokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResetTokenService {

    private final TokenRepository tokenRepository;
    private final JwtConfigHelper jwtConfigHelper;

    // Crear o actualizar el reset token
    public Token createOrUpdateResetToken(Usuario usuario, String dispositivo) {
        log.info("Procesando token de reseteo para usuario {} en dispositivo {}", usuario.getIdUsuario(), dispositivo);

        return findByUsuarioAndDispositivo(usuario, dispositivo)
                .map(existingToken -> {
                    if (existingToken.isExpired() || existingToken.getEstado() == EstadoToken.REVOCADO) {
                        return updateResetToken(existingToken);
                    }
                    return existingToken;
                })
                .orElseGet(() -> {
                    log.info("No se encontró token de reseteo, creando nuevo para usuario {}", usuario.getIdUsuario());
                    return createResetToken(usuario, dispositivo);
                });
    }

    // Actualizar el reset token
    private Token updateResetToken(Token existingToken) {
        log.info("Actualizando token de reseteo ID={} para usuario {}", existingToken.getIdToken(), existingToken.getUsuario().getIdUsuario());

        existingToken.setToken(UUID.randomUUID().toString());
        existingToken.setFechaCreacion(Instant.now());
        existingToken.setFechaExpiracion(Instant.now().plusMillis(jwtConfigHelper.getResetTokenExpiration()));
        existingToken.setEstado(EstadoToken.ACTIVO);

        return save(existingToken);
    }

    private Token createResetToken(Usuario usuario, String dispositivo) {
        log.info("Creando nuevo token de reseteo para usuario {} en dispositivo {}", usuario.getIdUsuario(), dispositivo);

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

    // Validar y obtener el reset token
    public Token validateAndGetResetToken(String token) {
        log.debug("Validando token de reseteo: {}", token);

        return findByToken(token)
                .filter(t -> !t.isExpired() && t.getEstado() == EstadoToken.ACTIVO)
                .orElseThrow(() -> TokenException.invalid("Reset"));
    }

    // Revocar el reset token
    public void revokeResetToken(String token) {
        log.warn("Revocando token de reseteo: {}", token);

        Token resetToken = findByToken(token)
                .orElseThrow(() -> TokenException.invalid("Reset"));

        resetToken.setEstado(EstadoToken.REVOCADO);
        save(resetToken);
    }

    // Guardar el reset token
    public Token save(Token token) {
        log.debug("Guardando token de reseteo ID={}", token.getIdToken());
        return tokenRepository.save(token);
    }

    // Encontrar el reset token
    public Optional<Token> findByToken(String token) {
        log.debug("Buscando token de reseteo: {}", token);
        return tokenRepository.findByTokenAndTipo(token, TipoToken.RESET);
    }

    // Encontrar el reset token por usuario y dispositivo
    public Optional<Token> findByUsuarioAndDispositivo(Usuario usuario, String dispositivo) {
        log.debug("Buscando token de reseteo para usuario {} en dispositivo {}", usuario.getIdUsuario(), dispositivo);
        return tokenRepository.findByUsuario_IdUsuarioAndIpDispositivoAndTipo(usuario.getIdUsuario(), dispositivo, TipoToken.RESET);
    }
}
