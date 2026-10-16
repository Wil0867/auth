package com.ecommerce.auth.infraestructure.entry_points;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.usecase.UsuarioUseCase;
import com.ecommerce.auth.infraestructure.driver_adapters.UsuarioData;
import com.ecommerce.auth.infraestructure.entry_points.dto.LoginRequest;
import com.ecommerce.auth.infraestructure.entry_points.dto.RegistroUsuario;
import com.ecommerce.auth.infraestructure.mapper.MapperUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/ecommerce/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;
    private final MapperUsuario mapperUsuario;

    @PostMapping("/save")
    public ResponseEntity<?> guardarUsuario(@RequestBody RegistroUsuario registro) {
        Usuario usuario = mapperUsuario.toUsuario(registro);
        Usuario guardado = usuarioUseCase.guardarUsuario(usuario);
        return ResponseEntity.ok("Registro exitoso de " + guardado.getNombre()
                + " con ID: " + guardado.getIdUsuario());
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable Long idUsuario) {
        return Optional.ofNullable(idUsuario)
                .map(id -> {
                    try {
                        return usuarioUseCase.buscarUsuario(id);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long idUsuario) {
        return Optional.ofNullable(idUsuario)
                .map(id -> {
                    try {
                        usuarioUseCase.eliminarUsuario(id);
                        return ResponseEntity.ok("Usuario eliminado exitosamente");
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
                    }
                })
                .orElseGet(() -> ResponseEntity.badRequest().body("El id del usuario es obligatorio"));
    }

    @PutMapping("/update/{idUsuario}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long idUsuario,
                                               @RequestBody RegistroUsuario registro) {
        Usuario usuario = mapperUsuario.toUsuario(registro);
        usuario.setIdUsuario(idUsuario);
        Usuario actualizado = usuarioUseCase.modificarUsuario(usuario);
        return ResponseEntity.ok("Usuario actualizado: " + actualizado.getIdUsuario());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return Optional.ofNullable(loginRequest)
                .map(req -> {
                    try {
                        Usuario usuario = usuarioUseCase.login(req.getCorreo(), req.getClave());
                        return ResponseEntity.ok("Bienvenido: " + usuario.getNombre());
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.ok("Usuario o Contraseña incorrectos");
                    }
                })
                .orElseGet(() -> ResponseEntity.badRequest().body("Datos de login requeridos"));
    }
    @PostMapping("/{idUsuario}/recargar")
    public ResponseEntity<?> recargarSaldo(
            @PathVariable Long idUsuario,
            @RequestBody Map<String, BigDecimal> request) {

        BigDecimal monto = request.get("monto");

        // Llamamos al caso de uso unificado que maneja la lógica y persistencia
        Usuario usuarioActualizado = usuarioUseCase.recargarSaldo(idUsuario, monto);

        // Retornamos una respuesta clara con el nuevo estado del usuario
        return ResponseEntity.ok(Map.of(
                "mensaje", "Recarga efectuada correctamente",
                "idUsuario", usuarioActualizado.getIdUsuario(),
                "nombre", usuarioActualizado.getNombre(),
                "nuevoSaldo", usuarioActualizado.getSaldo()
        ));
    }

    @GetMapping("/{idUsuario}/saldo")
    public ResponseEntity<?> consultarSaldo(@PathVariable Long idUsuario) {
        try {
            Usuario usuario = usuarioUseCase.buscarUsuario(idUsuario);
            BigDecimal saldo = usuario.getSaldo() == null ? BigDecimal.ZERO : usuario.getSaldo();
            return ResponseEntity.ok(Map.of(
                    "idUsuario", usuario.getIdUsuario(),
                    "nombre", usuario.getNombre(),
                    "saldo", saldo));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "No se encontró un usuario con el id: " + idUsuario));
        }
    }

    @PostMapping("/{idUsuario}/descontar")
    public ResponseEntity<?> descontarSaldo(@PathVariable Long idUsuario,
                                            @RequestBody Map<String, BigDecimal> request) {
        try {
            Usuario usuario = usuarioUseCase.descontarSaldoPorCompra(idUsuario, request.get("monto"));
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Descuento efectuado correctamente",
                    "idUsuario", usuario.getIdUsuario(),
                    "nuevoSaldo", usuario.getSaldo()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", String.valueOf(e.getMessage())));
        }
    }

}