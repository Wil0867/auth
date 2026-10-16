package com.ecommerce.catalogo.infraestructure.mapper;

import com.ecommerce.catalogo.domain.model.Producto;
import com.ecommerce.catalogo.infraestructure.drivers_adapters.jpa_repository.producto.ProductoData;
import com.ecommerce.catalogo.infraestructure.entry_points.dto.ProductoRequest;
import org.springframework.stereotype.Component;

@Component
public class MapperProducto {

    public Producto toProducto(ProductoData productoData)   {
        if (productoData == null) return null;
    return new Producto (

            productoData.getIdProducto(),
            productoData.getNombre(),
            productoData.getDescripcion(),
            productoData.getPrecio(),
            productoData.getStock(),
            productoData.getImagenUrl()

    );
    }

    public ProductoData toProductoData(Producto producto) {
        if (producto == null) return null;
        return new ProductoData(
                null,
                producto.getIdProducto(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getImagenUrl()


        );
    }

    public Producto toProducto(ProductoRequest request) {
        if (request == null) return null;
        return new Producto(
                null,
                request.getNombre(),
                request.getDescripcion(),
                request.getPrecio(),
                request.getStock(),
                request.getImagenUrl()
        );
    }
}
