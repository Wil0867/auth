package com.ecommerce.auth.infraestructure.entry_points.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private Long idUsuario;
    private String nombre;
    private String correo;
    private String rol;
    private Integer edad;
    private String numeroTelefonico;
}
