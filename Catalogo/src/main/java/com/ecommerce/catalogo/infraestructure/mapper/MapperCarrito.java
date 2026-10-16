package com.ecommerce.catalogo.infraestructure.mapper;

import com.ecommerce.catalogo.domain.model.Carrito;
import com.ecommerce.catalogo.domain.model.ItemCarrito;
import com.ecommerce.catalogo.infraestructure.drivers_adapters.jpa_repository.carrito.CarritoData;
import com.ecommerce.catalogo.infraestructure.drivers_adapters.jpa_repository.itemcarrito.ItemCarritoData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Solo convierte de Data a dominio. La conversión de dominio a Data la hace el adaptador,
 * porque necesita los registros existentes de la base de datos (ver CarritoDataGatewayImpl).
 */
@Component
public class MapperCarrito {

    public Carrito toCarrito(CarritoData carritoData) {
        if (carritoData == null) return null;

        List<ItemCarrito> items = new ArrayList<>();
        if (carritoData.getItems() != null) {
            for (ItemCarritoData itemData : carritoData.getItems()) {
                items.add(toItemCarrito(itemData));
            }
        }

        return new Carrito(
                carritoData.getIdCarrito(),
                carritoData.getIdUsuario(),
                carritoData.getEstado(),
                items,
                carritoData.getTotal()
        );
    }

    public ItemCarrito toItemCarrito(ItemCarritoData itemData) {
        if (itemData == null) return null;
        return new ItemCarrito(
                itemData.getIdItem(),
                itemData.getProducto() != null ? itemData.getProducto().getIdProducto() : null,
                itemData.getProducto() != null ? itemData.getProducto().getNombre() : null,
                itemData.getCantidad(),
                itemData.getPrecioUnitario(),
                itemData.getSubtotal()
        );
    }
}