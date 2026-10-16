package com.ecommerce.catalogo.infraestructure.entry_points;

import com.ecommerce.catalogo.domain.model.Producto;
import com.ecommerce.catalogo.domain.usecase.ProductoUseCase;
import com.ecommerce.catalogo.infraestructure.entry_points.dto.ProductoRequest;
import com.ecommerce.catalogo.infraestructure.mapper.MapperProducto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/ecommerce/producto")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoUseCase productoUseCase;
    private final MapperProducto mapperProducto;

    @PostMapping("/crear")
    public ResponseEntity<Producto> crearProducto(@RequestBody ProductoRequest request) {
        Producto producto = mapperProducto.toProducto(request);
        Producto creado = productoUseCase.guardarProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping("/lista")
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(productoUseCase.obtenerLista());
    }

    @GetMapping("/buscar/{idProducto}")
    public ResponseEntity<Producto> buscarProducto(@PathVariable Long idProducto) {
        return ResponseEntity.ok(productoUseCase.buscarProducto(idProducto));
    }

    @PutMapping("/actualizar/{idProducto}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long idProducto,
                                                       @RequestBody ProductoRequest request) {
        Producto producto = mapperProducto.toProducto(request);
        Producto actualizado = productoUseCase.modificarProducto(idProducto, producto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/eliminar/{idProducto}")
    public ResponseEntity<String> eliminarProducto(@PathVariable Long idProducto) {
        productoUseCase.eliminarProducto(idProducto);
        return ResponseEntity.ok("Producto eliminado exitosamente");
    }
}