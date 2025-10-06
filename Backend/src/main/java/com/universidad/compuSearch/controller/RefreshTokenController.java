package com.universidad.compusearch.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth/refresh")
@RequiredArgsConstructor
public class RefreshTokenController {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenController.class);

    private final RefreshTokenService refreshTokenService;
    private final AuthService authService;

    // Endpoint para refrescar el access token usando el refresh token
    @PostMapping
    public ResponseEntity<MessageResponse> refresh(@CookieValue("refresh_token") String refreshTokenValue,
            HttpServletResponse response) {
        logger.info("Solicitud de renovación de token con refresh_token");

        Token refreshToken = refreshTokenService.validateAndGetRefreshToken(refreshTokenValue);
        Usuario usuario = refreshToken.getUsuario();
        String accessToken = authService.generateJwtToken(usuario);

        ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(10 * 60)
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", accessCookie.toString());

        logger.info("Access token renovado para usuario ID: {}", usuario.getIdUsuario());
        return ResponseEntity.ok(new MessageResponse("Token de acceso renovado correctamente"));
    }

    // Endpoint para cerrar sesión y revocar el refresh token
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(@CookieValue("refresh_token") String refreshTokenValue,
            HttpServletResponse response) {
        logger.info("Solicitud de logout con refresh_token");

        refreshTokenService.revokeRefreshToken(refreshTokenValue);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        logger.info("Sesión cerrada y refresh token revocado");
        return ResponseEntity.ok(new MessageResponse("Sesión cerrada"));
    }
}
