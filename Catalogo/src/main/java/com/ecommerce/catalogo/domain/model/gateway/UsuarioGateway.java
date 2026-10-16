package com.ecommerce.catalogo.domain.model.gateway;

import java.math.BigDecimal;
import java.util.Optional;

public interface UsuarioGateway {
    /** Saldo del usuario, o vacío si el usuario no existe. */
    Optional<BigDecimal> consultarSaldo(Long idUsuario);

    void descontarSaldo(Long idUsuario, BigDecimal monto);

    /** Compensación: devuelve el dinero si la compra falla después de haber cobrado. */
    void reembolsarSaldo(Long idUsuario, BigDecimal monto);
}
