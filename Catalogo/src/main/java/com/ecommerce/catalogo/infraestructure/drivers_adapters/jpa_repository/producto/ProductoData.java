package com.ecommerce.catalogo.infraestructure.drivers_adapters.jpa_repository.producto;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "productos")

public class ProductoData {
    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;
    @Column(nullable = false, length = 255)
    private String nombre;
    @Column(nullable = false, length = 1000)
    private String descripcion;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;
    @Column(nullable = false)
    private Integer stock;
    @Column(length = 2048)
    private String imagenUrl;


}