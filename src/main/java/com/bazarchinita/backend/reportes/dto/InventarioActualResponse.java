package com.bazarchinita.backend.reportes.dto;

import java.math.BigDecimal;

public record InventarioActualResponse(
        Integer idProducto,
        String codigoProducto,
        String nombreProducto,
        String nombreCategoria,
        BigDecimal precioCompra,
        BigDecimal precioVenta,
        Integer stockActual,
        Integer stockMinimo,
        String estadoStock,
        Boolean aplicaIva,
        BigDecimal porcentajeIva,
        Boolean estado
) {
}
