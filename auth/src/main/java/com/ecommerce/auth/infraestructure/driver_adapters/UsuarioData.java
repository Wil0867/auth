package com.ecommerce.auth.infraestructure.driver_adapters;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table

public class UsuarioData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idUsuario;
    private String nombre;
    @Column(length = 20, nullable = false, unique = true)
    private String correo;
    @Column(length = 12, nullable = false)
    private String clave;
    private String rol;
    private String numeroTelefonico;
    private Integer edad;

}