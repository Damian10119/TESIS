package com.bazarchinita.backend.metodospago.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.bazarchinita.backend.metodospago.dto.MetodoPagoResponse;
import com.bazarchinita.backend.metodospago.service.MetodoPagoService;

@RestController
@RequestMapping("/api/metodos-pago")
public class MetodoPagoController {

    private final MetodoPagoService metodoPagoService;

    public MetodoPagoController(MetodoPagoService metodoPagoService) {
        this.metodoPagoService = metodoPagoService;
    }

    @GetMapping
    public List<MetodoPagoResponse> listarActivos() {
        return metodoPagoService.listarActivos();
    }

    @GetMapping("/todos")
    public List<MetodoPagoResponse> listarTodos() {
        return metodoPagoService.listarTodos();
    }

    @GetMapping("/{id}")
    public MetodoPagoResponse buscarPorId(@PathVariable Integer id) {
        return metodoPagoService.buscarPorId(id);
    }

    @GetMapping("/nombre/{nombre}")
    public MetodoPagoResponse buscarPorNombre(@PathVariable String nombre) {
        return metodoPagoService.buscarPorNombre(nombre);
    }
}
