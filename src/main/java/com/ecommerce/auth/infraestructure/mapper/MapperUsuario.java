package com.ecommerce.auth.infraestructure.mapper;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.infraestructure.driver_adapters.UsuarioData;
import com.ecommerce.auth.infraestructure.entry_points.dto.RegistroUsuario;
import org.springframework.stereotype.Component;

@Component
public class MapperUsuario {

    public Usuario toUsuario(UsuarioData usuarioData){
        return new Usuario(

                usuarioData.getIdUsuario(),
                usuarioData.getNombre(),
                usuarioData.getCorreo(),
                usuarioData.getClave(),
                usuarioData.getRol(),
                usuarioData.getEdad(),
                usuarioData.getNumeroTelefonico(),
                usuarioData.getSaldo()
        );

    }

    public UsuarioData toUsuarioData(Usuario usuario){
        return new UsuarioData(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getClave(),
                usuario.getRol(),
                usuario.getNumeroTelefonico(),
                usuario.getEdad(),
                usuario.getSaldo()
        );
    }

    public Usuario usuario(RegistroUsuario registro){
        Usuario usuario = new Usuario();

        usuario.setNombre(registro.getNombre());
        usuario.setCorreo(registro.getCorreo());
        usuario.setClave(registro.getClave());
        usuario.setRol(registro.getRol());
        usuario.setNumeroTelefonico(registro.getNumeroTelefonico());
        usuario.setEdad(registro.getEdad());
        return usuario;
    }
}