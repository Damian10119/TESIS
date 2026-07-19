package com.bazarchinita.backend.categorias.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.bazarchinita.backend.categorias.dto.CategoriaRequest;
import com.bazarchinita.backend.categorias.dto.CategoriaResponse;
import com.bazarchinita.backend.categorias.service.CategoriaService;
import com.bazarchinita.backend.common.response.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<CategoriaResponse> listarActivas() {
        return categoriaService.listarCategoriasActivas();
    }

    @GetMapping("/todas")
    public List<CategoriaResponse> listarTodas() {
        return categoriaService.listarTodas();
    }

    @GetMapping("/{id}")
    public CategoriaResponse buscarPorId(@PathVariable Integer id) {
        return categoriaService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponse crear(@Valid @RequestBody CategoriaRequest request) {
        return categoriaService.crear(request);
    }

    @PutMapping("/{id}")
    public CategoriaResponse actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody CategoriaRequest request
    ) {
        return categoriaService.actualizar(id, request);
    }

    @PatchMapping("/{id}/desactivar")
    public CategoriaResponse desactivar(@PathVariable Integer id) {
        return categoriaService.desactivar(id);
    }

    @PatchMapping("/{idCategoria}/activar")
    public ApiResponse<CategoriaResponse> activar(
            @PathVariable Integer idCategoria
    ) {
        return ApiResponse.success(
                "Categoría activada correctamente",
                categoriaService.activar(idCategoria)
        );
    }
}
