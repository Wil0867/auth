package com.ecommerce.auth.application;

import com.ecommerce.auth.domain.usecase.UsuarioUseCase;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UsuarioConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UsuarioUseCase usuarioUseCase(UsuarioGateway usuarioGateway, PasswordEncoder passwordEncoder) {
        return new UsuarioUseCase(usuarioGateway, passwordEncoder());
    }

}
