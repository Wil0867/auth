package com.ecommerce.auth.domain.model.gateway;

import com.ecommerce.auth.domain.model.Usuario;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public interface UsuarioGateway {
    @Bean
    Usuario guardarusuario(Usuario usuario);
    Usuario eliminarUsuario(Long idUsuario);
    Usuario buscarusuario(Long idUsuario);
    Usuario modificarusuario(Usuario usuario);

}