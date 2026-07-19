package com.bazarchinita.backend.inventario.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.bazarchinita.backend.common.response.ApiResponse;
import com.bazarchinita.backend.inventario.dto.AjustarStockRequest;
import com.bazarchinita.backend.inventario.dto.AjustarStockResponse;
import com.bazarchinita.backend.inventario.dto.MovimientoInventarioResponse;
import com.bazarchinita.backend.inventario.service.InventarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @PostMapping("/ajustar-stock")
    public ApiResponse<AjustarStockResponse> ajustarStock(
            @Valid @RequestBody AjustarStockRequest request
    ) {
        return ApiResponse.success(
                "Stock ajustado correctamente",
                inventarioService.ajustarStock(request)
        );
    }

    @GetMapping("/movimientos")
    public ApiResponse<List<MovimientoInventarioResponse>> listarMovimientos() {
        return ApiResponse.success(
                "Movimientos de inventario consultados correctamente",
                inventarioService.listarMovimientos()
        );
    }

    @GetMapping("/movimientos/producto/{idProducto}")
    public ApiResponse<List<MovimientoInventarioResponse>> listarMovimientosPorProducto(
            @PathVariable Integer idProducto
    ) {
        return ApiResponse.success(
                "Movimientos del producto consultados correctamente",
                inventarioService.listarMovimientosPorProducto(idProducto)
        );
    }
}
