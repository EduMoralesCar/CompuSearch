package com.universidad.compusearch.jwt;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.TipoToken;
import com.universidad.compusearch.entity.Usuario;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de generar tokens JWT para distintos propósitos:
 * acceso, refresco y reseteo de contraseña.
 *
 * <p>
 * Utiliza la configuración definida en {@link JwtConfig} y las utilidades de {@link JwtConfigHelper}.
 * Todos los tokens contienen información de tipo y, opcionalmente, roles y tipo de usuario.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class JwtTokenFactory {

    private final JwtConfig jwtConfig;
    private final JwtConfigHelper jwtConfigHelper;

    /**
     * Método base para construir cualquier tipo de token JWT.
     *
     * @param claims     mapa de claims adicionales a incluir en el token
     * @param subject    el "subject" del token, típicamente el username del usuario
     * @param durationMs duración del token en milisegundos
     * @param tipoToken  tipo de token (ACCESS, REFRESH, RESET)
     * @return token JWT firmado como String
     */
    private String buildToken(Map<String, Object> claims, String subject, long durationMs, TipoToken tipoToken) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(durationMs);

        claims.put("tipo", tipoToken.name());

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(jwtConfigHelper.getSigningKey())
                .compact();
    }

    /**
     * Genera un token de acceso (ACCESS) para el usuario especificado.
     * Incluye roles y tipo de usuario en los claims si se trata de una instancia de {@link Usuario}.
     *
     * @param userDetails detalles del usuario autenticado
     * @return token JWT de acceso
     */
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof Usuario usuario) {
            claims.put("rol", usuario.getAuthorities());
            claims.put("tipoUsuario", usuario.getTipoUsuario().name());
        }
        return buildToken(claims, userDetails.getUsername(), jwtConfig.getExpiration(), TipoToken.ACCESS);
    }

    /**
     * Genera un token de refresco (REFRESH) para el usuario especificado.
     * Este token normalmente se usa para obtener un nuevo token de acceso.
     *
     * @param userDetails detalles del usuario autenticado
     * @return token JWT de refresco
     */
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails.getUsername(), jwtConfig.getRefreshExpiration(), TipoToken.REFRESH);
    }

    /**
     * Genera un token de reseteo de contraseña (RESET) para el usuario especificado.
     *
     * @param userDetails detalles del usuario autenticado
     * @return token JWT de reseteo de contraseña
     */
    public String generateResetToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails.getUsername(), jwtConfig.getResetExpiration(), TipoToken.RESET);
    }
}
