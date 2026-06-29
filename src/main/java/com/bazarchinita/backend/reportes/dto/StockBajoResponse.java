package com.bazarchinita.backend.reportes.dto;

public record StockBajoResponse(
        Integer idProducto,
        String codigoProducto,
        String nombreProducto,
        String nombreCategoria,
        Integer stockActual,
        Integer stockMinimo,
        String estadoStock
) {
}
