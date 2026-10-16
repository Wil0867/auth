package com.ecommerce.auth.infraestructure.driver_adapters;
import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import com.ecommerce.auth.infraestructure.mapper.MapperUsuario;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
@AllArgsConstructor

    public class UsuarioDataGatewayImpl implements UsuarioGateway {

    private final MapperUsuario mapperUsuario;
    private final UsuarioDataJpaRepository repository;


    @Override
    public Usuario guardarusuario(Usuario usuario) {
    UsuarioData usuarioData = mapperUsuario.toUsuarioData(usuario);
    return mapperUsuario.toUsuario(repository.save(usuarioData));
    }


    @Override
    public Usuario eliminarUsuario(Long idUsuario) {
        repository.deleteById(idUsuario);

        return null;
    }

    @Override
    public Usuario buscarusuario(Long idUsuario) {
        return repository.findById(idUsuario)
                .map(mapperUsuario::toUsuario)
                .orElse(null);
    }

    @Override
    public Usuario modificarusuario(Usuario usuario) {
            UsuarioData usuarioData = mapperUsuario.toUsuarioData(usuario);
            return mapperUsuario.toUsuario(repository.save(usuarioData));
    }

    @Override
    public boolean existeUsuario(Long idUsuario) {
        return repository.existsById(idUsuario);
    }

    @Override
    public Usuario buscarPorCorreo(String correo) {
        return repository.findByCorreo(correo)
                .map(mapperUsuario::toUsuario)
                .orElse(null);
    }

}






