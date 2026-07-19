package com.bazarchinita.backend.inventario.dto;

import java.time.LocalDateTime;

public record MovimientoInventarioResponse(
        Integer idMovimiento,
        Integer idProducto,
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
