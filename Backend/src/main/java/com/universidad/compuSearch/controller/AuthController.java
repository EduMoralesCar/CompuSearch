package com.universidad.compuSearch.controller;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.universidad.compuSearch.dto.AuthResponse;
import com.universidad.compuSearch.dto.LoginRequest;
import com.universidad.compuSearch.dto.RegisterRequest;
import com.universidad.compuSearch.entity.RefreshToken;
import com.universidad.compuSearch.entity.TipoUsuario;
import com.universidad.compuSearch.entity.Usuario;
import com.universidad.compuSearch.service.AuthService;
import com.universidad.compuSearch.service.RefreshTokenService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    // Endpoint donde el usuario inicia sesion
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {

        // Autentica al usuario con su contraseña
        Usuario usuario = authService.authenticate(request.getEmail(), request.getContrasena());
        // Si el usuario es valido genera el token
        String token = authService.generateJwtToken(usuario);
        // Genera el token de refresco
        RefreshToken refreshToken = refreshTokenService.createOrUpdateRefreshToken(usuario, request.getDispositivo());

        // Guardar refresh token en cookie HttpOnly
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken.getToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(30 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();

        // Añade al header
        response.addHeader("Set-Cookie", cookie.toString());

        // Devuelve los datos necesarios
        return ResponseEntity.ok(
                new AuthResponse(token,
                        request.getEmail(),
                        usuario.getTipoUsuario().name()));
    }

    // Enpoint donde el usuario se registra
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request,
            HttpServletResponse response) {

        // Se extrae el tipo de usuario (siempre sera de tipo usuario USUARIO)
        TipoUsuario tipo = TipoUsuario.valueOf(request.getTipoUsuario().toUpperCase());
        // Se registra el usuario con su email y contraseña
        Usuario usuario = authService.register(request.getEmail(), request.getContrasena(), tipo);
        // Si el usuario se registro correctamente genera un token
        String token = authService.generateJwtToken(usuario);
        // Genera un token de refresco
        RefreshToken refreshToken = refreshTokenService.createOrUpdateRefreshToken(usuario, "default");

        // Guardar refresh token en cookie HttpOnly
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken.getToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(30 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();

        // Añade al header
        response.addHeader("Set-Cookie", cookie.toString());

        // Devuelve los datos necesarios
        return ResponseEntity.ok(
                new AuthResponse(token,
                        request.getEmail(),
                        usuario.getTipoUsuario().name()));
    }
}
