package com.ecommerce.auth.infraestructure.entry_points;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.usecase.UsuarioUseCase;
import com.ecommerce.auth.infraestructure.driver_adapters.UsuarioData;
import com.ecommerce.auth.infraestructure.entry_points.dto.LoginRequest;
import com.ecommerce.auth.infraestructure.mapper.MapperUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("api/ecommerce/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;
    private final MapperUsuario mapperUsuario;

    @PostMapping("/save")
    public ResponseEntity<?> guardarUsuario(@RequestBody UsuarioData usuarioData) {
        return Optional.ofNullable(usuarioData)
                .map(mapperUsuario::toUsuario)
                .map(usuarioUseCase::guardarUsuario)
                .map(usuarioGuardado -> usuarioGuardado.getIdUsuario() != null
                        ? ResponseEntity.ok("Registro éxitoso")
                        : ResponseEntity.status(HttpStatus.CONFLICT).body(usuarioGuardado))
                .orElseGet(() -> ResponseEntity.badRequest().build());
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

    @PutMapping("/{idUsuario}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long idUsuario, @RequestBody UsuarioData usuarioData) {
        return Optional.ofNullable(usuarioData)
                .map(mapperUsuario::toUsuario)
                .map(usuario -> {
                    usuario.setIdUsuario(idUsuario);
                    try {
                        return usuarioUseCase.modificarUsuario(usuario);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
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
}