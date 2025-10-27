package com.universidad.compusearch.jwt;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

/**
 * Clase auxiliar para manejar la configuración y utilidades relacionadas con JWT.
 *
 * <p>
 * Proporciona métodos para obtener la clave de firma, manejar prefijos de tokens,
 * obtener encabezados y tiempos de expiración según la configuración definida
 * en {@link JwtConfig}.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class JwtConfigHelper {

    private final JwtConfig jwtConfig;

    /**
     * Obtiene la clave secreta en formato {@link SecretKey} para firmar los tokens JWT.
     *
     * @return clave secreta HMAC-SHA generada a partir del valor base64 de la configuración
     */
    public SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Añade el prefijo configurado al token.
     *
     * @param token el token JWT sin prefijo
     * @return token con el prefijo añadido
     */
    public String addPrefix(String token) {
        return jwtConfig.getTokenPrefix() + " " + token;
    }

    /**
     * Elimina el prefijo configurado del token si existe.
     *
     * @param token token JWT con prefijo
     * @return token sin prefijo
     */
    public String removePrefix(String token) {
        String prefix = jwtConfig.getTokenPrefix() + " ";
        return token.startsWith(prefix) ? token.substring(prefix.length()) : token;
    }

    /**
     * Obtiene el nombre del encabezado HTTP donde se espera recibir el token.
     *
     * @return nombre del encabezado (por ejemplo, "Authorization")
     */
    public String getHeaderName() {
        return jwtConfig.getHeader();
    }

    /**
     * Obtiene el tiempo de expiración configurado para los tokens de acceso.
     *
     * @return tiempo de expiración en milisegundos
     */
    public long getAccessTokenExpiration() {
        return jwtConfig.getExpiration();
    }

    /**
     * Obtiene el tiempo de expiración configurado para los tokens de refresco.
     *
     * @return tiempo de expiración en milisegundos
     */
    public long getRefreshTokenExpiration() {
        return jwtConfig.getRefreshExpiration();
    }

    /**
     * Obtiene el tiempo de expiración configurado para los tokens de reseteo.
     *
     * @return tiempo de expiración en milisegundos
     */
    public long getResetTokenExpiration() {
        return jwtConfig.getResetExpiration();
    }

    /**
     * Obtiene el prefijo configurado para los tokens.
     *
     * @return prefijo del token (por ejemplo, "Bearer")
     */
    public String getTokenPrefix() {
        return jwtConfig.getTokenPrefix();
    }
}
