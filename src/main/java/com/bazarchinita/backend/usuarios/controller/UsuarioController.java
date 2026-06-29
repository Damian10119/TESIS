package com.bazarchinita.backend.usuarios.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.bazarchinita.backend.usuarios.dto.UsuarioRequest;
import com.bazarchinita.backend.usuarios.dto.UsuarioResponse;
import com.bazarchinita.backend.usuarios.dto.UsuarioUpdateRequest;
import com.bazarchinita.backend.usuarios.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponse> listarActivos() {
        return usuarioService.listarActivos();
    }

    @GetMapping("/todos")
    public List<UsuarioResponse> listarTodos() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscarPorId(@PathVariable Integer id) {
        return usuarioService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse crear(@Valid @RequestBody UsuarioRequest request) {
        return usuarioService.crear(request);
    }

    @PutMapping("/{id}")
    public UsuarioResponse actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioUpdateRequest request
    ) {
        return usuarioService.actualizar(id, request);
    }

    @PatchMapping("/{id}/desactivar")
    public UsuarioResponse desactivar(@PathVariable Integer id) {
        return usuarioService.desactivar(id);
    }
}
