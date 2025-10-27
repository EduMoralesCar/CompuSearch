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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenController {

        private final CookieUtil cookieUtil;
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

                ResponseCookie accessCookie = cookieUtil.createAccessCookie(accessToken);

                ResponseCookie refreshCookie = cookieUtil.createRefreshCookie(newRefreshToken.getToken(), true);

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

                ResponseCookie refreshCookie = cookieUtil.clearRefreshCookie();
                ResponseCookie accessCookie = cookieUtil.clearAccessCookie();

                response.addHeader("Set-Cookie", refreshCookie.toString());
                response.addHeader("Set-Cookie", accessCookie.toString());

                log.info("Sesión cerrada y tokens eliminados");
                return ResponseEntity.ok(new MessageResponse("Sesión cerrada"));
        }

}
