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

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenController {

        private final RefreshTokenService refreshTokenService;
        private final AuthService authService;

        @PostMapping("/refresh")
        public ResponseEntity<MessageResponse> refresh(@CookieValue("refresh_token") String refreshTokenValue,
                        HttpServletResponse response) {
                log.info("Solicitud de renovación de token con refresh_token");

                Token refreshToken = refreshTokenService.validateAndGetRefreshToken(refreshTokenValue);
                Usuario usuario = refreshToken.getUsuario();
                String accessToken = authService.generateJwtToken(usuario);
                Token newRefreshToken = refreshTokenService.createOrUpdateRefreshToken(usuario,
                                refreshToken.getIpDispositivo());

                ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(10 * 60)
                                .sameSite("Strict")
                                .build();

                ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", newRefreshToken.getToken())
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(7 * 24 * 60 * 60)
                                .sameSite("Strict")
                                .build();

                response.addHeader("Set-Cookie", refreshCookie.toString());
                response.addHeader("Set-Cookie", accessCookie.toString());

                log.info("Access token renovado para usuario ID: {}", usuario.getIdUsuario());
                return ResponseEntity.ok(new MessageResponse("Token de acceso renovado correctamente"));
        }

        @PostMapping("/logout")
        public ResponseEntity<MessageResponse> logout(@CookieValue("refresh_token") String refreshTokenValue,
                        HttpServletResponse response) {
                log.info("Solicitud de logout con refresh_token");

                refreshTokenService.revokeRefreshToken(refreshTokenValue);

                ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", "")
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(0)
                                .sameSite("Strict")
                                .build();

                ResponseCookie accessCookie = ResponseCookie.from("access_token", "")
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(0)
                                .sameSite("Strict")
                                .build();

                response.addHeader("Set-Cookie", refreshCookie.toString());
                response.addHeader("Set-Cookie", accessCookie.toString());

                log.info("Sesión cerrada y tokens eliminados");
                return ResponseEntity.ok(new MessageResponse("Sesión cerrada"));
        }

}
