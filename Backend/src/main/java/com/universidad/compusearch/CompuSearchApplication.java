package com.universidad.compusearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.universidad.compusearch.jwt.JwtConfig;

/**
 * Clase principal de la aplicación <b>CompuSearch</b>.
 * 
 * Esta clase inicia la aplicación Spring Boot y habilita la configuración 
 * personalizada definida en {@link JwtConfig}.
 * 
 * <p>El método {@link #main(String[])} sirve como punto de entrada 
 * para ejecutar la aplicación.</p>
 * 
 * @see com.universidad.compusearch.jwt.JwtConfig
 */
@SpringBootApplication
@EnableConfigurationProperties(JwtConfig.class)
public class CompuSearchApplication {

    /**
     * Punto de entrada principal para ejecutar la aplicación CompuSearch.
     * 
     * Este método arranca el contexto de Spring Boot, inicializa los beans 
     * definidos y lanza el servidor embebido (por ejemplo, Tomcat).
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        SpringApplication.run(CompuSearchApplication.class, args);
    }
}
