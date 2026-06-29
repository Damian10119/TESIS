package com.bazarchinita.backend.configuracionnegocio.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.bazarchinita.backend.configuracionnegocio.dto.ConfiguracionNegocioRequest;
import com.bazarchinita.backend.configuracionnegocio.dto.ConfiguracionNegocioResponse;
import com.bazarchinita.backend.configuracionnegocio.service.ConfiguracionNegocioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/configuracion-negocio")
public class ConfiguracionNegocioController {

    private final ConfiguracionNegocioService configuracionNegocioService;

    public ConfiguracionNegocioController(ConfiguracionNegocioService configuracionNegocioService) {
        this.configuracionNegocioService = configuracionNegocioService;
    }

    @GetMapping
    public ConfiguracionNegocioResponse obtenerActual() {
        return configuracionNegocioService.obtenerActual();
    }

    @GetMapping("/activas")
    public List<ConfiguracionNegocioResponse> listarActivas() {
        return configuracionNegocioService.listarActivas();
    }

    @GetMapping("/todas")
    public List<ConfiguracionNegocioResponse> listarTodas() {
        return configuracionNegocioService.listarTodas();
    }

    @GetMapping("/{id}")
    public ConfiguracionNegocioResponse buscarPorId(@PathVariable Integer id) {
        return configuracionNegocioService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ConfiguracionNegocioResponse actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ConfiguracionNegocioRequest request
    ) {
        return configuracionNegocioService.actualizar(id, request);
    }
}