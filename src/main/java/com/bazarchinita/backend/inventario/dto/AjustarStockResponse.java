package com.bazarchinita.backend.inventario.dto;

public record AjustarStockResponse(
        Integer idProductoActualizado,
        Integer stockAnterior,
        Integer stockNuevo,
        String tipoMovimiento,
        String mensaje
) {
}