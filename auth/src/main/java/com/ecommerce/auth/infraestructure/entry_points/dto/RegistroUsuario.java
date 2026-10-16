package com.ecommerce.auth.infraestructure.entry_points.dto;
import lombok.Data;

@Data
public class RegistroUsuario {
        private String nombre;
        private String correo;
        private String clave;
        private String rol;
        private String numeroTelefonico;
        private Integer edad;

}
