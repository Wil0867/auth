package com.ecommerce.catalogo.infraestructure.entry_points;

import com.ecommerce.catalogo.domain.Excepciones.CompraRechazadaException;
import com.ecommerce.catalogo.domain.Excepciones.RecursoNoEncontradoException;
import com.ecommerce.catalogo.domain.Excepciones.ServicioExternoException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Traduce las excepciones de TODOS los controladores (Producto y Carrito) a códigos HTTP.
 * Así los casos de uso dicen QUÉ salió mal y aquí se decide CÓMO responder.
 */
@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> validacion(IllegalArgumentException e) {
        return respuesta(HttpStatus.BAD_REQUEST, e.getMessage());                    // 400
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> noEncontrado(RecursoNoEncontradoException e) {
        return respuesta(HttpStatus.NOT_FOUND, e.getMessage());                      // 404
    }

    @ExceptionHandler(CompraRechazadaException.class)
    public ResponseEntity<Map<String, Object>> compraRechazada(CompraRechazadaException e) {
        return respuesta(HttpStatus.CONFLICT, e.getMessage());                       // 409
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<Map<String, Object>> conflictoVersion(ObjectOptimisticLockingFailureException e) {
        return respuesta(HttpStatus.CONFLICT,
                "Otro proceso modificó el registro al mismo tiempo. Intenta de nuevo");   // 409
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> integridad(DataIntegrityViolationException e) {
        return respuesta(HttpStatus.CONFLICT,
                "La operación viola la integridad de los datos (por ejemplo, eliminar un producto que está en carritos)");
    }

    @ExceptionHandler(ServicioExternoException.class)
    public ResponseEntity<Map<String, Object>> servicioExterno(ServicioExternoException e) {
        return respuesta(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());            // 503
    }

    private ResponseEntity<Map<String, Object>> respuesta(HttpStatus status, String mensaje) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("fecha", LocalDateTime.now().toString());
        cuerpo.put("status", status.value());
        cuerpo.put("error", status.getReasonPhrase());
        cuerpo.put("mensaje", mensaje);
        return ResponseEntity.status(status).body(cuerpo);
    }
}