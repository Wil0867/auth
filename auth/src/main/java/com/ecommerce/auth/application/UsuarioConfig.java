package com.ecommerce.auth.application;

import com.ecommerce.auth.domain.model.gateway.EncriptarGateway;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import com.ecommerce.auth.domain.usecase.UsuarioUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsuarioConfig {

    @Bean
    public UsuarioUseCase usuarioUseCase(UsuarioGateway usuarioGateway, EncriptarGateway encriptarGateway) {
        return new UsuarioUseCase(usuarioGateway, encriptarGateway);
    }
}
