package com.ecommerce.catalogo.application;

import com.ecommerce.catalogo.domain.model.gateway.ProductoGateway;
import com.ecommerce.catalogo.domain.usecase.ProductoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ProductoUseCase productoUseCase(ProductoGateway productoGateway) {
        return new ProductoUseCase(productoGateway);
    }
}