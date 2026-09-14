package com.ecommerce.auth.infraestructure.driver_adapters;
import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import com.ecommerce.auth.infraestructure.mapper.MapperUsuario;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

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
            UsuarioData usuarioData = repository.getReferenceById(idUsuario);
            return mapperUsuario.toUsuario(usuarioData);
    }

    @Override
    public Usuario modificarusuario(Usuario usuario) {
            UsuarioData usuarioData = mapperUsuario.toUsuarioData(usuario);
            return mapperUsuario.toUsuario(repository.save(usuarioData));
    }



}






