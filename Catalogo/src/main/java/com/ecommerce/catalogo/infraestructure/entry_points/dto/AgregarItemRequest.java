package com.ecommerce.catalogo.infraestructure.entry_points.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Sin precio: el precio lo pone el servidor a partir del producto guardado
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgregarItemRequest {
    private Long idProducto;
    private Integer cantidad;
}