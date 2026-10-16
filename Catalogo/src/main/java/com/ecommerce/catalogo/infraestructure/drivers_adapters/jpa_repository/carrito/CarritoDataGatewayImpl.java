package com.ecommerce.catalogo.infraestructure.drivers_adapters.jpa_repository.carrito;

import com.ecommerce.catalogo.domain.Excepciones.CompraRechazadaException;
import com.ecommerce.catalogo.domain.Excepciones.RecursoNoEncontradoException;
import com.ecommerce.catalogo.domain.model.Carrito;
import com.ecommerce.catalogo.domain.model.EstadoCarrito;
import com.ecommerce.catalogo.domain.model.ItemCarrito;
import com.ecommerce.catalogo.domain.model.gateway.CarritoGateway;
import com.ecommerce.catalogo.infraestructure.drivers_adapters.jpa_repository.itemcarrito.ItemCarritoData;
import com.ecommerce.catalogo.infraestructure.drivers_adapters.jpa_repository.producto.ProductoData;
import com.ecommerce.catalogo.infraestructure.drivers_adapters.jpa_repository.producto.ProductoDataJpaRepository;
import com.ecommerce.catalogo.infraestructure.mapper.MapperCarrito;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CarritoDataGatewayImpl implements CarritoGateway {

    private final CarritoDataJpaRepository carritoRepository;
    private final ProductoDataJpaRepository productoRepository;
    private final MapperCarrito mapperCarrito;

    @Override
    @Transactional(readOnly = true)
    public Optional<Carrito> buscarActivoPorUsuario(Long idUsuario) {
        return carritoRepository
                .findFirstByIdUsuarioAndEstadoNotOrderByIdCarritoDesc(idUsuario, EstadoCarrito.COMPRADO)
                .map(mapperCarrito::toCarrito);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Carrito> buscarPorId(Long idCarrito) {
        return carritoRepository.findById(idCarrito).map(mapperCarrito::toCarrito);
    }

    @Override
    @Transactional
    public Carrito guardar(Carrito carrito) {
        CarritoData data;

        if (carrito.getIdCarrito() == null) {
            data = new CarritoData();
            data.setIdUsuario(carrito.getIdUsuario());
            data.setEstado(EstadoCarrito.ACTIVO);
        } else {
            data = carritoRepository.findById(carrito.getIdCarrito())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe un carrito con el id: " + carrito.getIdCarrito()));

            if (data.getEstado() != EstadoCarrito.ACTIVO) {
                throw new CompraRechazadaException(
                        "El carrito no se puede modificar porque está en estado " + data.getEstado());
            }
        }

        sincronizarItems(data, carrito);
        data.setTotal(carrito.getTotal());

        return mapperCarrito.toCarrito(carritoRepository.saveAndFlush(data));
    }

    @Override
    @Transactional
    public boolean bloquearParaCompra(Long idCarrito) {
        return carritoRepository.cambiarEstado(idCarrito, EstadoCarrito.ACTIVO, EstadoCarrito.PROCESANDO) == 1;
    }

    @Override
    @Transactional
    public void liberarCarrito(Long idCarrito) {
        carritoRepository.cambiarEstado(idCarrito, EstadoCarrito.PROCESANDO, EstadoCarrito.ACTIVO);
    }

    @Override
    @Transactional
    public Carrito confirmarCompra(Carrito carrito) {
        CarritoData data = carritoRepository.findById(carrito.getIdCarrito())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un carrito con el id: " + carrito.getIdCarrito()));

        for (ItemCarrito item : carrito.getItems()) {
            ProductoData producto = productoRepository.findById(item.getIdProducto())
                    .orElseThrow(() -> new CompraRechazadaException(
                            "El producto con id " + item.getIdProducto() + " ya no existe"));

            if (producto.getStock() < item.getCantidad()) {
                throw new CompraRechazadaException("Stock insuficiente para '" + producto.getNombre() + "'");
            }

            producto.setStock(producto.getStock() - item.getCantidad());
        }

        sincronizarItems(data, carrito);
        data.setTotal(carrito.getTotal());
        data.setEstado(EstadoCarrito.COMPRADO);

        return mapperCarrito.toCarrito(carritoRepository.saveAndFlush(data));
    }

    private void sincronizarItems(CarritoData data, Carrito carrito) {
        Set<Long> idsQueSeQuedan = carrito.getItems().stream()
                .map(ItemCarrito::getIdItem)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        data.getItems().removeIf(itemData -> !idsQueSeQuedan.contains(itemData.getIdItem()));

        Map<Long, ItemCarritoData> existentes = data.getItems().stream()
                .collect(Collectors.toMap(ItemCarritoData::getIdItem, Function.identity()));

        for (ItemCarrito item : carrito.getItems()) {
            ItemCarritoData itemData = (item.getIdItem() == null) ? null : existentes.get(item.getIdItem());

            if (itemData == null) {
                itemData = new ItemCarritoData();
                itemData.setCarrito(data);
                itemData.setProducto(productoRepository.getReferenceById(item.getIdProducto()));
                data.getItems().add(itemData);
            }

            itemData.setCantidad(item.getCantidad());
            itemData.setPrecioUnitario(item.getPrecioUnitario());
            itemData.setSubtotal(item.getSubtotal());
        }
    }
}