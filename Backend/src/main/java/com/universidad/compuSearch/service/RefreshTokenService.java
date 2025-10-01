package com.universidad.compuSearch.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.universidad.compuSearch.entity.EstadoToken;
import com.universidad.compuSearch.entity.RefreshToken;
import com.universidad.compuSearch.entity.Usuario;
import com.universidad.compuSearch.exception.TokenException;
import com.universidad.compuSearch.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpirationMillis;

    // Crea o actualiza segun encuentra o no el token de refresco en la base de datos
    public RefreshToken createOrUpdateRefreshToken(Usuario usuario, String device) {
        return refreshTokenRepository
                .findByUsuarioAndDispositivoAndEstado(usuario, device, EstadoToken.ACTIVO)
                .map(this::updateRefreshToken)
                .orElseGet(() -> createRefreshToken(usuario, device));
    }

    // Actualiza el token de refresco
    private RefreshToken updateRefreshToken(RefreshToken existingToken) {
        existingToken.setToken(UUID.randomUUID().toString());
        existingToken.setFechaCreacion(Instant.now());
        existingToken.setFechaExpiracion(Instant.now().plusMillis(refreshExpirationMillis));
        return refreshTokenRepository.save(existingToken);
    }

    // Crea un nuevo token de refresco
    private RefreshToken createRefreshToken(Usuario usuario, String device) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsuario(usuario);
        refreshToken.setDispositivo(device);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setFechaCreacion(Instant.now());
        refreshToken.setFechaExpiracion(Instant.now().plusMillis(refreshExpirationMillis));
        refreshToken.setEstado(EstadoToken.ACTIVO);

        return refreshTokenRepository.save(refreshToken);
    }

    // Retorna el token existe y no esta expirado o revocado
    public RefreshToken validateAndGetRefreshToken(String token) {
    return refreshTokenRepository.findByToken(token)
            .filter(rt -> !rt.isExpired() && rt.getEstado() == EstadoToken.ACTIVO)
            .orElseThrow(() -> TokenException.invalid("Refresh"));
}


    // Encuentra el token de refresco de la base de datos
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> TokenException.notFound("Refresh"));
    }

    // Guarda el token de refresco en la base de datos
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    // Revoca el token de refresco en la base de datos    
    public void revokeRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> TokenException.invalid("Refresh"));

        refreshToken.setEstado(EstadoToken.REVOCADO);
        refreshTokenRepository.save(refreshToken);
    }
}
