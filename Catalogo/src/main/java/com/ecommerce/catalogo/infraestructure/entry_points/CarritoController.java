package com.ecommerce.catalogo.infraestructure.entry_points;

import com.ecommerce.catalogo.domain.model.Carrito;
import com.ecommerce.catalogo.domain.usecase.CarritoUseCase;
import com.ecommerce.catalogo.infraestructure.entry_points.dto.AgregarItemRequest;
import com.ecommerce.catalogo.infraestructure.entry_points.dto.CantidadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Sin try/catch: los errores los traduce ManejadorGlobalExcepciones a 400 / 404 / 409 / 503.
 */
@RestController
@RequestMapping("api/ecommerce/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoUseCase carritoUseCase;

    // Asignar carrito a un usuario (si ya tiene uno activo, devuelve ese)
    @PostMapping("/usuario/{idUsuario}")
    public ResponseEntity<Carrito> asignarCarrito(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(carritoUseCase.asignarCarrito(idUsuario));
    }

    // Consultar el carrito activo con subtotales y total
    @GetMapping("/usuario/consulta/{idUsuario}")
    public ResponseEntity<Carrito> consultarCarrito(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(carritoUseCase.consultarCarrito(idUsuario));
    }

    // Consultar cualquier carrito por id (sirve para ver uno ya COMPRADO)
    @GetMapping("/consultaID/{idCarrito}")
    public ResponseEntity<Carrito> consultarPorId(@PathVariable Long idCarrito) {
        return ResponseEntity.ok(carritoUseCase.consultarCarritoPorId(idCarrito));
    }

    // Agregar producto (si ya estaba, suma la cantidad)
    @PostMapping("/usuario/{idUsuario}/items")
    public ResponseEntity<Carrito> agregarProducto(@PathVariable Long idUsuario,
                                                   @RequestBody AgregarItemRequest request) {
        return ResponseEntity.ok(carritoUseCase.agregarProducto(
                idUsuario, request.getIdProducto(), request.getCantidad()));
    }

    // Cambiar la cantidad de un producto del carrito
    @PutMapping("/usuario/{idUsuario}/items/{idProducto}")
    public ResponseEntity<Carrito> actualizarCantidad(@PathVariable Long idUsuario,
                                                      @PathVariable Long idProducto,
                                                      @RequestBody CantidadRequest request) {
        return ResponseEntity.ok(carritoUseCase.actualizarCantidad(idUsuario, idProducto, request.getCantidad()));
    }

    // Retirar un producto del carrito
    @DeleteMapping("/usuario/{idUsuario}/items/{idProducto}")
    public ResponseEntity<Carrito> retirarProducto(@PathVariable Long idUsuario,
                                                   @PathVariable Long idProducto) {
        return ResponseEntity.ok(carritoUseCase.retirarProducto(idUsuario, idProducto));
    }

    // Ejecutar la compra
    @PostMapping("/usuario/{idUsuario}/comprar")
    public ResponseEntity<Carrito> comprar(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(carritoUseCase.comprar(idUsuario));
    }
}