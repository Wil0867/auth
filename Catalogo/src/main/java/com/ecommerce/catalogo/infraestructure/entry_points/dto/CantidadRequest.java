package com.ecommerce.catalogo.infraestructure.entry_points.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CantidadRequest {
    private Integer cantidad;
}