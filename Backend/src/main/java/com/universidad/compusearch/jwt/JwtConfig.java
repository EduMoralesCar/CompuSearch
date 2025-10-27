package com.universidad.compusearch.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuración de JWT cargada desde el archivo {@code application.properties}.
 *
 * Esta clase permite mapear las propiedades definidas con el prefijo {@code jwt} en
 * el archivo de configuración y proporciona los valores necesarios para la generación,
 * validación y manejo de tokens JWT en la aplicación.
 *
 * Propiedades:
 * <ul>
 *     <li>{@code secret}: Clave secreta para firmar los tokens.</li>
 *     <li>{@code expiration}: Tiempo de expiración en milisegundos para los tokens de acceso.</li>
 *     <li>{@code refreshExpiration}: Tiempo de expiración para los tokens de refresco.</li>
 *     <li>{@code resetExpiration}: Tiempo de expiración para los tokens de reseteo de contraseña.</li>
 *     <li>{@code tokenPrefix}: Prefijo del token en el encabezado HTTP (por ejemplo, "Bearer ").</li>
 *     <li>{@code header}: Nombre del encabezado HTTP donde se enviará el token (por ejemplo, "Authorization").</li>
 * </ul>
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtConfig {
    private String secret;
    private long expiration;
    private long refreshExpiration;
    private long resetExpiration;
    private String tokenPrefix;
    private String header;
}
