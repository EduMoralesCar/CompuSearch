package com.universidad.compuSearch.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.universidad.compuSearch.entity.EstadoToken;
import com.universidad.compuSearch.entity.ResetToken;
import com.universidad.compuSearch.entity.Usuario;
import com.universidad.compuSearch.repository.ResetTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResetTokenService {

    private final ResetTokenRepository resetTokenRepository;

    public ResetToken createToken(Usuario usuario, long expirationMillis) {
        ResetToken token = new ResetToken(usuario, Instant.now().plusMillis(expirationMillis));
        ResetToken savedToken = resetTokenRepository.save(token);
        log.info("Reset token creado para usuario {}", usuario.getEmail());
        return savedToken;
    }

    public ResetToken findValidToken(String tokenStr) {
        ResetToken token = resetTokenRepository.findByToken(tokenStr)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (token.isExpired() || token.getEstado() != EstadoToken.ACTIVO) {
            log.warn("Reset token expirado o revocado: {}", tokenStr);
            throw new RuntimeException("Token expirado o revocado");
        }

        return token;
    }

    public void revokeToken(ResetToken token) {
        token.setEstado(EstadoToken.REVOCADO);
        resetTokenRepository.save(token);
        log.info("Reset token revocado para usuario {}: {}", token.getUsuario().getEmail(), token.getToken());
    }
}

