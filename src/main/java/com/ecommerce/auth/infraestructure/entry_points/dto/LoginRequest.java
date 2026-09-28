package com.ecommerce.auth.infraestructure.entry_points.dto;
    import lombok.Data;

    @Data
    public class LoginRequest {

        private String correo;
        private String clave;
    }

