package com.bazarchinita.backend.productos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.bazarchinita.backend.common.response.ApiResponse;
import com.bazarchinita.backend.productos.dto.ProductoRequest;
import com.bazarchinita.backend.productos.dto.ProductoResponse;
import com.bazarchinita.backend.productos.service.ProductoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<ProductoResponse> listarActivos() {
        return productoService.listarActivos();
    }

    @GetMapping("/todos")
    public List<ProductoResponse> listarTodos() {
        return productoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ProductoResponse buscarPorId(@PathVariable Integer id) {
        return productoService.buscarPorId(id);
    }

    @GetMapping("/codigo/{codigo}")
    public ProductoResponse buscarPorCodigo(@PathVariable String codigo) {
        return productoService.buscarPorCodigo(codigo);
    }

    @GetMapping("/buscar")
    public List<ProductoResponse> buscarPorNombre(@RequestParam String nombre) {
        return productoService.buscarPorNombre(nombre);
    }

    @GetMapping("/stock-bajo")
    public List<ProductoResponse> listarStockBajo() {
        return productoService.listarStockBajo();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoResponse crear(@Valid @RequestBody ProductoRequest request) {
        return productoService.crear(request);
    }

    @PutMapping("/{id}")
    public ProductoResponse actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ProductoRequest request
    ) {
        return productoService.actualizar(id, request);
    }

    @PatchMapping("/{id}/desactivar")
    public ProductoResponse desactivar(@PathVariable Integer id) {
        return productoService.desactivar(id);
    }
    
    @PatchMapping("/{idProducto}/activar")
    public ApiResponse<ProductoResponse> activar(
            @PathVariable Integer idProducto
    ) {
        return ApiResponse.success(
                "Producto activado correctamente",
                productoService.activar(idProducto)
        );
    }

}
