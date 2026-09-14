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

        Optional.of(usuarioAValidar.getCorreo())
                .filter(correo -> true)
                .filter(correo -> correo.contains("@"))
                .orElseThrow(() -> new IllegalArgumentException("El correo es obligatorio y debe contener un @ válido"));

        // Si pasa todas las validaciones (ningún orElseThrow se activó), se guarda
        return usuarioGateway.guardarusuario(usuarioAValidar);


    }

    public void eliminarUsuario(Long idUsuario) {
        Long idValidado = Optional.ofNullable(idUsuario)
                .filter(id -> id > 0)
                .orElseThrow(() -> new IllegalArgumentException("El id del usuario debe ser válido y mayor a cero"));

        usuarioGateway.eliminarUsuario(idValidado);
    }

    public Usuario modificarUsuario(Usuario usuario) {
        Usuario usuarioAValidar = Optional.ofNullable(usuario)
                .orElseThrow(() -> new IllegalArgumentException("El objeto usuario no puede ser nulo"));

        Optional.ofNullable(usuarioAValidar.getIdUsuario())
                .filter(id -> id > 0)
                .orElseThrow(() -> new IllegalArgumentException("El id del usuario es obligatorio para modificar"));

        Optional.ofNullable(usuarioAValidar.getNombre())
                .filter(nombre -> !nombre.trim().isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("El nombre es obligatorio y no puede estar vacío"));

        Optional.ofNullable(usuarioAValidar.getCorreo())
                .filter(correo -> !correo.trim().isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("El correo es obligatorio y no puede estar vacío"));

        Optional.ofNullable(usuarioAValidar.getClave())
                .filter(clave -> !clave.trim().isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("La clave es obligatoria"));

        Optional.ofNullable(usuarioAValidar.getRol())
                .filter(rol -> !rol.trim().isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("El rol del usuario es obligatorio"));

        Optional.ofNullable(usuarioAValidar.getNumeroTelefonico())
                .filter(tel -> !tel.trim().isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("El número telefónico es obligatorio"));

        Optional.ofNullable(usuarioAValidar.getEdad())
                .filter(edad -> edad >= 18)
                .orElseThrow(() -> new IllegalArgumentException("El usuario debe ser mayor de edad (mínimo 18 años)"));

        Optional.of(usuarioAValidar.getCorreo())
                .filter(correo -> true)
                .filter(correo -> correo.contains("@"))
                .orElseThrow(() -> new IllegalArgumentException("El correo es obligatorio y debe contener un @ válido"));

        return usuarioGateway.modificarusuario(usuarioAValidar);
    }

    public Usuario buscarUsuario(Long idUsuario) {
        Long idValidado = Optional.ofNullable(idUsuario)
                .filter(id -> id > 0)
                .orElseThrow(() -> new IllegalArgumentException("El id del usuario debe ser válido y mayor a cero"));

        return Optional.ofNullable(usuarioGateway.buscarusuario(idValidado))
                .orElseThrow(() -> new IllegalArgumentException("No se encontró un usuario con el id: " + idValidado));
    }
}