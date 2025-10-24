package com.universidad.compusearch.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

// Clase auxliar para obtener los datos del jwt del
// aplication.properties
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