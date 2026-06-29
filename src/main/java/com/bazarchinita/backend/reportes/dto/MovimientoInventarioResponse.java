package com.bazarchinita.backend.reportes.dto;

import java.time.LocalDateTime;

public record MovimientoInventarioResponse(
        Integer idMovimiento,
        LocalDateTime fechaMovimiento,
        String codigoProducto,
        String nombreProducto,
        String nombreCategoria,
        String usuarioRegistra,
        Integer idVenta,
        String tipoMovimiento,
        Integer cantidad,
        Integer stockAnterior,
        Integer stockNuevo,
        String motivo
) {
}
