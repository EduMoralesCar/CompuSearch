package com.universidad.compusearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.universidad.compusearch.jwt.JwtConfig;

@SpringBootApplication
@EnableConfigurationProperties(JwtConfig.class)
public class CompuSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompuSearchApplication.class, args);
    } 
}
