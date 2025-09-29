package com.universidad.compuSearch.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compuSearch.entity.ResetToken;
import com.universidad.compuSearch.entity.Usuario;
import com.universidad.compuSearch.service.AuthService;
import com.universidad.compuSearch.service.EmailService;
import com.universidad.compuSearch.service.ResetPasswordAttemptService;
import com.universidad.compuSearch.service.ResetTokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth/password")
@RequiredArgsConstructor
public class ResetPasswordController {

    private final AuthService authService;
    private final ResetTokenService resetTokenService;
    private final ResetPasswordAttemptService resetAttemptService;
    private final EmailService emailService;

    @PostMapping("/forgot")
    public ResponseEntity<?> forgot(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email requerido"));
        }

        if (resetAttemptService.isBlocked(email)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of("error", "Demasiados intentos. Intenta más tarde."));
        }

        Usuario usuario = authService.findByEmail(email);
        if (usuario == null) {
            resetAttemptService.requestFailed(email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
        }

        ResetToken token = resetTokenService.createToken(usuario, 3600_000);

        emailService.sendPasswordResetEmail(email, token.getToken());

        resetAttemptService.requestSucceeded(email);
        return ResponseEntity.ok(Map.of(
                "message", "Se ha enviado un correo con instrucciones para restablecer tu contraseña"));
    }

    @PostMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody Map<String, String> request) {
        String tokenStr = request.get("token");
        String nuevaPassword = request.get("password");

        if (tokenStr == null || tokenStr.isBlank() || nuevaPassword == null || nuevaPassword.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Token y nueva contraseña requeridos"));
        }

        if (resetAttemptService.isBlocked(tokenStr)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of("error", "Demasiados intentos con este token. Intenta más tarde."));
        }

        try {
            ResetToken token = resetTokenService.findValidToken(tokenStr);
            authService.updatePassword(token.getUsuario(), nuevaPassword);
            resetTokenService.revokeToken(token);
            resetAttemptService.requestSucceeded(token.getUsuario().getEmail());
            return ResponseEntity.ok(Map.of("message", "Contraseña restablecida correctamente"));
        } catch (RuntimeException e) {
            resetAttemptService.requestFailed(tokenStr);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
