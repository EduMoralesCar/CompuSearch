package com.universidad.compusearch.jwt;

import java.util.Date;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.TipoToken;
import com.universidad.compusearch.exception.TokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtTokenParser {

    private final JwtConfigHelper jwtConfigHelper;

    // Extrae todos los claims del token
    public Claims extractAllClaims(String token) {

        try {
            
            return Jwts.parser()
                    .verifyWith(jwtConfigHelper.getSigningKey())
                    .build()
                    .parseSignedClaims(jwtConfigHelper.removePrefix(token))
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw TokenException.expiredType();
        } catch (JwtException | IllegalArgumentException e) {
            throw TokenException.invalidType();
        }
    }

    // Extrae el nombre de usuario (subject)
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Extrae la fecha de expiración
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    // Extrae un claim específico
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    // Extrae el tipo de token
    public TipoToken extractTipoToken(String token) {
        Claims claims = extractAllClaims(token);
        String tipo = claims.get("tipo", String.class);

        try {
            return TipoToken.valueOf(tipo);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw TokenException.invalidType();
        }
    }
}