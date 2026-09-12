package com.ecommerce.auth.infraestructure.entry_points;
import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.usecase.UsuarioUseCase;
import com.ecommerce.auth.infraestructure.mapper.MapperUsuario;
import com.ecommerce.auth.infraestructure.driver_adapters.UsuarioData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/ecommerce/usuario")
@RequiredArgsConstructor

public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;
    private final MapperUsuario mapperUsuario;

    @PostMapping("/save")
    public ResponseEntity<Usuario> guardarUsuario(@RequestBody UsuarioData usuarioData){
        Usuario usuario = mapperUsuario.toUsuario(usuarioData);
        Usuario usuarioValidaGuardado = usuarioUseCase.guardarUsuario(usuario);

        if(usuarioValidaGuardado.getIdUsuario() != null){
            return new ResponseEntity<>(usuarioValidaGuardado, HttpStatus.OK);
        }

        return new ResponseEntity<>(usuarioValidaGuardado, HttpStatus.CONFLICT);
    }


}