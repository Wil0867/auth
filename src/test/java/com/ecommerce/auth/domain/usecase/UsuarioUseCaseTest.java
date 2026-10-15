package com.ecommerce.auth.domain.usecase;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.model.gateway.EncriptarGateway;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    @Mock
    private EncriptarGateway encriptarGateway;

    @InjectMocks
    private UsuarioUseCase usuarioUseCase;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNombre("Juan Perez");
        usuario.setCorreo("juan@gmail.com");
        usuario.setClave("clave123");
        usuario.setRol("ADMIN");
        usuario.setNumeroTelefonico("1234567890");
        usuario.setEdad(25);
    }

    @Test
    void debeGuardarUsuarioEncriptandoClave() {
        when(encriptarGateway.encriptar("clave123")).thenReturn("claveEncriptada");
        when(usuarioGateway.guardarusuario(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = usuarioUseCase.guardarUsuario(usuario);

        assertNotNull(resultado);
        assertEquals("claveEncriptada", resultado.getClave());
        verify(encriptarGateway).encriptar("clave123");
        verify(usuarioGateway).guardarusuario(usuario);
    }

    @Test
    void debeHacerLoginExitoso() {
        when(usuarioGateway.buscarPorCorreo("juan@gmail.com")).thenReturn(usuario);
        when(encriptarGateway.verificar("clave123", "clave123")).thenReturn(true);

        Usuario resultado = usuarioUseCase.login("juan@gmail.com", "clave123");

        assertNotNull(resultado);
        assertEquals("Juan Perez", resultado.getNombre());
        verify(usuarioGateway).buscarPorCorreo("juan@gmail.com");
        verify(encriptarGateway).verificar("clave123", "clave123");
    }

    @Test
    void debeFallarLoginCuandoClaveEsIncorrecta() {
        when(usuarioGateway.buscarPorCorreo("juan@gmail.com")).thenReturn(usuario);
        when(encriptarGateway.verificar("claveErronea", "clave123")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                usuarioUseCase.login("juan@gmail.com", "claveErronea")
        );

        assertEquals("Correo o clave incorrectos", exception.getMessage());
    }
}
