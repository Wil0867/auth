package com.ecommerce.catalogo.infraestructure.drivers_adapters.jpa_repository.itemcarrito;

import com.ecommerce.catalogo.infraestructure.drivers_adapters.jpa_repository.carrito.CarritoData;
import com.ecommerce.catalogo.infraestructure.drivers_adapters.jpa_repository.producto.ProductoData;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "items_carrito",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_carrito", "id_producto"}))
public class ItemCarritoData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idItem;

    // N ítems pertenecen a 1 carrito (FK id_carrito)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_carrito", nullable = false)
    private CarritoData carrito;

    // N ítems apuntan a 1 producto (FK id_producto)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductoData producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;
}