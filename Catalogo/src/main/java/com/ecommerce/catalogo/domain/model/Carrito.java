package com.ecommerce.catalogo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Carrito {
    private Long idCarrito;
    private Long idUsuario;          // Solo el id: el usuario vive en Auth (sin FK física)
    private EstadoCarrito estado;
    private List<ItemCarrito> items = new ArrayList<>();
    private BigDecimal total;
}