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

@RestController
@RequestMapping("/auth/password")
@RequiredArgsConstructor
@Slf4j
public class ResetPasswordController {

    private final AuthService authService;
    private final ResetTokenService resetTokenService;
    private final EmailService emailService;
    private final ResetPasswordService resetPasswordService;

    // Endpoint para iniciar recuperación de contraseña
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

    // Endpoint para aplicar nueva contraseña usando el token
    @PostMapping("/reset")
    public ResponseEntity<MessageResponse> reset(@Valid @RequestBody ResetPasswordRequest request) {
        log.info("Solicitud de reseteo de contraseña con token: {}", request.getToken().substring(0, 6));

        Usuario usuario = resetPasswordService.validateResetToken(request.getToken());
        authService.updatePassword(usuario, request.getContrasena());

        log.info("Contraseña actualizada para usuario ID: {}", usuario.getIdUsuario());
        return ResponseEntity.ok(new MessageResponse("Contraseña restablecida correctamente"));
    }
}
