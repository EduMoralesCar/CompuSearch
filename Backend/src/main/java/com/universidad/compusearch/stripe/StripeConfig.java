package com.universidad.compusearch.stripe;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "stripe")
public class StripeConfig {

    private String secret;

    private String webhook;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secret;
    }
}
