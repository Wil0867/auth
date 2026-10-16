package com.ecommerce.catalogo.domain.model.gateway;

import com.ecommerce.catalogo.domain.model.Carrito;

import java.util.Optional;

public interface CarritoGateway {

        /** Carrito actual del usuario (ACTIVO o PROCESANDO). Los COMPRADOS no cuentan. */
        Optional<Carrito> buscarActivoPorUsuario(Long idUsuario);

        Optional<Carrito> buscarPorId(Long idCarrito);

        /** Crea el carrito o guarda sus ítems y total. Solo modifica carritos ACTIVOS. */
        Carrito guardar(Carrito carrito);

        /** Pasa el carrito de ACTIVO a PROCESANDO de forma atómica. Devuelve false si otro proceso ya lo tomó. */
        boolean bloquearParaCompra(Long idCarrito);

        /** Devuelve el carrito de PROCESANDO a ACTIVO cuando la compra falla. */
        void liberarCarrito(Long idCarrito);

        /** En una sola transacción: descuenta el stock de cada producto y marca el carrito como COMPRADO. */
        Carrito confirmarCompra(Carrito carrito);
    }

