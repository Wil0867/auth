package com.ecommerce.auth.infraestructure.security;

import com.ecommerce.auth.domain.model.gateway.EncriptarGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EncriptarImpl implements EncriptarGateway {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encriptar(String clave) {
        return passwordEncoder.encode(clave);
    }

    @Override
    public boolean verificar(String clavePlana, String claveEncriptada) {
        return passwordEncoder.matches(clavePlana, claveEncriptada);
    }
}
