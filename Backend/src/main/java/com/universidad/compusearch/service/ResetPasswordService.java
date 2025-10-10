package com.universidad.compusearch.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.Token;
import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.exception.TokenException;
import com.universidad.compusearch.exception.TooManyAttemptsException;
import com.universidad.compusearch.exception.UserException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {

    private final ResetPasswordAttemptService resetPasswordAttemptService;
    private final AuthService authService;
    private final ResetTokenService resetTokenService;

    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordService.class);

    // Valida el email y devuelve al usuario si existe
    public Usuario validateEmail(String email) {
        logger.info("Validando solicitud de reseteo para email: {}", email);

        if (resetPasswordAttemptService.isBlocked(email)) {
            logger.warn("Email bloqueado por demasiados intentos: {}", email);
            throw new TooManyAttemptsException();
        }

        Usuario usuario = authService.findByEmail(email);

        if (usuario == null) {
            logger.warn("Email no encontrado: {}", email);
            resetPasswordAttemptService.requestFailed(email);
            throw UserException.notFound();
        }

        resetPasswordAttemptService.requestSucceeded(email);
        logger.info("Email validado correctamente: {}", email);
        return usuario;
    }

    // Valida el token de reseteo y devuelve al usuario asociado
    public Usuario validateResetToken(String token) {
        logger.debug("Validando token de reseteo: {}", token);

        Token resetToken = resetTokenService.findByToken(token)
                .orElseThrow(() -> {
                    logger.warn("Token de reseteo no encontrado o inválido: {}", token);
                    return TokenException.invalid("Reset");
                });

        String email = resetToken.getUsuario().getEmail();

        if (resetPasswordAttemptService.isBlocked(email)) {
            logger.warn("Email bloqueado durante validación de token: {}", email);
            throw new TooManyAttemptsException();
        }

        resetTokenService.revokeResetToken(token);
        resetPasswordAttemptService.requestSucceeded(email);

        logger.info("Token de reseteo validado y revocado para usuario {}", resetToken.getUsuario().getIdUsuario());
        return resetToken.getUsuario();
    }
}
