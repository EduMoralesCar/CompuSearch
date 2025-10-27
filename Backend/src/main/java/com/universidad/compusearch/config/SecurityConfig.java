package com.universidad.compusearch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.universidad.compusearch.jwt.JwtAuthenticationFilter;
import com.universidad.compusearch.service.UsuarioService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Configuración de seguridad de la aplicación.
 *
 * <p>
 * Esta clase habilita Spring Security, configura el filtro JWT, define los
 * endpoints públicos y
 * protegidos, y establece la política de manejo de sesiones.
 * </p>
 */
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Configura la cadena de filtros de seguridad (SecurityFilterChain) y las
     * reglas de autorización.
     *
     * <p>
     * - Deshabilita CSRF porque se usa JWT y no sesiones de navegador.
     * - Habilita soporte CORS.
     * - Define los endpoints públicos y los que requieren autenticación.
     * - Configura la política de sesiones como stateless.
     * - Registra el filtro JWT antes del filtro de login por username/password.
     * </p>
     *
     * @param http configuración de HttpSecurity
     * @return la cadena de filtros de seguridad
     * @throws Exception si ocurre un error en la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {
                })
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/me",
                                "/usuario/**",
                                "/incidentes/**")
                        .authenticated()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/",
                                "/categorias/**",
                                "/productos/**",
                                "/tiendas/**",
                                "/builds/**",
                                "/etiquetas/**",
                                "/filtro/**",
                                "/componentes/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        log.info("Configuración de seguridad cargada correctamente. Filtros y endpoints asegurados.");
        return http.build();
    }

    /**
     * Define el AuthenticationProvider para autenticar usuarios.
     *
     * <p>
     * Utiliza {@link DaoAuthenticationProvider} con {@link UsuarioService} como
     * {@link org.springframework.security.core.userdetails.UserDetailsService} y
     * {@link PasswordEncoder} para verificar contraseñas.
     * </p>
     *
     * @return el AuthenticationProvider configurado
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(usuarioService);
        authProvider.setPasswordEncoder(passwordEncoder);

        log.info("DaoAuthenticationProvider registrado con UsuarioService y BCryptPasswordEncoder.");
        return authProvider;
    }

    /**
     * Proporciona el {@link AuthenticationManager} que orquesta todo el proceso de
     * autenticación.
     *
     * @param config configuración de autenticación
     * @return AuthenticationManager inicializado
     * @throws Exception si ocurre un error al inicializar
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.info("AuthenticationManager inicializado.");
        return config.getAuthenticationManager();
    }
}
