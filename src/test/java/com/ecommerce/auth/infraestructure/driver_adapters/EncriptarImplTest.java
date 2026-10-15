package com.ecommerce.auth.infraestructure.driver_adapters;

import com.ecommerce.auth.infraestructure.security.EncriptarImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class EncriptarImplTest {

    private EncriptarImpl encriptar;

    @BeforeEach
    void setUp() {
        encriptar = new EncriptarImpl(new BCryptPasswordEncoder());
    }

    @Test
    void debeEncriptarClaveCorrectamente() {
        String clavePlana = "Password123";
        String claveEncriptada = encriptar.encriptar(clavePlana);

        assertNotNull(claveEncriptada);
        assertNotEquals(clavePlana, claveEncriptada);
    }

    @Test
    void debeVerificarClaveCorrectamente() {
        String clavePlana = "Password123";
        String claveEncriptada = encriptar.encriptar(clavePlana);

        assertTrue(encriptar.verificar(clavePlana, claveEncriptada));
        assertFalse(encriptar.verificar("ClaveIncorrecta", claveEncriptada));
    }
}
