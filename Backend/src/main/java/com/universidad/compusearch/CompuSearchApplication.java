package com.universidad.compusearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.universidad.compusearch.jwt.JwtConfig;
import com.universidad.compusearch.stripe.StripeConfig;

@SpringBootApplication
@EnableConfigurationProperties({JwtConfig.class, StripeConfig.class})
public class CompuSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompuSearchApplication.class, args);
    }
}
