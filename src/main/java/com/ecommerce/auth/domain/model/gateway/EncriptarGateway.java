package com.ecommerce.auth.domain.model.gateway;

public interface EncriptarGateway {
    String encriptar(String clave);
    boolean verificar(String clavePlana, String claveEncriptada);
}
