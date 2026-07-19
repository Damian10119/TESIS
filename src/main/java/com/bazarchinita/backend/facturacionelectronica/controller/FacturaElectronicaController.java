package com.bazarchinita.backend.facturacionelectronica.controller;

import java.util.List;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.bazarchinita.backend.common.response.ApiResponse;
import com.bazarchinita.backend.facturacionelectronica.dto.ActualizarEstadoFacturaRequest;
import com.bazarchinita.backend.facturacionelectronica.dto.FacturaElectronicaResponse;
import com.bazarchinita.backend.facturacionelectronica.service.FacturaElectronicaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/facturas-electronicas")
public class FacturaElectronicaController {

    private final FacturaElectronicaService facturaElectronicaService;

    public FacturaElectronicaController(FacturaElectronicaService facturaElectronicaService) {
        this.facturaElectronicaService = facturaElectronicaService;
    }

    @GetMapping
    public ApiResponse<List<FacturaElectronicaResponse>> listar() {
        return ApiResponse.success(
                "Facturas electrónicas consultadas correctamente",
                facturaElectronicaService.listar()
        );
    }

    @GetMapping("/{idFactura}")
    public ApiResponse<FacturaElectronicaResponse> buscarPorId(
            @PathVariable Integer idFactura
    ) {
        return ApiResponse.success(
                "Factura electrónica consultada correctamente",
                facturaElectronicaService.buscarPorId(idFactura)
        );
    }

    @GetMapping("/venta/{idVenta}")
    public ApiResponse<FacturaElectronicaResponse> buscarPorVenta(
            @PathVariable Integer idVenta
    ) {
        return ApiResponse.success(
                "Factura electrónica de la venta consultada correctamente",
                facturaElectronicaService.buscarPorVenta(idVenta)
        );
    }

    @GetMapping("/comprobante/{idComprobante}")
    public ApiResponse<FacturaElectronicaResponse> buscarPorComprobante(
            @PathVariable Integer idComprobante
    ) {
        return ApiResponse.success(
                "Factura electrónica del comprobante consultada correctamente",
                facturaElectronicaService.buscarPorComprobante(idComprobante)
        );
    }

    @GetMapping("/buscar")
        public ApiResponse<List<FacturaElectronicaResponse>> buscarFacturas(
                @RequestParam(required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                LocalDate fechaInicio,

                @RequestParam(required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                LocalDate fechaFin,

                @RequestParam(required = false)
                String estadoSri,

                @RequestParam(required = false)
                String identificacionCliente,

                @RequestParam(required = false)
                String numeroComprobante,

                @RequestParam(required = false)
                String claveAcceso,

                @RequestParam(required = false)
                String numeroAutorizacion
        ) {
        return ApiResponse.success(
                "Facturas electrónicas filtradas correctamente",
                facturaElectronicaService.buscarFacturas(
                        fechaInicio,
                        fechaFin,
                        estadoSri,
                        identificacionCliente,
                        numeroComprobante,
                        claveAcceso,
                        numeroAutorizacion
                )
        );
        }

    @PostMapping("/generar/venta/{idVenta}")
    public ApiResponse<FacturaElectronicaResponse> generarDesdeVenta(
            @PathVariable Integer idVenta
    ) {
        return ApiResponse.success(
                "Factura electrónica generada correctamente",
                facturaElectronicaService.generarDesdeVenta(idVenta)
        );
    }

    @PatchMapping("/{idFactura}/marcar-recibida")
    public ApiResponse<FacturaElectronicaResponse> marcarRecibida(
            @PathVariable Integer idFactura,
            @Valid @RequestBody ActualizarEstadoFacturaRequest request
    ) {
        return ApiResponse.success(
                "Factura marcada como recibida correctamente",
                facturaElectronicaService.marcarRecibida(idFactura, request)
        );
    }

    @PatchMapping("/{idFactura}/marcar-autorizada")
    public ApiResponse<FacturaElectronicaResponse> marcarAutorizada(
            @PathVariable Integer idFactura,
            @Valid @RequestBody ActualizarEstadoFacturaRequest request
    ) {
        return ApiResponse.success(
                "Factura marcada como autorizada correctamente",
                facturaElectronicaService.marcarAutorizada(idFactura, request)
        );
    }

    @PatchMapping("/{idFactura}/marcar-rechazada")
    public ApiResponse<FacturaElectronicaResponse> marcarRechazada(
            @PathVariable Integer idFactura,
            @Valid @RequestBody ActualizarEstadoFacturaRequest request
    ) {
        return ApiResponse.success(
                "Factura marcada como rechazada correctamente",
                facturaElectronicaService.marcarRechazada(idFactura, request)
        );
    }
}