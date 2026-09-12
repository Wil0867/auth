package com.ecommerce.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter


public class Usuario {

    private Long idUsuario;
    private String nombre;
    private String correo;
    private String clave;
    private String rol;
    private Integer edad;
    private String numeroTelefonico;

}
