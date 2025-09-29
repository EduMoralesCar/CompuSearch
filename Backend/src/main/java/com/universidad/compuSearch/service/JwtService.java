package com.universidad.compuSearch.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.universidad.compuSearch.entity.Usuario;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    @Value("${jwt.token-prefix}")
    private String tokenPrefix;

    @Value("${jwt.header}")
    private String header;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof Usuario usuario) {
            claims.put("roles", usuario.getAuthorities());
            claims.put("tipoUsuario", usuario.getTipoUsuario().name());
        }
        return buildToken(claims, userDetails, expiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    private String buildToken(Map<String, Object> claims, UserDetails userDetails, long durationMs) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + durationMs);
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        log.info("JWT generado para usuario {}", userDetails.getUsername());
        return token;
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(removePrefix(token))
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("Token expirado: {}", token);
            throw e;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token inválido: {}", token);
            throw new RuntimeException("Token inválido", e);
        }
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        if(!(userDetails instanceof Usuario usuario)) return false;
        String username = extractUsername(token);
        boolean valid = username.equals(userDetails.getUsername()) && !isTokenExpired(token) && usuario.isActivo();
        if(!valid) log.warn("Token inválido para usuario {}", userDetails.getUsername());
        return valid;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String addPrefix(String token){
        return tokenPrefix + " " + token;
    }

    public String removePrefix(String token){
        if(token.startsWith(tokenPrefix + " ")) return token.substring(tokenPrefix.length() + 1);
        return token;
    }

    public String getHeader() { return header; }
    public String getTokenPrefix() { return tokenPrefix; }
    public long getExpirationToken() { return expiration; }
    public long getRefreshExpirationToken() { return refreshExpiration; }
}
