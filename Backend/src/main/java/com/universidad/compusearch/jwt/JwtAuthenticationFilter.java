package com.universidad.compusearch.jwt;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.universidad.compusearch.service.UsuarioService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenValidator jwtTokenValidator;
    private final JwtTokenParser jwtTokenParser;
    private final UsuarioService usuarioService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            // Extraer token desde cookies
            String token = getCookieValue(request, "access_token");

            if (StringUtils.isBlank(token)) {
                log.debug("No se encontró el token en las cookies.");
                filterChain.doFilter(request, response);
                return;
            }

            // Extraer username desde el token
            String username = jwtTokenParser.extractUsername(token);

            // Verificar si ya hay autenticación en el contexto
            if (StringUtils.isNotBlank(username) &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {
                log.debug("Intentando autenticar usuario: {}", username);

                // Cargar usuario desde base de datos
                UserDetails userDetails = usuarioService.loadUserByUsername(username);

                // Validar token contra el usuario
                if (jwtTokenValidator.isValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    log.info("Autenticación exitosa para el usuario: {}", username);
                } else {
                    log.warn("Token inválido o expirado para usuario: {}", username);
                }
            }

        } catch (Exception e) {
            log.error("Error procesando JWT: {}", e.getMessage(), e);
        }

        // Continuar con el flujo del filtro
        filterChain.doFilter(request, response);
    }

    private String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() == null)
            return null;
        return Arrays.stream(request.getCookies())
                .filter(c -> name.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
