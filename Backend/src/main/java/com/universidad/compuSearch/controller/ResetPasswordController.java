package com.universidad.compuSearch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compuSearch.dto.ForgotPasswordRequest;
import com.universidad.compuSearch.dto.MessageResponse;
import com.universidad.compuSearch.dto.ResetPasswordRequest;
import com.universidad.compuSearch.entity.ResetToken;
import com.universidad.compuSearch.entity.Usuario;
import com.universidad.compuSearch.service.AuthService;
import com.universidad.compuSearch.service.EmailService;
import com.universidad.compuSearch.service.ResetPasswordService;
import com.universidad.compuSearch.service.ResetTokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth/password")
@RequiredArgsConstructor
public class ResetPasswordController {

    private final AuthService authService;
    private final ResetTokenService resetTokenService;
    private final EmailService emailService;
    private final ResetPasswordService resetPasswordService;

    // Endpoint que recibe el correo del usuario
    @PostMapping("/forgot")
    public ResponseEntity<MessageResponse> forgot(@Valid @RequestBody ForgotPasswordRequest request) {

        // Obtiene el email
        String email = request.getEmail();
        // Obtiene el usuario del email
        Usuario usuario = resetPasswordService.validateEmail(email);
        // Crea el token de reseteo por el usuario
        ResetToken resetToken = resetTokenService.createToken(usuario, 3600_000);
        // Envia el token al email del usuario
        emailService.sendPasswordResetEmail(email, resetToken.getToken());

        return ResponseEntity.ok(new MessageResponse("Se ha enviado un correo con instrucciones para restablecer tu contraseña"));
    }

    // Endpoint para cambiar la contraseña del usuario despues de forgot
    @PostMapping("/reset")
    public ResponseEntity<MessageResponse> reset(@Valid @RequestBody ResetPasswordRequest request) {

        // Obtenemos el token
        String token = request.getToken();
        // Obtenemos la contraseña
        String nuevaContrasena = request.getContrasena();
        //Obtenemos al usuario
        Usuario usuario = resetPasswordService.validateResetToken(token);
        // Actualizamos la contraseña
        authService.updatePassword(usuario, nuevaContrasena);

        return ResponseEntity.ok(new MessageResponse("Contraseña restablecida correctamente"));
    }
}
