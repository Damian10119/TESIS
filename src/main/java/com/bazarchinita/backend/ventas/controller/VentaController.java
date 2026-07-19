package com.bazarchinita.backend.ventas.controller;

import java.util.List;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
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

    @GetMapping("/buscar")
    public ApiResponse<List<VentaConsultaResponse>> buscarVentas(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaInicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaFin,

            @RequestParam(required = false)
            String estadoVenta,

            @RequestParam(required = false)
            String tipoComprobante,

            @RequestParam(required = false)
            String identificacionCliente,

            @RequestParam(required = false)
            String numeroComprobante
    ) {
        return ApiResponse.success(
                "Ventas filtradas correctamente",
                ventaService.buscarVentas(
                        fechaInicio,
                        fechaFin,
                        estadoVenta,
                        tipoComprobante,
                        identificacionCliente,
                        numeroComprobante
                )
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