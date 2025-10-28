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

/**
 * Servicio para extraer información (claims) de un token JWT.
 *
 * <p>
 * Permite obtener el nombre de usuario, la fecha de expiración, el tipo de token
 * y cualquier otro claim personalizado de un JWT firmado.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class JwtTokenParser {

    private final JwtConfigHelper jwtConfigHelper;

    /**
     * Extrae todos los claims de un token JWT.
     *
     * @param token token JWT a procesar
     * @return objeto Claims con toda la información contenida en el token
     * @throws TokenException si el token ha expirado o es inválido
     */
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

    /**
     * Extrae el nombre de usuario (subject) del token.
     *
     * @param token token JWT
     * @return nombre de usuario contenido en el token
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extrae la fecha de expiración del token.
     *
     * @param token token JWT
     * @return fecha de expiración
     */
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    /**
     * Extrae un claim específico del token usando un resolver.
     *
     * @param <T>      tipo del claim
     * @param token    token JWT
     * @param resolver función que obtiene el claim de los Claims
     * @return valor del claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    /**
     * Extrae el tipo de token (ACCESS, REFRESH, RESET) de los claims.
     *
     * @param token token JWT
     * @return tipo de token
     * @throws TokenException si el claim "tipo" es inválido o no está presente
     */
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
