package com.example.healthapp.config;

import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    @Primary
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * RestTemplate dedicat descărcării de fișiere PDF (prospecte/SMPC).
     * Timeout-uri mărite față de default pentru fișiere mari de pe serverele EMA.
     */
    @Bean(name = "pdfRestTemplate")
    public RestTemplate pdfRestTemplate(RestTemplateBuilder builder) {
        return builder
            .connectTimeout(Duration.ofSeconds(15))
            .readTimeout(Duration.ofSeconds(60))
            .build();
    }
}
