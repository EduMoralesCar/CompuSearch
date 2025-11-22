package com.universidad.compusearch.service;

import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.Token;
import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.exception.TokenException;
import com.universidad.compusearch.exception.TooManyAttemptsException;
import com.universidad.compusearch.exception.UserException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio para manejar la lógica de reseteo de contraseña.
 * <p>
 * Valida emails, tokens de reseteo y controla los intentos para prevenir abusos.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ResetPasswordService {

    private final ResetPasswordAttemptService resetPasswordAttemptService;
    private final AuthService authService;
    private final ResetTokenService resetTokenService;

    /**
     * Valida que el email exista en el sistema y que no esté bloqueado
     * por demasiados intentos de reseteo.
     *
     * @param email correo electrónico del usuario
     * @return usuario asociado al email
     * @throws TooManyAttemptsException si el email está bloqueado por intentos fallidos
     * @throws UserException            si no existe el usuario con el email proporcionado
     */
    public Usuario validateEmail(String email) {
        log.info("Validando solicitud de reseteo para email: {}", email);

        if (resetPasswordAttemptService.isBlocked(email)) {
            log.warn("Email bloqueado por demasiados intentos: {}", email);
            throw TooManyAttemptsException.resetPassword();
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

    /**
     * Valida un token de reseteo de contraseña y devuelve el usuario asociado.
     * Además, revoca el token y registra el intento como exitoso.
     *
     * @param token token de reseteo recibido
     * @return usuario asociado al token
     * @throws TokenException           si el token es inválido o no existe
     * @throws TooManyAttemptsException si el usuario está bloqueado por intentos
     */
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
            throw TooManyAttemptsException.resetPassword();
        }

        // Revocar token para que no pueda reutilizarse
        resetTokenService.revokeResetToken(token);
        resetPasswordAttemptService.success(email);

        log.info("Token de reseteo validado y revocado para usuario {}", resetToken.getUsuario().getIdUsuario());
        return resetToken.getUsuario();
    }
}
