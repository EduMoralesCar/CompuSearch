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

/**
 * Controlador REST responsable de gestionar las operaciones de autenticación
 * y registro de usuarios del sistema.
 * <p>
 * Contiene los endpoints para:
 * <ul>
 * <li>Iniciar sesión (login)</li>
 * <li>Registrar un nuevo usuario</li>
 * <li>Obtener la información del usuario autenticado</li>
 * </ul>
 *
 * <p>
 * Utiliza cookies seguras (HttpOnly) para almacenar el token de acceso
 * y el token de refresco.
 * </p>
 * 
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

        /** Utilidad para la creación de cookies seguras. */
        private final CookieUtil cookieUtil;

        /** Servicio encargado de la autenticación y generación de tokens. */
        private final AuthService authService;

        /** Servicio para la gestión y actualización de tokens de refresco. */
        private final RefreshTokenService refreshTokenService;

        /**
         * Endpoint para iniciar sesión.
         * <p>
         * Autentica las credenciales del usuario, genera el token JWT y
         * el token de refresco, y los devuelve mediante cookies seguras.
         * </p>
         *
         * @param request  objeto {@link LoginRequest} con las credenciales del usuario.
         * @param response objeto {@link HttpServletResponse} para adjuntar las cookies.
         * @return una {@link ResponseEntity} con un {@link MessageResponse} indicando
         *         el resultado.
         */
        @PostMapping("/login")
        public ResponseEntity<MessageResponse> login(@Valid @RequestBody LoginRequest request,
                        HttpServletResponse response) {
                log.info("Intento de login para identificador: {}", request.getIdentificador());

                Usuario usuario = authService.authenticate(request.getIdentificador(), request.getContrasena());

                String accessToken = authService.generateJwtToken(usuario);
                Token refreshToken = refreshTokenService.createOrUpdateRefreshToken(usuario, request.getDispositivo());

                // Configuración de cookies seguras
                ResponseCookie accessCookie = cookieUtil.createAccessCookie(accessToken);
                ResponseCookie refreshCookie = cookieUtil.createRefreshCookie(refreshToken.getToken(),
                                request.getRecordar());

                response.addHeader("Set-Cookie", accessCookie.toString());
                response.addHeader("Set-Cookie", refreshCookie.toString());

                log.info("Login exitoso para usuario ID: {}", usuario.getIdUsuario());

                return ResponseEntity.ok(new MessageResponse("Usuario logueado correctamente"));
        }

        /**
         * Endpoint para registrar un nuevo usuario.
         * <p>
         * Crea una nueva cuenta de usuario, genera el token JWT y el token de refresco,
         * y los devuelve como cookies seguras.
         * </p>
         *
         * @param request  objeto {@link RegisterRequest} con los datos de registro del
         *                 usuario.
         * @param response objeto {@link HttpServletResponse} para adjuntar las cookies.
         * @return una {@link ResponseEntity} con un {@link MessageResponse} confirmando
         *         el registro.
         */
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

        /**
         * Endpoint para obtener la información del usuario autenticado.
         * <p>
         * Retorna el identificador, nombre de usuario, tipo de usuario
         * y rol (si aplica) del usuario autenticado.
         * </p>
         *
         * @param usuario objeto {@link Usuario} inyectado automáticamente
         *                mediante {@link AuthenticationPrincipal}.
         * @return una {@link ResponseEntity} con un {@link AuthResponse}
         *         que contiene la información del usuario autenticado.
         */
        @GetMapping("/me")
        public ResponseEntity<AuthResponse> getAuthenticatedUser(@AuthenticationPrincipal Usuario usuario) {
                String rol = null;

                if (usuario instanceof Empleado) {
                        Empleado empleado = (Empleado) usuario;
                        rol = empleado.getRol().name();
                }

                return ResponseEntity.ok(
                                new AuthResponse(
                                                usuario.getIdUsuario(),
                                                usuario.getUsername(),
                                                usuario.getTipoUsuario().name(),
                                                rol));
        }
}
