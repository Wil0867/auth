package com.ecommerce.auth.domain.usecase;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
@RequiredArgsConstructor


public class UsuarioUseCase {

    private final UsuarioGateway usuarioGateway;

    public Usuario guardarUsuario(Usuario usuario) {

        // 1. Validar que el objeto completo no sea nulo
        Usuario usuarioAValidar = Optional.ofNullable(usuario)
                .orElseThrow(() -> new IllegalArgumentException("El objeto usuario no puede ser nulo"));

        // 2. Validar Nombre
        Optional.ofNullable(usuarioAValidar.getNombre())
                .filter(nombre -> !nombre.trim().isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("El nombre es obligatorio y no puede estar vacío"));

        // 3. Validar Correo
        Optional.ofNullable(usuarioAValidar.getCorreo())
                .filter(correo -> !correo.trim().isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("El correo es obligatorio y no puede estar vacío"));

        // 4. Validar Clave
        Optional.ofNullable(usuarioAValidar.getClave())
                .filter(clave -> !clave.trim().isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("La clave es obligatoria"));

        // 5. Validar Rol
        Optional.ofNullable(usuarioAValidar.getRol())
                .filter(rol -> !rol.trim().isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("El rol del usuario es obligatorio"));

        // 6. Validar Número Telefónico
        Optional.ofNullable(usuarioAValidar.getNumeroTelefonico())
                .filter(tel -> !tel.trim().isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("El número telefónico es obligatorio"));

        // 7. Validar Edad (No nula y mayor o igual a 18)
        Optional.ofNullable(usuarioAValidar.getEdad())
                .filter(edad -> edad >= 18)
                .orElseThrow(() -> new IllegalArgumentException("El usuario debe ser mayor de edad (mínimo 18 años)"));

        // Si pasa todas las validaciones (ningún orElseThrow se activó), se guarda
        return usuarioGateway.guardarusuario(usuarioAValidar);
    }
}