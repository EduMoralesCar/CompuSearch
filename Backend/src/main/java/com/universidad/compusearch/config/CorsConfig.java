package com.universidad.compusearch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

/**
 * Configuración de CORS (Cross-Origin Resource Sharing) para la aplicación.
 *
 * <p>
 * Permite que el frontend (por ejemplo, en http://localhost:5173) haga solicitudes
 * al backend, incluyendo cookies y cabeceras de autenticación.
 * </p>
 */
@Configuration
@Slf4j
public class CorsConfig {

    /**
     * Configura las reglas de CORS para toda la API.
     *
     * <p>
     * Se permiten los métodos GET, POST, PUT, DELETE y OPTIONS desde el origen especificado.
     * También se permite el envío de cookies o cabeceras de autenticación.
     * </p>
     *
     * @return un {@link WebMvcConfigurer} con la configuración de CORS aplicada
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowCredentials(true);

                log.info("CORS configurado para origen: http://localhost:5173");
            }
        };
    }
}
