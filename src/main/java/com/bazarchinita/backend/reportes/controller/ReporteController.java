package com.bazarchinita.backend.reportes.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bazarchinita.backend.common.response.ApiResponse;
import com.bazarchinita.backend.reportes.dto.InventarioActualResponse;
import com.bazarchinita.backend.reportes.dto.MovimientoInventarioResponse;
import com.bazarchinita.backend.reportes.dto.PagoVentaResponse;
import com.bazarchinita.backend.reportes.dto.ResumenVentaDiariaResponse;
import com.bazarchinita.backend.reportes.dto.StockBajoResponse;
import com.bazarchinita.backend.reportes.service.ReporteService;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/resumen-ventas-diarias")
    public ApiResponse<List<ResumenVentaDiariaResponse>> obtenerResumenVentasDiarias() {
        return ApiResponse.success(
                "Resumen diario de ventas consultado correctamente",
                reporteService.obtenerResumenVentasDiarias()
        );
    }

    @GetMapping("/inventario-actual")
    public ApiResponse<List<InventarioActualResponse>> obtenerInventarioActual() {
        return ApiResponse.success(
                "Inventario actual consultado correctamente",
                reporteService.obtenerInventarioActual()
        );
    }

    @GetMapping("/stock-bajo")
    public ApiResponse<List<StockBajoResponse>> obtenerStockBajo() {
        return ApiResponse.success(
                "Productos con stock bajo consultados correctamente",
                reporteService.obtenerStockBajo()
        );
    }

    @GetMapping("/movimientos-inventario")
    public ApiResponse<List<MovimientoInventarioResponse>> obtenerMovimientosInventario() {
        return ApiResponse.success(
                "Movimientos de inventario consultados correctamente",
                reporteService.obtenerMovimientosInventario()
        );
    }

    @GetMapping("/pagos-ventas")
    public ApiResponse<List<PagoVentaResponse>> obtenerPagosVentas() {
        return ApiResponse.success(
                "Pagos por venta consultados correctamente",
                reporteService.obtenerPagosVentas()
        );
    }
}
