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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.universidad.compusearch.jwt.JwtAuthenticationFilter;
import com.universidad.compusearch.service.UsuarioService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Configuramos la sseguridad de los endpoints
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UsuarioService usuarioService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desabilita CSRF ya que trabajamos con JWT y no con sesiones
                .csrf(csrf -> csrf.disable())
                // Habilita soporte para CORS
                .cors(cors -> {
                })
                // Configuramos las rutas y sus permisos
                .authorizeHttpRequests(auth -> auth

                        // Solo cuando el usuario este autenticado
                        .requestMatchers("/auth/me").authenticated()
                        // Endpoints públicos: login, registro, refresh token, etc.
                        .requestMatchers("/auth/**").permitAll()
                        // Las paginas de navegacion estan disponibles
                        .requestMatchers("/", "/categorias/**", "/productos/**", "/tiendas/**", 
                        "/builds/**", "/etiquetas/**", "/productos/**", "/filtro/**",
                        "/componentes/**", "/categorias").permitAll()
                        // Todo lo demás requiere autenticación
                        .anyRequest().authenticated())

                // Indicamos que no se usen sesiones: el estado lo mantiene el token
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Registramos nuestro AuthenticationProvider
                .authenticationProvider(authenticationProvider())
                // Agregamos el filtro JWT antes del filtro de login por username/password
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        log.info("Configuración de seguridad cargada correctamente. Filtros y endpoints asegurados.");
        return http.build();
    }

    // Usamos BCrypt para codificar contraseñas de los usuarios
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Definimos cómo autenticar usuarios (cargar desde BD + validar password)
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(usuarioService);
        authProvider.setPasswordEncoder(passwordEncoder());

        log.info("DaoAuthenticationProvider registrado con UsuarioService y BCryptPasswordEncoder.");
        return authProvider;
    }

    // AuthenticationManager: orquesta todo el proceso de autenticación
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.info("AuthenticationManager inicializado.");
        return config.getAuthenticationManager();
    }
}
