package com.crop.cropbackend.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Configuration class for RestTemplate Bean
 * 
 * RestTemplate is used to make HTTP requests to external APIs.
 * This configuration sets up a RestTemplate bean with proper timeout settings.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Creates and configures a RestTemplate bean for REST API calls
     * 
     * The RestTemplate is configured with:
     * - Connection timeout: 5 seconds (time to establish connection)
     * - Read timeout: 10 seconds (time to wait for response)
     * 
     * @param builder The RestTemplateBuilder provided by Spring Boot
     * @return Configured RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                // Time to wait while establishing a connection
                .setConnectTimeout(Duration.ofSeconds(5))
                // Time to wait for the server response
                .setReadTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * Alternative method to create RestTemplate without builder
     * (Commented out - using builder approach above is recommended)
     * 
     * @return Configured RestTemplate instance
     */
    /*
     * @Bean
     * public RestTemplate restTemplateAlternative() {
     * SimpleClientHttpRequestFactory factory = new
     * SimpleClientHttpRequestFactory();
     * factory.setConnectTimeout(5000); // 5 seconds
     * factory.setReadTimeout(10000); // 10 seconds
     * return new RestTemplate(factory);
     * }
     */
}
