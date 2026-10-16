package com.ecommerce.auth.infraestructure.driver_adapters;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table (name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Data


public class UsuarioData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;
    private String nombre;
    @Column(length = 255, nullable = false, unique = true)
    private String correo;
    @Column(length = 255, nullable = false)
    private String clave;
    private String rol;
    @Column(length = 10, unique = true)
    private String numeroTelefonico;
    private Integer edad;
    private BigDecimal saldo;

}