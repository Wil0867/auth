package com.ecommerce.catalogo.domain.usecase;

import com.ecommerce.catalogo.domain.model.Producto;
import com.ecommerce.catalogo.domain.model.gateway.ProductoGateway;
import com.ecommerce.catalogo.domain.Excepciones.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;


import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

@RequiredArgsConstructor

public class ProductoUseCase {
    private final ProductoGateway productoGateway;

    public Producto guardarProducto(Producto producto)
    {

        Producto productoAValidar = Optional.ofNullable(producto)
                .orElseThrow(() -> new IllegalArgumentException("El objeto producto no puede ser nulo"));
        Optional.ofNullable(productoAValidar.getNombre())
                .filter(nombre -> !nombre.trim().isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("El nombre del producto es obligatorio"));
        Optional.ofNullable(productoAValidar.getPrecio())
                .filter(precio -> precio.compareTo(BigDecimal.ZERO) > 0)
                .orElseThrow(() -> new IllegalArgumentException("El precio es obligatorio y debe ser mayor a cero"));
        Optional.ofNullable(productoAValidar.getStock())
                .filter(stock -> stock >= 0)
                .orElseThrow(() -> new IllegalArgumentException("El stock es obligatorio y no puede ser negativo"));
        Optional.ofNullable(productoAValidar.getDescripcion())
                .filter(descripcion -> !descripcion.trim().isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("La descripción es obligatoria"));
        productoAValidar.setIdProducto(null);

        return productoGateway.crear(productoAValidar);

    }
    public Producto buscarProducto(Long idProducto)
    {
        Long idValidado = Optional.ofNullable(idProducto)
                .filter(id -> id > 0)
                .orElseThrow(() -> new IllegalArgumentException("El id del producto debe ser mayor a cero"));

        return productoGateway.buscar(idValidado)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró un producto con el id: " + idValidado));
    }

    public Producto modificarProducto(Long idProducto, Producto producto)
    {
        buscarProducto(idProducto);

        Producto productoAValidar = Optional.ofNullable(producto)
                .orElseThrow(() -> new IllegalArgumentException("El objeto producto no puede ser nulo"));
        Optional.ofNullable(productoAValidar.getNombre())
                .filter(nombre -> !nombre.trim().isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("El nombre del producto es obligatorio"));
        Optional.ofNullable(productoAValidar.getPrecio())
                .filter(precio -> precio.compareTo(BigDecimal.ZERO) > 0)
                .orElseThrow(() -> new IllegalArgumentException("El precio es obligatorio y debe ser mayor a cero"));
        Optional.ofNullable(productoAValidar.getStock())
                .filter(stock -> stock >= 0)
                .orElseThrow(() -> new IllegalArgumentException("El stock es obligatorio y no puede ser negativo"));
        Optional.ofNullable(productoAValidar.getDescripcion())
                .filter(descripcion -> !descripcion.trim().isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("La descripción es obligatoria"));

        productoAValidar.setIdProducto(idProducto);

        return productoGateway.actualizar(productoAValidar);
    }
    public List<Producto> obtenerLista()
    {
        return productoGateway.listar();
    }
    public void eliminarProducto(Long idProducto)
    {
        Long idValidado = Optional.ofNullable(idProducto)
                .filter(id -> id > 0)
                .orElseThrow(() -> new IllegalArgumentException("El id del producto debe ser mayor a cero"));
        Optional.ofNullable(productoGateway.existe(idValidado))
                .filter(existe -> existe)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un producto con el id: " + idValidado));
        productoGateway.eliminarPorId(idValidado);
    }
}