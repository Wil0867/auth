package com.ecommerce.catalogo.domain.usecase;

import com.ecommerce.catalogo.domain.Excepciones.CompraRechazadaException;
import com.ecommerce.catalogo.domain.Excepciones.RecursoNoEncontradoException;
import com.ecommerce.catalogo.domain.model.Carrito;
import com.ecommerce.catalogo.domain.model.EstadoCarrito;
import com.ecommerce.catalogo.domain.model.ItemCarrito;
import com.ecommerce.catalogo.domain.model.Producto;
import com.ecommerce.catalogo.domain.model.gateway.CarritoGateway;
import com.ecommerce.catalogo.domain.model.gateway.ProductoGateway;
import com.ecommerce.catalogo.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@RequiredArgsConstructor
public class CarritoUseCase {

    private final CarritoGateway carritoGateway;
    private final ProductoGateway productoGateway;
    private final UsuarioGateway usuarioAuthGateway;

    // ---------------------------------------------------------------
    // Asignar / consultar
    // ---------------------------------------------------------------

    /** Devuelve el carrito actual del usuario o le crea uno nuevo. Un usuario solo tiene un carrito activo. */
    public Carrito asignarCarrito(Long idUsuario) {
        Long idValidado = validarId(idUsuario, "usuario");

        // El usuario debe existir en Auth (se consulta por API, no hay FK entre bases de datos)
        usuarioAuthGateway.consultarSaldo(idValidado)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un usuario con el id: " + idValidado));

        return carritoGateway.buscarActivoPorUsuario(idValidado)
                .orElseGet(() -> carritoGateway.guardar(nuevoCarrito(idValidado)));
    }

    public Carrito consultarCarrito(Long idUsuario) {
        Long idValidado = validarId(idUsuario, "usuario");
        return carritoGateway.buscarActivoPorUsuario(idValidado)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El usuario con id " + idValidado + " no tiene un carrito activo"));
    }

    public Carrito consultarCarritoPorId(Long idCarrito) {
        Long idValidado = validarId(idCarrito, "carrito");
        return carritoGateway.buscarPorId(idValidado)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un carrito con el id: " + idValidado));
    }

    // ---------------------------------------------------------------
    // Gestión de ítems (no cobra ni descuenta inventario)
    // ---------------------------------------------------------------

    public Carrito agregarProducto(Long idUsuario, Long idProducto, Integer cantidad) {
        validarCantidad(cantidad);
        Long idProductoValidado = validarId(idProducto, "producto");

        Carrito carrito = asignarCarrito(idUsuario);
        exigirActivo(carrito);
        Producto producto = buscarProducto(idProductoValidado);

        // Mismo producto otra vez -> se suma la cantidad, no se duplica el ítem
        Optional<ItemCarrito> existente = buscarItem(carrito, idProductoValidado);
        if (existente.isPresent()) {
            ItemCarrito item = existente.get();
            item.setCantidad(item.getCantidad() + cantidad);
            actualizarPrecioItem(item, producto);
        } else {
            ItemCarrito nuevo = new ItemCarrito(null, idProductoValidado, producto.getNombre(), cantidad, null, null);
            actualizarPrecioItem(nuevo, producto);
            carrito.getItems().add(nuevo);
        }

        recalcularTotal(carrito);
        return carritoGateway.guardar(carrito);
    }

    public Carrito actualizarCantidad(Long idUsuario, Long idProducto, Integer cantidad) {
        validarCantidad(cantidad);
        Long idProductoValidado = validarId(idProducto, "producto");

        Carrito carrito = consultarCarrito(idUsuario);
        exigirActivo(carrito);
        ItemCarrito item = buscarItem(carrito, idProductoValidado)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El producto con id " + idProductoValidado + " no está en el carrito"));

        item.setCantidad(cantidad);
        actualizarPrecioItem(item, buscarProducto(idProductoValidado));

        recalcularTotal(carrito);
        return carritoGateway.guardar(carrito);
    }

    public Carrito retirarProducto(Long idUsuario, Long idProducto) {
        Long idProductoValidado = validarId(idProducto, "producto");

        Carrito carrito = consultarCarrito(idUsuario);
        exigirActivo(carrito);

        Optional.of(carrito.getItems().removeIf(item -> item.getIdProducto().equals(idProductoValidado)))
                .filter(retirado -> retirado)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El producto con id " + idProductoValidado + " no está en el carrito"));

        recalcularTotal(carrito);
        return carritoGateway.guardar(carrito);
    }

    // ---------------------------------------------------------------
    // Compra (pasos 5 a 8 del flujo del taller)
    // ---------------------------------------------------------------

