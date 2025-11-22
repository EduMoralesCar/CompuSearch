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

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final TokenRepository tokenRepository;
    private final JwtConfigHelper jwtConfigHelper;

    // Crear y actualizar token de refresco
    public Token createOrUpdateRefreshToken(Usuario usuario, String dispositivo) {
        log.info("Procesando token de refresco para usuario {} en dispositivo {}", usuario.getIdUsuario(), dispositivo);

        return findByUsuarioAndDispositivo(usuario, dispositivo)
                .map(existingToken -> {
                    if (existingToken.isExpired() || existingToken.getEstado() == EstadoToken.REVOCADO) {
                        return updateRefreshToken(existingToken);
                    }
                    return existingToken;
                })
                .orElseGet(() -> createRefreshToken(usuario, dispositivo));
    }

    // Actualizar token de refresco
    private Token updateRefreshToken(Token existingToken) {
        log.info("Actualizando token de refresco ID={} para usuario {}", existingToken.getIdToken(),
                existingToken.getUsuario().getIdUsuario());

        existingToken.setToken(UUID.randomUUID().toString());
        existingToken.setFechaCreacion(Instant.now());
        existingToken.setFechaExpiracion(Instant.now().plusMillis(jwtConfigHelper.getRefreshTokenExpiration()));
        existingToken.setEstado(EstadoToken.ACTIVO);

        return save(existingToken);
    }

    // Crear y actualizar el token de refresco
    private Token createRefreshToken(Usuario usuario, String dispositivo) {
        log.info("Creando nuevo token de refresco para usuario {} en dispositivo {}", usuario.getIdUsuario(), dispositivo);

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

    // Validar y obtener el token de refresco
    public Token validateAndGetRefreshToken(String token) {
        log.debug("Validando token de refresco: {}", token);

        return findByToken(token)
                .filter(t -> !t.isExpired() && t.getEstado() == EstadoToken.ACTIVO)
                .orElseThrow(() -> TokenException.invalid("Refresh"));
    }

    // Revocar el token de refresco
    public void revokeRefreshToken(String token) {
        log.warn("Revocando token de refresco: {}", token);

        Token refreshToken = findByToken(token)
                .orElseThrow(() -> TokenException.invalid("Refresh"));

        refreshToken.setEstado(EstadoToken.REVOCADO);
        save(refreshToken);
    }

    // Guardar el token de refresco
    public Token save(Token token) {
        log.debug("Guardando token de refresco ID={}", token.getIdToken());
        return tokenRepository.save(token);
    }

    // Obtener el token de refresco
    public Optional<Token> findByToken(String token) {
        log.debug("Buscando token de refresco: {}", token);
        return tokenRepository.findByTokenAndTipo(token, TipoToken.REFRESH);
    }
    
    // Obtener token por usuario y dispositivo
    public Optional<Token> findByUsuarioAndDispositivo(Usuario usuario, String dispositivo) {
        log.debug("Buscando token de refresco para usuario {} en dispositivo {}", usuario.getIdUsuario(), dispositivo);
        return tokenRepository.findByUsuario_IdUsuarioAndIpDispositivoAndTipo(usuario.getIdUsuario(), dispositivo, TipoToken.REFRESH);
    }
}
