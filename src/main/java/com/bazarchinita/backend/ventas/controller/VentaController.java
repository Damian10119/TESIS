package com.bazarchinita.backend.ventas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.bazarchinita.backend.common.response.ApiResponse;
import com.bazarchinita.backend.ventas.dto.AnularVentaRequest;
import com.bazarchinita.backend.ventas.dto.AnularVentaResponse;
import com.bazarchinita.backend.ventas.dto.DetalleVentaConsultaResponse;
import com.bazarchinita.backend.ventas.dto.VentaConsultaResponse;
import com.bazarchinita.backend.ventas.dto.VentaRequest;
import com.bazarchinita.backend.ventas.dto.VentaResultadoResponse;
import com.bazarchinita.backend.ventas.service.VentaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping
    public ApiResponse<VentaResultadoResponse> registrarVenta(
            @Valid @RequestBody VentaRequest request
    ) {
        VentaResultadoResponse response = ventaService.registrarVenta(request);
        return ApiResponse.success("Venta registrada correctamente", response);
    }

    @GetMapping
    public ApiResponse<List<VentaConsultaResponse>> listarVentas() {
        return ApiResponse.success(
                "Ventas consultadas correctamente",
                ventaService.listarVentas()
        );
    }

    @GetMapping("/{id}/detalle")
    public ApiResponse<List<DetalleVentaConsultaResponse>> obtenerDetalleVenta(
            @PathVariable Integer id
    ) {
        return ApiResponse.success(
                "Detalle de venta consultado correctamente",
                ventaService.obtenerDetalleVenta(id)
        );
    }

    @PatchMapping("/{id}/anular")
    public ApiResponse<AnularVentaResponse> anularVenta(
            @PathVariable Integer id,
            @Valid @RequestBody AnularVentaRequest request
    ) {
        AnularVentaResponse response = ventaService.anularVenta(id, request);
        return ApiResponse.success("Venta anulada correctamente", response);
    }
}