    public Carrito comprar(Long idUsuario) {
        Carrito carrito = consultarCarrito(idUsuario);
        exigirActivo(carrito);

        Optional.of(carrito.getItems())
                .filter(items -> !items.isEmpty())
                .orElseThrow(() -> new CompraRechazadaException("El carrito está vacío"));

        // 1. Validar inventario y recalcular precios con los valores actuales (nada se modifica aún)
        for (ItemCarrito item : carrito.getItems()) {
            Producto producto = productoGateway.buscar(item.getIdProducto())
                    .orElseThrow(() -> new CompraRechazadaException(
                            "El producto con id " + item.getIdProducto() + " ya no existe"));

            if (producto.getStock() < item.getCantidad()) {
                throw new CompraRechazadaException("Stock insuficiente para '" + producto.getNombre()
                        + "'. Disponible: " + producto.getStock() + ", solicitado: " + item.getCantidad());
            }
            actualizarPrecioItem(item, producto);
        }
        recalcularTotal(carrito);
        BigDecimal total = carrito.getTotal();

        // 2. Validar saldo consultando a Auth (nada se modifica aún)
        BigDecimal saldo = usuarioAuthGateway.consultarSaldo(carrito.getIdUsuario())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un usuario con el id: " + carrito.getIdUsuario()));

        if (saldo.compareTo(total) < 0) {
            throw new CompraRechazadaException("Saldo insuficiente. Saldo disponible: " + saldo
                    + ", total de la compra: " + total);
        }

        // 3. Bloquear el carrito: si llegan dos compras a la vez, solo una pasa (evita cobro doble)
        if (!carritoGateway.bloquearParaCompra(carrito.getIdCarrito())) {
            throw new CompraRechazadaException("El carrito ya fue comprado o tiene una compra en proceso");
        }

        // 4. Cobrar en Auth. Si falla, se libera el carrito y no se tocó el inventario.
        try {
            usuarioAuthGateway.descontarSaldo(carrito.getIdUsuario(), total);
        } catch (RuntimeException e) {
            carritoGateway.liberarCarrito(carrito.getIdCarrito());
            throw e;
        }

        // 5. Descontar stock y marcar COMPRADO (transacción local).
        //    Si falla, se compensa: se reintegra el saldo en Auth y se libera el carrito.
        try {
            carrito.setEstado(EstadoCarrito.COMPRADO);
            return carritoGateway.confirmarCompra(carrito);
        } catch (RuntimeException e) {
            usuarioAuthGateway.reembolsarSaldo(carrito.getIdUsuario(), total);
            carritoGateway.liberarCarrito(carrito.getIdCarrito());

            String motivo = (e instanceof CompraRechazadaException)
                    ? e.getMessage()
                    : "El inventario cambió mientras se procesaba la compra";
            throw new CompraRechazadaException(motivo + ". La compra se revirtió y el saldo fue reintegrado");
        }
    }

    // ---------------------------------------------------------------
    // Métodos de apoyo
    // ---------------------------------------------------------------

    private Long validarId(Long id, String entidad) {
        return Optional.ofNullable(id)
                .filter(valor -> valor > 0)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El id del " + entidad + " es obligatorio y debe ser mayor a cero"));
    }

    private void validarCantidad(Integer cantidad) {
        Optional.ofNullable(cantidad)
                .filter(valor -> valor > 0)
                .orElseThrow(() -> new IllegalArgumentException("La cantidad es obligatoria y debe ser mayor a cero"));
    }

    private void exigirActivo(Carrito carrito) {
        Optional.of(carrito.getEstado())
                .filter(estado -> estado == EstadoCarrito.ACTIVO)
                .orElseThrow(() -> new CompraRechazadaException(
                        "El carrito no se puede modificar porque está en estado " + carrito.getEstado()));
    }

    private Producto buscarProducto(Long idProducto) {
        return productoGateway.buscar(idProducto)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró un producto con el id: " + idProducto));
    }

    private Optional<ItemCarrito> buscarItem(Carrito carrito, Long idProducto) {
        return carrito.getItems().stream()
                .filter(item -> item.getIdProducto().equals(idProducto))
                .findFirst();
    }

    /** El precio siempre sale del producto guardado, nunca del JSON del cliente. */
    private void actualizarPrecioItem(ItemCarrito item, Producto producto) {
        item.setNombreProducto(producto.getNombre());
        item.setPrecioUnitario(producto.getPrecio());
        item.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())));
    }

    private void recalcularTotal(Carrito carrito) {
        carrito.setTotal(carrito.getItems().stream()
                .map(ItemCarrito::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private Carrito nuevoCarrito(Long idUsuario) {
        return new Carrito(null, idUsuario, EstadoCarrito.ACTIVO, new ArrayList<>(),
                BigDecimal.ZERO);
    }
}