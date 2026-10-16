package com.ecommerce.catalogo.application;

import com.ecommerce.catalogo.domain.model.gateway.CarritoGateway;
import com.ecommerce.catalogo.domain.model.gateway.ProductoGateway;
import com.ecommerce.catalogo.domain.model.gateway.UsuarioGateway;
import com.ecommerce.catalogo.domain.usecase.CarritoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CarritoConfig {

    @Bean
    public CarritoUseCase carritoUseCase(CarritoGateway carritoGateway,
                                         ProductoGateway productoGateway,
                                         UsuarioGateway usuarioAuthGateway) {
        return new CarritoUseCase(carritoGateway, productoGateway, usuarioAuthGateway);
    }
}
