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

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

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
