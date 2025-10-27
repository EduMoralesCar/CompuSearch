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

/**
 * Filtro de autenticación JWT que se ejecuta una vez por cada solicitud.
 * 
 * Este filtro extrae el token JWT desde las cookies de la solicitud, valida el
 * token
 * y, si es válido, establece la autenticación en el contexto de seguridad de
 * Spring.
 * 
 * Funcionalidades principales:
 * <ul>
 * <li>Extraer el token JWT desde la cookie "access_token".</li>
 * <li>Extraer el nombre de usuario del token.</li>
 * <li>Validar el token contra los detalles del usuario cargados desde la base
 * de datos.</li>
 * <li>Configurar la autenticación en el contexto de seguridad si el token es
 * válido.</li>
 * </ul>
 */
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

    /**
     * Obtiene el valor de una cookie por nombre.
     *
     * @param request La solicitud HTTP.
     * @param name    Nombre de la cookie a buscar.
     * @return El valor de la cookie si existe, de lo contrario null.
     */
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
