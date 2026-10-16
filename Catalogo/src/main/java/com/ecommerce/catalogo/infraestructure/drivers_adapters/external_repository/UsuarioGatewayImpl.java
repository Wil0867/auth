package com.ecommerce.catalogo.infraestructure.drivers_adapters.external_repository;

import com.ecommerce.catalogo.domain.Excepciones.CompraRechazadaException;
import com.ecommerce.catalogo.domain.Excepciones.ServicioExternoException;
import com.ecommerce.catalogo.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.Optional;

/** Adaptador que habla con el microservicio Auth por REST. */
@Component
@RequiredArgsConstructor
public class UsuarioGatewayImpl implements UsuarioGateway {

    private static final String RUTA_USUARIO = "/api/ecommerce/usuario/{idUsuario}";

    private final RestClient authRestClient;

    // Estructuras del JSON que se envía y recibe de Auth
    public record SaldoResponse(Long idUsuario, String nombre, BigDecimal saldo) {}
    public record MontoRequest(BigDecimal monto) {}

    @Override
    public Optional<BigDecimal> consultarSaldo(Long idUsuario) {
        try {
            SaldoResponse respuesta = authRestClient.get()
                    .uri(RUTA_USUARIO + "/saldo", idUsuario)
                    .retrieve()
                    .body(SaldoResponse.class);
            return Optional.ofNullable(respuesta).map(SaldoResponse::saldo);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty(); // El usuario no existe en Auth
        } catch (RestClientException e) {
            throw new ServicioExternoException("No fue posible consultar el saldo en Auth", e);
        }
    }

    @Override
    public void descontarSaldo(Long idUsuario, BigDecimal monto) {
        try {
            authRestClient.post()
                    .uri(RUTA_USUARIO + "/descontar", idUsuario)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new MontoRequest(monto))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            // Auth rechazó el descuento (por ejemplo, saldo insuficiente)
            throw new CompraRechazadaException("Auth rechazó el descuento: " + e.getResponseBodyAsString());
        } catch (RestClientException e) {
            throw new ServicioExternoException("No fue posible descontar el saldo en Auth", e);
        }
    }

    @Override
    public void reembolsarSaldo(Long idUsuario, BigDecimal monto) {
        try {
            // Se reutiliza la operación de recarga que ya existe en Auth
            authRestClient.post()
                    .uri(RUTA_USUARIO + "/recargar", idUsuario)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new MontoRequest(monto))
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            throw new ServicioExternoException(
                    "No fue posible reintegrar " + monto + " al usuario " + idUsuario + " en Auth", e);
        }
    }
}