package com.ecommerce.catalogo.domain.model.gateway;

import com.ecommerce.catalogo.domain.model.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoGateway {
    Producto crear(Producto producto);
    Optional<Producto> buscar(Long idProducto);
    Producto actualizar(Producto producto);
    List<Producto> listar();
    boolean existe(Long idProducto);
    void eliminarPorId(Long idProducto);
}