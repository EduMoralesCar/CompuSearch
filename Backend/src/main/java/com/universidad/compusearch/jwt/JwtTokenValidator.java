package com.universidad.compusearch.jwt;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.Usuario;

import lombok.RequiredArgsConstructor;

/**
 * Servicio para validar tokens JWT.
 *
 * 
 * Permite verificar si un token JWT es válido para un usuario dado, comprobando su
 * expiración y el estado activo del usuario.
 * 
 */
@Service
@RequiredArgsConstructor
public class JwtTokenValidator {

    private final JwtTokenParser jwtTokenParser;

    /**
     * Verifica si un token JWT es válido para un usuario específico.
     *
     * 
     * Comprueba que:
     * <ul>
     *     <li>El nombre de usuario dentro del token coincide con el usuario proporcionado.</li>
     *     <li>El token no ha expirado.</li>
     *     <li>El usuario se encuentra activo.</li>
     * </ul>
     * 
     *
     * @param token       el token JWT a validar
     * @param userDetails el usuario a quien debería pertenecer el token
     * @return {@code true} si el token es válido, {@code false} en caso contrario
     */
    public boolean isValid(String token, UserDetails userDetails) {
        if (!(userDetails instanceof Usuario usuario)) return false;

        String username = jwtTokenParser.extractUsername(token);
        boolean notExpired = !isExpired(token);
        boolean activo = usuario.isActivo();

        return username.equals(userDetails.getUsername()) && notExpired && activo;
    }

    /**
     * Verifica si un token JWT ha expirado.
     *
     * @param token el token JWT
     * @return {@code true} si el token ha expirado, {@code false} si aún es válido
     */
    public boolean isExpired(String token) {
        Date expiration = jwtTokenParser.extractExpiration(token);
        return expiration.before(new Date());
    }
}
