package com.ecommerce.catalogo.infraestructure.drivers_adapters.jpa_repository.carrito;

import com.ecommerce.catalogo.domain.model.EstadoCarrito;
import com.ecommerce.catalogo.infraestructure.drivers_adapters.jpa_repository.itemcarrito.ItemCarritoData;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Sin @Data: la relación con los ítems haría que equals/hashCode/toString entren en ciclo
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "carritos")
public class CarritoData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarrito;

    // Solo el id del usuario: vive en la base de datos de Auth, NO hay clave foránea física
    @Column(nullable = false)
    private Long idUsuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoCarrito estado;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(nullable = false)

    // Un carrito tiene muchos ítems (1:N). Si se quita un ítem de la lista, se borra de la tabla.
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarritoData> items = new ArrayList<>();
}