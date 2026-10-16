package com.ecommerce.catalogo.infraestructure.drivers_adapters.jpa_repository.producto;

import com.ecommerce.catalogo.domain.model.Producto;
import com.ecommerce.catalogo.domain.model.gateway.ProductoGateway;
import com.ecommerce.catalogo.infraestructure.mapper.MapperProducto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Repository
@AllArgsConstructor

public class ProductoDataGatewayImpl implements ProductoGateway {
    private final MapperProducto mapperProducto;
    private final ProductoDataJpaRepository repository;

    @Override
    public Producto crear(Producto producto)
    {
        return mapperProducto.toProducto(repository.save(mapperProducto.toProductoData(producto)));
    }

    @Override
    public Optional<Producto> buscar(Long idProducto)
    {
        return repository.findById(idProducto).map(mapperProducto::toProducto);
    }

    @Override
    public List<Producto> listar()
    {
        return repository.findAll().stream()
                .map(mapperProducto::toProducto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existe(Long idProducto)
    {
        return repository.existsById(idProducto);
    }

    @Override
    public  void eliminarPorId(Long idProducto)
    {
        repository.deleteById(idProducto);
    }

    @Override
    public Producto actualizar(Producto producto) {
        // 1. Trae el producto existente (con su versión real)
        ProductoData existente = repository.findById(producto.getIdProducto())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el producto a actualizar"));

        // 2. Copia los datos nuevos (sin tocar id ni version)
        existente.setNombre(producto.getNombre());
        existente.setDescripcion(producto.getDescripcion());
        existente.setPrecio(producto.getPrecio());
        existente.setStock(producto.getStock());
        existente.setImagenUrl(producto.getImagenUrl());

        // 3. Guarda y 4. devuelve convertido
        return mapperProducto.toProducto(repository.save(existente));
    }
}
