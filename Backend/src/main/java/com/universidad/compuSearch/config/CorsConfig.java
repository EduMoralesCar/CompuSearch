package com.universidad.compusearch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    private static final Logger logger = LoggerFactory.getLogger(CorsConfig.class);

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

                logger.info("CORS configurado para origen: http://localhost:5173");
            }
        };
    }
}