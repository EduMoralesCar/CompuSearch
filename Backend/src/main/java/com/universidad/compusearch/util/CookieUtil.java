package com.universidad.compusearch.util;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.universidad.compusearch.jwt.JwtConfigHelper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CookieUtil {

    private final JwtConfigHelper jwtConfigHelper;

    // Crear cookie de acceso
    public ResponseCookie createAccessCookie(String token) {
        return ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .build();
    }

    // Crear cookie de refresco
    public ResponseCookie createRefreshCookie(String token, boolean recordar) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from("refresh_token", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict");

        if (recordar) {
            builder.maxAge(jwtConfigHelper.getRefreshTokenExpiration());
        }

        return builder.build();
    }

    // Borrar la cookie de acceso
    public ResponseCookie clearAccessCookie() {
        return ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
    }

    // Borrar la cookie de refresco
    public ResponseCookie clearRefreshCookie() {
        return ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
    }
}
