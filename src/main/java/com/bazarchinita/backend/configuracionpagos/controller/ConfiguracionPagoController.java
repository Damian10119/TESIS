package com.bazarchinita.backend.configuracionpagos.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.bazarchinita.backend.configuracionpagos.dto.ConfiguracionPagoRequest;
import com.bazarchinita.backend.configuracionpagos.dto.ConfiguracionPagoResponse;
import com.bazarchinita.backend.configuracionpagos.service.ConfiguracionPagoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/configuracion-pagos")
public class ConfiguracionPagoController {

    private final ConfiguracionPagoService configuracionPagoService;

    public ConfiguracionPagoController(ConfiguracionPagoService configuracionPagoService) {
        this.configuracionPagoService = configuracionPagoService;
    }

    @GetMapping
    public List<ConfiguracionPagoResponse> listarActivas() {
        return configuracionPagoService.listarActivas();
    }

    @GetMapping("/todas")
    public List<ConfiguracionPagoResponse> listarTodas() {
        return configuracionPagoService.listarTodas();
    }

    @GetMapping("/{id}")
    public ConfiguracionPagoResponse buscarPorId(@PathVariable Integer id) {
        return configuracionPagoService.buscarPorId(id);
    }

    @GetMapping("/metodo")
    public ConfiguracionPagoResponse buscarPorMetodo(@RequestParam String nombre) {
        return configuracionPagoService.buscarPorMetodo(nombre);
    }

    @PutMapping("/{id}")
    public ConfiguracionPagoResponse actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ConfiguracionPagoRequest request
    ) {
        return configuracionPagoService.actualizar(id, request);
    }
}