package com.universidad.compusearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.universidad.compusearch.jwt.JwtConfig;
import com.universidad.compusearch.stripe.StripeConfig;

import io.micrometer.core.instrument.MeterRegistry;

@SpringBootApplication
@EnableConfigurationProperties({JwtConfig.class, StripeConfig.class})
public class CompuSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompuSearchApplication.class, args);
    }

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("application", "CompuSearch");
    }
}
