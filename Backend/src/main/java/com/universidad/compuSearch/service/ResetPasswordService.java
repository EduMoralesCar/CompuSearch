package com.universidad.compuSearch.service;

import org.springframework.stereotype.Service;

import com.universidad.compuSearch.entity.ResetToken;
import com.universidad.compuSearch.entity.Usuario;
import com.universidad.compuSearch.exception.TooManyAttemptsException;
import com.universidad.compuSearch.exception.UserException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {

    private final ResetPasswordAttemptService resetPasswordAttemptService;
    private final AuthService authService;
    private final ResetTokenService resetTokenService;

    // Valida el email y devuelve al usuario del email
    public Usuario validateEmail(String email) {

        // Si el email esta bloqueado sale del metodo
        if (resetPasswordAttemptService.isBlocked(email)) {
            throw new TooManyAttemptsException();
        }

        // Obtiene al usuario
        Usuario usuario = authService.findByEmail(email);

        // Si no lo encuentra aumenta un intento
        if (usuario == null) {
            resetPasswordAttemptService.requestFailed(email);
            throw UserException.notFound();
        }

        // Si encuentra al email borra los intentos
        resetPasswordAttemptService.requestSucceeded(email);

        // Devuelve al usuario
        return usuario;
    }

    // Valida el token de reseteo y devuelve al usuario
    public Usuario validateResetToken(String token) {

        // Obtenemos el token de reseteo
        ResetToken resetToken = resetTokenService.findValidToken(token);
        // Obtenemos el email del token
        String email = resetToken.getUsuario().getEmail();

        // Si esta bloqueado salimos del metodo
        if (resetPasswordAttemptService.isBlocked(email)) {
            throw new TooManyAttemptsException();
        }

        // Si es correcto revocamos este token de reseto
        resetTokenService.revokeToken(resetToken);
        // Borramos los intentos
        resetPasswordAttemptService.requestSucceeded(email);
        // Obtenemos el usuario
        Usuario usuario = resetToken.getUsuario();

        //Devolvemos el usuario
        return usuario;
    }
}
