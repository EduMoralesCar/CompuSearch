package com.universidad.compusearch.jwt;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtConfigHelper {

    private final JwtConfig jwtConfig;

    // Clave secreta para firmar los tokens
    public SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Añade el prefijo
    public String addPrefix(String token) {
        return jwtConfig.getTokenPrefix() + " " + token;
    }

    // Remueve el prefijo del token si existe
    public String removePrefix(String token) {
        String prefix = jwtConfig.getTokenPrefix() + " ";
        return token.startsWith(prefix) ? token.substring(prefix.length()) : token;
    }

    // Obtiene el nombre del header donde se espera el token
    public String getHeaderName() {
        return jwtConfig.getHeader();
    }

    // Expiraciones configuradas
    public long getAccessTokenExpiration() {
        return jwtConfig.getExpiration();
    }

    public long getRefreshTokenExpiration() {
        return jwtConfig.getRefreshExpiration();
    }

    public long getResetTokenExpiration() {
        return jwtConfig.getResetExpiration();
    }

    // Prefijo configurado
    public String getTokenPrefix() {
        return jwtConfig.getTokenPrefix();
    }
}