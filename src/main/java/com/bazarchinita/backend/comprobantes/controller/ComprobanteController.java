package com.bazarchinita.backend.comprobantes.controller;

import java.util.List;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bazarchinita.backend.common.response.ApiResponse;
import com.bazarchinita.backend.comprobantes.dto.ComprobanteResponse;
import com.bazarchinita.backend.comprobantes.service.ComprobanteService;

@RestController
@RequestMapping("/api/comprobantes")
public class ComprobanteController {

    private final ComprobanteService comprobanteService;

    public ComprobanteController(ComprobanteService comprobanteService) {
        this.comprobanteService = comprobanteService;
    }

    @GetMapping
    public ApiResponse<List<ComprobanteResponse>> listarComprobantes() {
        return ApiResponse.success(
                "Comprobantes consultados correctamente",
                comprobanteService.listarComprobantes()
        );
    }

    @GetMapping("/id/{idComprobante}")
    public ApiResponse<ComprobanteResponse> buscarPorId(
            @PathVariable Integer idComprobante
    ) {
        return ApiResponse.success(
                "Comprobante consultado correctamente",
                comprobanteService.buscarPorId(idComprobante)
        );
    }

    @GetMapping("/venta/{idVenta}")
    public ApiResponse<ComprobanteResponse> buscarPorVenta(
            @PathVariable Integer idVenta
    ) {
        return ApiResponse.success(
                "Comprobante de la venta consultado correctamente",
                comprobanteService.buscarPorVenta(idVenta)
        );
    }

    @GetMapping("/numero/{numeroComprobante}")
    public ApiResponse<ComprobanteResponse> buscarPorNumero(
            @PathVariable String numeroComprobante
    ) {
        return ApiResponse.success(
                "Comprobante consultado correctamente",
                comprobanteService.buscarPorNumero(numeroComprobante)
        );
    }

    @GetMapping("/buscar")
        public ApiResponse<List<ComprobanteResponse>> buscarComprobantes(
                @RequestParam(required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                LocalDate fechaInicio,

                @RequestParam(required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                LocalDate fechaFin,

                @RequestParam(required = false)
                String tipoComprobante,

                @RequestParam(required = false)
                String estadoComprobante,

                @RequestParam(required = false)
                String identificacionCliente,

                @RequestParam(required = false)
                String numeroComprobante
        ) {
        return ApiResponse.success(
                "Comprobantes filtrados correctamente",
                comprobanteService.buscarComprobantes(
                        fechaInicio,
                        fechaFin,
                        tipoComprobante,
                        estadoComprobante,
                        identificacionCliente,
                        numeroComprobante
                )
        );
        }
}