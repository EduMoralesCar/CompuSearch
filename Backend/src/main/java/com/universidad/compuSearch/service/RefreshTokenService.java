package com.universidad.compuSearch.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.universidad.compuSearch.entity.EstadoToken;
import com.universidad.compuSearch.entity.RefreshToken;
import com.universidad.compuSearch.entity.Usuario;
import com.universidad.compuSearch.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpirationMillis;

    public RefreshToken createOrUpdateRefreshToken(Usuario usuario, String device) {
        RefreshToken existingToken = refreshTokenRepository
                .findByUsuarioAndDispositivoAndEstado(usuario, device, EstadoToken.ACTIVO)
                .orElse(null);

        if (existingToken != null) {
            existingToken.setToken(UUID.randomUUID().toString());
            existingToken.setFechaCreacion(Instant.now());
            existingToken.setFechaExpiracion(Instant.now().plusMillis(refreshExpirationMillis));
            return refreshTokenRepository.save(existingToken);
        }

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsuario(usuario);
        refreshToken.setDispositivo(device);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setFechaCreacion(Instant.now());
        refreshToken.setFechaExpiracion(Instant.now().plusMillis(refreshExpirationMillis));
        refreshToken.setEstado(EstadoToken.ACTIVO);

        return refreshTokenRepository.save(refreshToken);
    }

    public boolean validateRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .filter(rt -> !rt.isExpired() && rt.getEstado() == EstadoToken.ACTIVO)
                .isPresent();
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token no encontrado"));
    }

    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    public void revokeRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token inválido"));

        refreshToken.setEstado(EstadoToken.REVOCADO);
        refreshTokenRepository.save(refreshToken);
        log.info("Refresh token revocado: {}", token);
    }

}
