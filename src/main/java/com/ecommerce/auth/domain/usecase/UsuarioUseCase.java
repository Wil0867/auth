package com.ecommerce.auth.domain.usecase;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.model.gateway.EncriptarGateway;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioGateway usuarioGateway;
    private final EncriptarGateway encriptarGateway;

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
                .filter(correo -> correo.endsWith("@gmail.com") || correo.endsWith("@hotmail.com")
                        || correo.endsWith("@outlook.com") || correo.endsWith("@yahoo.com"))
                .orElseThrow(() -> new IllegalArgumentException(
                        "El correo debe ser de gmail, hotmail, outlook o yahoo"));

        usuarioAValidar.setSaldo(BigDecimal.ZERO);

        usuarioAValidar.setClave(encriptarGateway.encriptar(usuarioAValidar.getClave()));
        usuarioAValidar.setIdUsuario(null);
        return usuarioGateway.guardarusuario(usuarioAValidar);
    }

    public void eliminarUsuario(Long idUsuario) {
        Long idValidado = Optional.ofNullable(idUsuario)
                .filter(id -> id > 0)
                .orElseThrow(() -> new IllegalArgumentException("El id del usuario debe ser válido y mayor a cero"));

        Optional.of(usuarioGateway.existeUsuario(idValidado))
                .filter(existe -> existe)
                .orElseThrow(() -> new IllegalArgumentException("No existe un usuario con el id: " + idValidado));

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
        usuarioAValidar.setClave(encriptarGateway.encriptar(usuarioAValidar.getClave()));

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
                .filter(correo -> correo.contains("@"))
                .orElseThrow(() -> new IllegalArgumentException("El correo es obligatorio y debe contener un @ válido"));

        Usuario existente = usuarioGateway.buscarusuario(usuario.getIdUsuario());
        usuario.setSaldo(existente.getSaldo());
        return usuarioGateway.modificarusuario(usuario);
    }

    public Usuario buscarUsuario(Long idUsuario) {
        Long idValidado = Optional.ofNullable(idUsuario)
                .filter(id -> id > 0)
                .orElseThrow(() -> new IllegalArgumentException("El id del usuario debe ser válido y mayor a cero"));

        return Optional.ofNullable(usuarioGateway.buscarusuario(idValidado))
                .orElseThrow(() -> new IllegalArgumentException("No se encontró un usuario con el id: " + idValidado));
    }

    public Usuario login(String correo, String clave) {
        String correoValidado = Optional.ofNullable(correo)
                .filter(valor -> !valor.trim().isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("El correo es obligatorio"));

        String claveValidada = Optional.ofNullable(clave)
                .filter(valor -> !valor.trim().isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("La clave es obligatoria"));

        Usuario usuario = Optional.ofNullable(usuarioGateway.buscarPorCorreo(correoValidado))
                .orElseThrow(() -> new IllegalArgumentException("Correo o clave incorrectos"));

        return Optional.of(usuario)
                .filter(u -> encriptarGateway.verificar(claveValidada, u.getClave()))
                .orElseThrow(() -> new IllegalArgumentException("Correo o clave incorrectos"));
    }

    public Usuario recargarSaldo(Long idUsuario, BigDecimal montoARecargar) {
        // 1. Validar que el monto no sea nulo y sea mayor a cero
        if (montoARecargar == null || montoARecargar.signum() <= 0) {
            throw new IllegalArgumentException("El monto a recargar debe ser mayor a cero.");
        }

        // 2. Buscar al usuario usando tu método del gateway
        Usuario usuario = usuarioGateway.buscarusuario(idUsuario);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado con el ID: " + idUsuario);
        }

        // Asegurar que si el saldo inicial venía nulo de la BD, empiece en 0 antes de sumar
        if (usuario.getSaldo() == null) {
            usuario.setSaldo(BigDecimal.ZERO);
        }

        BigDecimal nuevoSaldo = usuario.getSaldo().add(montoARecargar);
        usuario.setSaldo(nuevoSaldo);

        // 4. Guardar los cambios en la base de datos
        return usuarioGateway.guardarusuario(usuario);
    }

    public Usuario descontarSaldoPorCompra(Long idUsuario, BigDecimal montoCompra) {
        // 1. Validar que el monto de la compra sea válido
        if (montoCompra == null || montoCompra.signum() <= 0) {
            throw new IllegalArgumentException("El monto de la compra debe ser mayor a cero.");
        }

        // 2. Buscar al usuario
        Usuario usuario = usuarioGateway.buscarusuario(idUsuario);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado con el ID: " + idUsuario);
        }

        if (usuario.getSaldo() == null) {
            usuario.setSaldo(BigDecimal.ZERO);
        }

        // 3. Validar fondos suficientes para evitar saldos negativos
        // compareTo devuelve -1 si el saldo actual del usuario es MENOR que el costo de la compra
        if (usuario.getSaldo().compareTo(montoCompra) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar la compra. El saldo no puede ser negativo.");
        }

        // 4. Restar el dinero y persistir el usuario actualizado
        BigDecimal nuevoSaldo = usuario.getSaldo().subtract(montoCompra);
        usuario.setSaldo(nuevoSaldo);

        return usuarioGateway.guardarusuario(usuario);
    }
}