package com.universidad.compusearch.jwt;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.Usuario;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtTokenValidator {

    private final JwtTokenParser jwtTokenParser;

    public boolean isValid(String token, UserDetails userDetails) {
        if (!(userDetails instanceof Usuario usuario)) return false;

        String username = jwtTokenParser.extractUsername(token);
        boolean notExpired = !isExpired(token);
        boolean activo = usuario.isActivo();

        return username.equals(userDetails.getUsername()) && notExpired && activo;
    }

    public boolean isExpired(String token) {
        Date expiration = jwtTokenParser.extractExpiration(token);
        return expiration.before(new Date());
    }
}
