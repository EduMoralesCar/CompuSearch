package com.universidad.compusearch.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.universidad.compusearch.dto.RegisterRequest;
import com.universidad.compusearch.dto.AuthResponse;
import com.universidad.compusearch.dto.LoginRequest;
import com.universidad.compusearch.dto.MessageResponse;
import com.universidad.compusearch.entity.TipoUsuario;
import com.universidad.compusearch.entity.Token;
import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.service.AuthService;
import com.universidad.compusearch.service.RefreshTokenService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

        private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

        private final AuthService authService;
        private final RefreshTokenService refreshTokenService;

        // Endpoint de login
        @PostMapping("/login")
        public ResponseEntity<MessageResponse> login(@Valid @RequestBody LoginRequest request,
                        HttpServletResponse response) {
                logger.info("Intento de login para identificador: {}", request.getIdentificador());

                Usuario usuario = authService.authenticate(request.getIdentificador(), request.getContrasena());
                String accessToken = authService.generateJwtToken(usuario);
                Token refreshToken = refreshTokenService.createOrUpdateRefreshToken(usuario,
                                request.getDispositivo());

                // Cookies HttpOnly
                ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(10 * 60)
                                .sameSite("Strict")
                                .build();

                ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken.getToken())
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(request.isRecordar() ? 30 * 24 * 60 * 60 : -1)
                                .sameSite("Strict")
                                .build();

                response.addHeader("Set-Cookie", accessCookie.toString());
                response.addHeader("Set-Cookie", refreshCookie.toString());

                logger.info("Login exitoso para usuario ID: {}", usuario.getIdUsuario());

                return ResponseEntity.ok(new MessageResponse("Usuario logueado correctamente"));
        }

        // Endpoint de registro
        @PostMapping("/register")
        public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest request,
                        HttpServletResponse response) {
                logger.info("Registro solicitado para email: {}", request.getEmail());

                TipoUsuario tipo = TipoUsuario.valueOf(request.getTipoUsuario().toUpperCase());
                Usuario usuario = authService.register(request.getUsername(), request.getEmail(),
                                request.getContrasena(), tipo);

                String accessToken = authService.generateJwtToken(usuario);
                Token refreshToken = refreshTokenService.createOrUpdateRefreshToken(usuario, request.getDispositivo());

                ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(30 * 24 * 60 * 60)
                                .sameSite("Strict")
                                .build();

                ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken.getToken())
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(30 * 24 * 60 * 60)
                                .sameSite("Strict")
                                .build();

                response.addHeader("Set-Cookie", accessCookie.toString());
                response.addHeader("Set-Cookie", refreshCookie.toString());

                logger.info("Usuario registrado con ID: {}", usuario.getIdUsuario());

                return ResponseEntity.ok(new MessageResponse("Usuario registrado correctamente"));
        }

        @GetMapping("/me")
        public ResponseEntity<AuthResponse> getAuthenticatedUser(@AuthenticationPrincipal Usuario usuario) {
                return ResponseEntity.ok(new AuthResponse(usuario.getUsername(), usuario.getTipoUsuario().name()));
        }
}