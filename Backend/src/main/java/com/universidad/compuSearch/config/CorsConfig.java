package com.universidad.compusearch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

// Configuracion para aceptar peticiones desde otro origen
@Configuration
@Slf4j
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Aplica CORS a todos los endpoints de la API
                registry.addMapping("/**")
                        // Permite peticiones desde el frontend
                        .allowedOrigins("http://localhost:5173") 
                        // Métodos HTTP que acepta tu backend desde ese origen
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        // Permite enviar cookies o cabeceras de autenticación 
                        .allowCredentials(true); 

                log.info("CORS configurado para origen: http://localhost:5173");
            }
        };
    }
}