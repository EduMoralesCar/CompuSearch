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

@Service
@RequiredArgsConstructor
public class JwtTokenFactory {

    private final JwtConfig jwtConfig;
    private final JwtConfigHelper jwtConfigHelper;

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

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof Usuario usuario) {
            claims.put("rol", usuario.getAuthorities());
            claims.put("tipoUsuario", usuario.getTipoUsuario().name());
        }
        return buildToken(claims, userDetails.getUsername(), jwtConfig.getExpiration(), TipoToken.ACCESS);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails.getUsername(), jwtConfig.getRefreshExpiration(), TipoToken.REFRESH);
    }

    public String generateResetToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails.getUsername(), jwtConfig.getResetExpiration(), TipoToken.RESET);
    }
}
