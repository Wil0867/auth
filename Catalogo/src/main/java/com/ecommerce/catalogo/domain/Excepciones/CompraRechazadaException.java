package com.ecommerce.catalogo.domain.Excepciones;

public class CompraRechazadaException extends RuntimeException {
    public CompraRechazadaException(String mensaje) {
        super(mensaje);
    }
}
