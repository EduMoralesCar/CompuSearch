package com.universidad.compusearch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.dto.ForgotPasswordRequest;
import com.universidad.compusearch.dto.MessageResponse;
import com.universidad.compusearch.dto.ResetPasswordRequest;
import com.universidad.compusearch.entity.Token;
import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.service.AuthService;
import com.universidad.compusearch.service.EmailService;
import com.universidad.compusearch.service.ResetPasswordService;
import com.universidad.compusearch.service.ResetTokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para recuperación y reseteo de contraseñas de usuarios.
 *
 * <p>
 * Proporciona endpoints para iniciar la recuperación de contraseña mediante correo electrónico y
 * para restablecer la contraseña usando un token de reseteo.
 * </p>
 *
 * <p>
 * Base URL: <b>/auth/password</b>
 * </p>
 */
@RestController
@RequestMapping("/auth/password")
@RequiredArgsConstructor
@Slf4j
public class ResetPasswordController {

    private final AuthService authService;
    private final ResetTokenService resetTokenService;
    private final EmailService emailService;
    private final ResetPasswordService resetPasswordService;

    /**
     * Inicia el proceso de recuperación de contraseña.
     *
     * <p>
     * Valida el correo electrónico del usuario y genera un token de reseteo. Luego envía un correo
     * electrónico con instrucciones para restablecer la contraseña.
     * </p>
     *
     * @param request Objeto que contiene el email del usuario y el dispositivo desde el cual se solicita el reseteo
     * @return Mensaje indicando que se envió un correo con instrucciones para restablecer la contraseña
     */
    @PostMapping("/forgot")
    public ResponseEntity<MessageResponse> forgot(@Valid @RequestBody ForgotPasswordRequest request) {
        log.info("Solicitud de recuperación de contraseña para email: {}", request.getEmail());

        Usuario usuario = resetPasswordService.validateEmail(request.getEmail());
        Token resetToken = resetTokenService.createOrUpdateResetToken(usuario, request.getDispositivo());
        emailService.sendPasswordResetEmail(request.getEmail(), resetToken.getToken());

        log.info("Token de reseteo generado y enviado para usuario ID: {}", usuario.getIdUsuario());
        return ResponseEntity
                .ok(new MessageResponse("Se ha enviado un correo con instrucciones para restablecer tu contraseña"));
    }

    /**
     * Aplica una nueva contraseña usando un token de reseteo válido.
     *
     * <p>
     * Valida el token de reseteo, actualiza la contraseña del usuario y elimina el token.
     * </p>
     *
     * @param request Objeto que contiene el token de reseteo y la nueva contraseña
     * @return Mensaje indicando que la contraseña fue restablecida correctamente
     */
    @PostMapping("/reset")
    public ResponseEntity<MessageResponse> reset(@Valid @RequestBody ResetPasswordRequest request) {
        log.info("Solicitud de reseteo de contraseña con token: {}", request.getToken().substring(0, 6));

        Usuario usuario = resetPasswordService.validateResetToken(request.getToken());
        authService.updatePassword(usuario, request.getContrasena());

        log.info("Contraseña actualizada para usuario ID: {}", usuario.getIdUsuario());
        return ResponseEntity.ok(new MessageResponse("Contraseña restablecida correctamente"));
    }
}
