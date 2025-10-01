package com.universidad.compuSearch.service;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.universidad.compuSearch.entity.Usuario;
import com.universidad.compuSearch.exception.TokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
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

    // Construye la clave secreta
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Genera el token de acceso
    public String generateAccessToken(UserDetails userDetails) {

        // Crea un objeto para almacenar los claims
        Map<String, Object> claims = new HashMap<>();
        // Verifica si el userDetails es usuario y coloca los datos en los claims
        if (userDetails instanceof Usuario usuario) {
            claims.put("roles", usuario.getAuthorities());
            claims.put("tipoUsuario", usuario.getTipoUsuario().name());
        }

        // Devuelve el token construido
        return buildToken(claims, userDetails, expiration);
    }

    // Genera el token de refresco
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    // Construye el token segun su duracion, claims y userDatails
    private String buildToken(Map<String, Object> claims, UserDetails userDetails, long durationMs) {

        // Se obtiene la fecha actual y de expiracion
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(durationMs);

        // Se construye el token y se le asignan los claims
        String token = Jwts.builder()
                .claims(claims) // le pasas el mapa directamente
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(getSigningKey())
                .compact();

        // Devuelve el token creado
        return token;
    }

    // Extrae todos los claims del token
    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(removePrefix(token))
                    .getPayload();

        // Si el token esta expirado
        } catch (ExpiredJwtException e) {
            throw TokenException.expired("Access");
        
        // Si el token esta invalido
        } catch (JwtException | IllegalArgumentException e) {
            throw TokenException.invalid("Access");
        }
    }

    // Extrae el nombre de usuario
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Extrae la fecha de expiracion
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    // Devuelve si el token esta expirado
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Devuelve si el token esta invalido
    public boolean isTokenValid(String token, UserDetails userDetails) {

        // Si el userDetails no es usuario devuelve false
        if (!(userDetails instanceof Usuario usuario))
            return false;
        
        // Obtiene el nombre de usuario
        String username = extractUsername(token);

        // Valida si el nombre es igual al nombre de usuario
        // del details y si el token esta expirado
        boolean valid = username.equals(userDetails.getUsername()) && !isTokenExpired(token) && usuario.isActivo();

        // Devuelve la validacion
        return valid;
    }

    // Extra un claim especifico
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Añade el prefijo al token
    public String addPrefix(String token) {
        return tokenPrefix + " " + token;
    }

    // Remueve el prefijo al token
    public String removePrefix(String token) {
        if (token.startsWith(tokenPrefix + " "))
            return token.substring(tokenPrefix.length() + 1);
        return token;
    }

    public String getHeader() {
        return header;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public long getExpirationToken() {
        return expiration;
    }

    public long getRefreshExpirationToken() {
        return refreshExpiration;
    }
}
