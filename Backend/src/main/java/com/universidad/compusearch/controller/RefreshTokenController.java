package com.universidad.compusearch.controller;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.compusearch.dto.MessageResponse;
import com.universidad.compusearch.entity.Token;
import com.universidad.compusearch.entity.Usuario;
import com.universidad.compusearch.service.AuthService;
import com.universidad.compusearch.service.RefreshTokenService;
import com.universidad.compusearch.util.CookieUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para manejo de tokens de refresco y cierre de sesión.
 *
 * <p>
 * Permite renovar el token de acceso mediante un refresh token y cerrar sesión
 * eliminando cookies.
 * </p>
 *
 * <p>
 * Base URL: <b>/auth</b>
 * </p>
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenController {

        private final CookieUtil cookieUtil;
        private final RefreshTokenService refreshTokenService;
        private final AuthService authService;

        /**
         * Renueva el token de acceso utilizando un refresh token válido.
         *
         * <p>
         * Recibe el refresh token desde la cookie "refresh_token" y responde con un
         * nuevo access token
         * y un nuevo refresh token en cookies HTTP.
         * </p>
         *
         * @param refreshTokenValue Valor del refresh token desde la cookie
         * @param response          HttpServletResponse para agregar las cookies
         *                          renovadas
         * @return Mensaje indicando que el token de acceso fue renovado correctamente
         */
        @PostMapping("/refresh")
        public ResponseEntity<MessageResponse> refresh(@CookieValue("refresh_token") String refreshTokenValue,
                        HttpServletResponse response) {
                log.info("Solicitud de renovación de token con refresh_token");

                Token refreshToken = refreshTokenService.validateAndGetRefreshToken(refreshTokenValue);
                Usuario usuario = refreshToken.getUsuario();
                String accessToken = authService.generateJwtToken(usuario);
                Token newRefreshToken = refreshTokenService.createOrUpdateRefreshToken(usuario,
                                refreshToken.getIpDispositivo());

                ResponseCookie accessCookie = cookieUtil.createAccessCookie(accessToken);
                ResponseCookie refreshCookie = cookieUtil.createRefreshCookie(newRefreshToken.getToken(), true);

                response.addHeader("Set-Cookie", refreshCookie.toString());
                response.addHeader("Set-Cookie", accessCookie.toString());

                log.info("Access token renovado para usuario ID: {}", usuario.getIdUsuario());
                return ResponseEntity.ok(new MessageResponse("Token de acceso renovado correctamente"));
        }

        /**
         * Cierra sesión del usuario eliminando las cookies de access y refresh tokens.
         *
         * <p>
         * Recibe el refresh token desde la cookie "refresh_token" y lo revoca.
         * </p>
         *
         * @param refreshTokenValue Valor del refresh token desde la cookie
         * @param response          HttpServletResponse para limpiar las cookies
         * @return Mensaje indicando que la sesión fue cerrada correctamente
         */
        @PostMapping("/logout")
        public ResponseEntity<MessageResponse> logout(@CookieValue("refresh_token") String refreshTokenValue,
                        HttpServletResponse response) {
                log.info("Solicitud de logout con refresh_token");

                refreshTokenService.revokeRefreshToken(refreshTokenValue);

                ResponseCookie refreshCookie = cookieUtil.clearRefreshCookie();
                ResponseCookie accessCookie = cookieUtil.clearAccessCookie();

                response.addHeader("Set-Cookie", refreshCookie.toString());
                response.addHeader("Set-Cookie", accessCookie.toString());

                log.info("Sesión cerrada y tokens eliminados");
                return ResponseEntity.ok(new MessageResponse("Sesión cerrada"));
        }
}
