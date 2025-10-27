package com.universidad.compusearch.service;

import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.Token;
import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.exception.TokenException;
import com.universidad.compusearch.exception.TooManyAttemptsException;
import com.universidad.compusearch.exception.UserException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Servicio de reseteo de contraseña
@Service
@RequiredArgsConstructor
@Slf4j
public class ResetPasswordService {

    private final ResetPasswordAttemptService resetPasswordAttemptService;
    private final AuthService authService;
    private final ResetTokenService resetTokenService;

    // Valida el email y devuelve al usuario si existe
    public Usuario validateEmail(String email) {
        log.info("Validando solicitud de reseteo para email: {}", email);

        if (resetPasswordAttemptService.isBlocked(email)) {
            log.warn("Email bloqueado por demasiados intentos: {}", email);
            throw new TooManyAttemptsException();
        }

        Usuario usuario = authService.findByEmail(email);

        if (usuario == null) {
            log.warn("Email no encontrado: {}", email);
            resetPasswordAttemptService.fail(email);
            throw UserException.notFound();
        }

        resetPasswordAttemptService.success(email);
        log.info("Email validado correctamente: {}", email);
        return usuario;
    }

    // Valida el token de reseteo y devuelve al usuario asociado
    public Usuario validateResetToken(String token) {
        log.debug("Validando token de reseteo: {}", token);

        Token resetToken = resetTokenService.findByToken(token)
                .orElseThrow(() -> {
                    log.warn("Token de reseteo no encontrado o inválido: {}", token);
                    return TokenException.invalid("Reset");
                });

        String email = resetToken.getUsuario().getEmail();

        if (resetPasswordAttemptService.isBlocked(email)) {
            log.warn("Email bloqueado durante validación de token: {}", email);
            throw new TooManyAttemptsException();
        }

        resetTokenService.revokeResetToken(token);
        resetPasswordAttemptService.success(email);

        log.info("Token de reseteo validado y revocado para usuario {}", resetToken.getUsuario().getIdUsuario());
        return resetToken.getUsuario();
    }
}
