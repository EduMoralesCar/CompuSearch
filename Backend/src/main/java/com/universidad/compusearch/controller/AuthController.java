package com.universidad.compusearch.controller;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.universidad.compusearch.dto.RegisterRequest;
import com.universidad.compusearch.dto.AuthResponse;
import com.universidad.compusearch.dto.LoginRequest;
import com.universidad.compusearch.dto.MessageResponse;
import com.universidad.compusearch.entity.Empleado;
import com.universidad.compusearch.entity.EstadoSuscripcion;
import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.entity.TipoUsuario;
import com.universidad.compusearch.entity.Token;
import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.service.AuthService;
import com.universidad.compusearch.service.RefreshTokenService;
import com.universidad.compusearch.util.CookieUtil;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

        private final CookieUtil cookieUtil;
        private final AuthService authService;
        private final RefreshTokenService refreshTokenService;

        @PostMapping("/login")
        public ResponseEntity<MessageResponse> login(@Valid @RequestBody LoginRequest request,
                        HttpServletResponse response) {
                log.info("Intento de login para identificador: {}", request.getIdentificador());

                Usuario usuario = authService.authenticate(request.getIdentificador(), request.getContrasena());

                String accessToken = authService.generateJwtToken(usuario);
                Token refreshToken = refreshTokenService.createOrUpdateRefreshToken(usuario, request.getDispositivo());

                ResponseCookie accessCookie = cookieUtil.createAccessCookie(accessToken);
                ResponseCookie refreshCookie = cookieUtil.createRefreshCookie(refreshToken.getToken(),
                                request.getRecordar());

                response.addHeader("Set-Cookie", accessCookie.toString());
                response.addHeader("Set-Cookie", refreshCookie.toString());

                log.info("Login exitoso para usuario ID: {}", usuario.getIdUsuario());

                return ResponseEntity.ok(new MessageResponse("Usuario logueado correctamente"));
        }

        @PostMapping("/register")
        public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest request,
                        HttpServletResponse response) {
                log.info("Registro solicitado para email: {}", request.getEmail());

                TipoUsuario tipo = TipoUsuario.valueOf(request.getTipoUsuario().toUpperCase());
                Usuario usuario = authService.register(request.getUsername(), request.getEmail(),
                                request.getContrasena(), tipo);

                String accessToken = authService.generateJwtToken(usuario);
                Token refreshToken = refreshTokenService.createOrUpdateRefreshToken(usuario, request.getDispositivo());

                ResponseCookie accessCookie = cookieUtil.createAccessCookie(accessToken);
                ResponseCookie refreshCookie = cookieUtil.createRefreshCookie(refreshToken.getToken(), true);

                response.addHeader("Set-Cookie", accessCookie.toString());
                response.addHeader("Set-Cookie", refreshCookie.toString());

                log.info("Usuario registrado con ID: {}", usuario.getIdUsuario());

                return ResponseEntity.ok(new MessageResponse("Usuario registrado correctamente"));
        }

        @GetMapping("/me")
        public ResponseEntity<AuthResponse> getAuthenticatedUser(@AuthenticationPrincipal Usuario usuario) {
                String rol = null;
                boolean susActiva = false;

                if (usuario instanceof Empleado) {
                        Empleado empleado = (Empleado) usuario;
                        rol = empleado.getRol().name();
                }

                if (usuario instanceof Tienda) {
                        Tienda tienda = (Tienda) usuario;

                        susActiva = tienda.getSuscripciones().stream()
                                        .anyMatch(s -> s.getEstado() == EstadoSuscripcion.ACTIVA
                                                        || s.getEstado() == EstadoSuscripcion.CAMBIO_PROGRAMADO
                                                        || s.getEstado() == EstadoSuscripcion.CANCELADA_PROGRAMADA);
                }

                return ResponseEntity.ok(
                                new AuthResponse(
                                                usuario.getIdUsuario(),
                                                usuario.getUsername(),
                                                usuario.getTipoUsuario().name(),
                                                rol,
                                                susActiva));
        }
}
