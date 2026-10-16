package com.ecommerce.catalogo.domain.Excepciones;

public class ServicioExternoException extends RuntimeException {
    public ServicioExternoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}