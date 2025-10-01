package com.universidad.compuSearch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compuSearch.dto.MessageResponse;
import com.universidad.compuSearch.dto.RefreshTokenRequest;
import com.universidad.compuSearch.dto.RefreshTokenResponse;
import com.universidad.compuSearch.entity.RefreshToken;
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

    // EndPoint para refrescar el token de refresco
    @PostMapping
    public ResponseEntity<RefreshTokenResponse> refresh(@RequestBody RefreshTokenRequest request) {

        // Valida el token y obtiene el token
        RefreshToken refreshToken = refreshTokenService.validateAndGetRefreshToken(request.getRefreshToken());
        // Si el token es valido obtiene el usuario
        Usuario usuario = refreshToken.getUsuario();
        // Generar un nuevo token
        String token = authService.generateJwtToken(usuario);

        //Devuelve el token
        return ResponseEntity.ok(new RefreshTokenResponse(token));
    }

    // Endpoint para revocar el token de refresco del usuario
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequest request) {

        // Busca y revoca el token del usuario
        refreshTokenService.revokeRefreshToken(request.getRefreshToken());

        // Devuelve un mensaje de sesion cerrada
        return ResponseEntity.ok(new MessageResponse("Sesión cerrada"));
    }
}

