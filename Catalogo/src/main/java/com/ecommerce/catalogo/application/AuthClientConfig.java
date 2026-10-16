package com.ecommerce.catalogo.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class AuthClientConfig {

    /** Cliente HTTP apuntando a Auth. La URL sale de application.properties (auth.service.url). */
    @Bean
    public RestClient authRestClient(@Value("${auth.service.url}") String authUrl) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(3));   // Si Auth está apagado, no se queda colgado
        factory.setReadTimeout(Duration.ofSeconds(5));

        return RestClient.builder()
                .baseUrl(authUrl)
                .requestFactory(factory)
                .build();
    }
}