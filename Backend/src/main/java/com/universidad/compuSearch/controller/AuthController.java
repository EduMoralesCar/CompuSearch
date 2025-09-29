package com.universidad.compuSearch.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.universidad.compuSearch.dto.LoginRequest;
import com.universidad.compuSearch.dto.RegisterRequest;
import com.universidad.compuSearch.entity.RefreshToken;
import com.universidad.compuSearch.entity.TipoUsuario;
import com.universidad.compuSearch.entity.Usuario;
import com.universidad.compuSearch.exception.AuthException;
import com.universidad.compuSearch.service.AuthService;
import com.universidad.compuSearch.service.RefreshTokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            Usuario usuario = authService.authenticate(request.getEmail(), request.getContrasena());

            String token = authService.generateJwtToken(usuario);

            RefreshToken refreshToken = refreshTokenService.createOrUpdateRefreshToken(usuario,
                    request.getDispositivo());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "refreshToken", refreshToken.getToken(),
                    "data", Map.of(
                            "email", usuario.getEmail(),
                            "rol", usuario.getTipoUsuario().name(),
                            "device", request.getDispositivo())));
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "success", false,
                            "error", "Credenciales inválidas"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            TipoUsuario tipo = TipoUsuario.valueOf(request.getTipoUsuario().toUpperCase());
            Usuario usuario = authService.register(request.getEmail(), request.getContrasena(), tipo);
            String token = authService.generateJwtToken(usuario);
            RefreshToken refreshToken = refreshTokenService.createOrUpdateRefreshToken(usuario, "default");

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", Map.of(
                            "token", token,
                            "refreshToken", refreshToken.getToken(),
                            "user", Map.of(
                                    "email", usuario.getEmail(),
                                    "rol", usuario.getTipoUsuario().name()))));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "error", "Tipo de usuario inválido"));
        } catch (AuthException e) {
            String mensajeError;
            if (e.getMessage().contains("email")) {
                mensajeError = "El email ya está en uso";
            } else if (e.getMessage().contains("contraseña")) {
                mensajeError = "Contraseña no válida";
            } else {
                mensajeError = "No se pudo registrar el usuario";
            }

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "error", mensajeError));
        }
    }

}
