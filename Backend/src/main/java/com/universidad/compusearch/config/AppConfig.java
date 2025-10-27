package com.universidad.compusearch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración de la aplicación.
 *
 * <p>
 * Proporciona beans necesarios para la aplicación, como el codificador de contraseñas.
 * </p>
 */
@Configuration
public class AppConfig {

    /**
     * Bean de {@link PasswordEncoder} para codificar y verificar contraseñas
     * utilizando el algoritmo BCrypt.
     *
     * @return una instancia de {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
