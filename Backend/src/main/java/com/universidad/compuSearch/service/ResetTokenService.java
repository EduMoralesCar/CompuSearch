package com.universidad.compuSearch.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.universidad.compuSearch.entity.EstadoToken;
import com.universidad.compuSearch.entity.ResetToken;
import com.universidad.compuSearch.entity.Usuario;
import com.universidad.compuSearch.exception.TokenException;
import com.universidad.compuSearch.repository.ResetTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResetTokenService {

    private final ResetTokenRepository resetTokenRepository;

    // Creamos un token de reseteo y lo guardamos y devolvemos
    public ResetToken createToken(Usuario usuario, long expirationMillis) {
        ResetToken token = new ResetToken(usuario, Instant.now().plusMillis(expirationMillis));
        ResetToken savedToken = resetTokenRepository.save(token);
        return savedToken;
    }

    // Buscamos el valido token en la base de datos y lo devolvemos
    public ResetToken findValidToken(String tokenStr) {
        ResetToken token = resetTokenRepository.findByToken(tokenStr)
                .orElseThrow(() -> TokenException.notFound("Reset"));
        validateToken(token);
        return token;
    }

    // Revocamos el token y lo guardamos
    public void revokeToken(ResetToken token) {
        validateToken(token);
        token.setEstado(EstadoToken.REVOCADO);
        resetTokenRepository.save(token);
    }

    private void validateToken(ResetToken token) {
        if (token.isExpired()) {
            throw TokenException.expired("Reset");
        }
        if (token.getEstado() != EstadoToken.ACTIVO) {
            throw TokenException.invalid("Reset");
        }
    }
}