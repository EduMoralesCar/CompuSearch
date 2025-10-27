package com.universidad.compusearch.util;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.universidad.compusearch.jwt.JwtConfig;
import com.universidad.compusearch.jwt.JwtConfigHelper;

import lombok.RequiredArgsConstructor;

/**
 * Clase de utilidad para la creación, configuración y limpieza de cookies
 * utilizadas en el manejo de autenticación JWT dentro del sistema CompuSearch.
 * 
 * <p>Esta clase genera cookies seguras y con políticas apropiadas de 
 * protección (HTTP-only y SameSite) tanto para el token de acceso 
 * como para el token de refresco.</p>
 * 
 * <p>Está marcada con {@link Component} para ser gestionada por el contenedor 
 * de Spring, y utiliza {@link RequiredArgsConstructor} de Lombok para la 
 * inyección del bean {@link JwtConfig}.</p>
 * 
 * @see com.universidad.compusearch.jwt.JwtConfigHelper
 */
@Component
@RequiredArgsConstructor
public class CookieUtil {

    /** Configuración del JWT (expiración, propiedades, etc.) */
    private final JwtConfigHelper jwtConfigHelper;

    /**
     * Crea una cookie de acceso (access token) segura y de solo lectura por el servidor.
     * 
     * <p>Esta cookie se utiliza para almacenar el token JWT de acceso a corto plazo.</p>
     *
     * @param token el token JWT que se almacenará en la cookie.
     * @return una instancia de {@link ResponseCookie} configurada con las propiedades de seguridad apropiadas.
     */
    public ResponseCookie createAccessCookie(String token) {
        return ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .build();
    }

    /**
     * Crea una cookie de refresco (refresh token) con las propiedades de seguridad
     * necesarias y duración configurable según si el usuario eligió la opción de "recordar sesión".
     *
     * @param token el token JWT de refresco.
     * @param recordar valor booleano que indica si la cookie debe tener duración extendida
     *                 basada en {@link JwtConfigHelper#getRefreshTokenExpiration()}.
     * @return una instancia de {@link ResponseCookie} configurada para el refresh token.
     */
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

    /**
     * Limpia (invalida) la cookie de acceso configurándola con valor vacío
     * y tiempo de vida igual a cero.
     *
     * @return una cookie {@link ResponseCookie} que elimina la cookie de acceso existente.
     */
    public ResponseCookie clearAccessCookie() {
        return ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
    }

    /**
     * Limpia (invalida) la cookie de refresco configurándola con valor vacío
     * y tiempo de vida igual a cero.
     *
     * @return una cookie {@link ResponseCookie} que elimina la cookie de refresco existente.
     */
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
