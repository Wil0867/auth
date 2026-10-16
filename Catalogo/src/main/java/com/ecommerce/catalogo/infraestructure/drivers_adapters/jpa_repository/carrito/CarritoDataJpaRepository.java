package com.ecommerce.catalogo.infraestructure.drivers_adapters.jpa_repository.carrito;

import com.ecommerce.catalogo.domain.model.EstadoCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CarritoDataJpaRepository extends JpaRepository<CarritoData, Long> {

    /**
     * Obtiene el carrito más reciente del usuario cuyo estado no sea COMPRADO.
     */
    Optional<CarritoData> findFirstByIdUsuarioAndEstadoNotOrderByIdCarritoDesc(
            Long idUsuario,
            EstadoCarrito estado
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE CarritoData c SET c.estado = :nuevo WHERE c.idCarrito = :idCarrito AND c.estado = :actual")
    int cambiarEstado(@Param("idCarrito") Long idCarrito,
                      @Param("actual") EstadoCarrito actual,
                      @Param("nuevo") EstadoCarrito nuevo);
}