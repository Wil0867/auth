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
    }






