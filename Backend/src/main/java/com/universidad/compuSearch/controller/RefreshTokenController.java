package com.universidad.compuSearch.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compuSearch.entity.Usuario;
import com.universidad.compuSearch.service.AuthService;
import com.universidad.compuSearch.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth/refresh")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String tokenStr = request.get("refreshToken");
        if (tokenStr == null || tokenStr.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Refresh token requerido"));
        }

        if (!refreshTokenService.validateRefreshToken(tokenStr)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Refresh token inválido o expirado"));
        }

        Usuario usuario = refreshTokenService.findByToken(tokenStr).getUsuario();
        String token = authService.generateJwtToken(usuario);

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request) {
        String tokenStr = request.get("refreshToken");
        if (tokenStr == null || tokenStr.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Refresh token requerido"));
        }

        refreshTokenService.revokeRefreshToken(tokenStr);
        return ResponseEntity.ok(Map.of("message", "Sesión cerrada"));
    }
}

