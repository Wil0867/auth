package com.ecommerce.auth.domain.model.gateway;

import com.ecommerce.auth.domain.model.Usuario;


public interface UsuarioGateway {
    Usuario guardarusuario(Usuario usuario);
    Usuario eliminarUsuario(Long idUsuario);
    Usuario buscarusuario(Long idUsuario);
    Usuario modificarusuario(Usuario usuario);
    boolean existeUsuario(Long idUsuario);
    Usuario buscarPorCorreo(String correo);
}
