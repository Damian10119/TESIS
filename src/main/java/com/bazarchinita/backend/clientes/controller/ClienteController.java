package com.bazarchinita.backend.clientes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.bazarchinita.backend.clientes.dto.ClienteRequest;
import com.bazarchinita.backend.clientes.dto.ClienteResponse;
import com.bazarchinita.backend.clientes.service.ClienteService;
import com.bazarchinita.backend.common.response.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<ClienteResponse> listarActivos() {
        return clienteService.listarActivos();
    }

    @GetMapping("/todos")
    public List<ClienteResponse> listarTodos() {
        return clienteService.listarTodos();
    }

    @GetMapping("/consumidor-final")
    public ClienteResponse obtenerConsumidorFinal() {
        return clienteService.obtenerConsumidorFinal();
    }

    @GetMapping("/{id}")
    public ClienteResponse buscarPorId(@PathVariable Integer id) {
        return clienteService.buscarPorId(id);
    }

    @GetMapping("/identificacion/{identificacion}")
    public ClienteResponse buscarPorIdentificacion(@PathVariable String identificacion) {
        return clienteService.buscarPorIdentificacion(identificacion);
    }

    @GetMapping("/buscar")
    public List<ClienteResponse> buscar(@RequestParam String texto) {
        return clienteService.buscar(texto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse crear(@Valid @RequestBody ClienteRequest request) {
        return clienteService.crear(request);
    }

    @PutMapping("/{id}")
    public ClienteResponse actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ClienteRequest request
    ) {
        return clienteService.actualizar(id, request);
    }

    @PatchMapping("/{id}/desactivar")
    public ClienteResponse desactivar(@PathVariable Integer id) {
        return clienteService.desactivar(id);
    }

    @PatchMapping("/{idCliente}/activar")
    public ApiResponse<ClienteResponse> activar(
            @PathVariable Integer idCliente
    ) {
        return ApiResponse.success(
                "Cliente activado correctamente",
                clienteService.activar(idCliente)
        );
    }
}
