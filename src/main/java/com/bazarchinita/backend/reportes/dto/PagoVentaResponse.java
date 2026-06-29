package com.bazarchinita.backend.reportes.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagoVentaResponse(
        Integer idPago,
        Integer idVenta,
        LocalDateTime fechaVenta,
        String cliente,
        String nombreMetodo,
        BigDecimal monto,
        String referenciaPago,
        String estadoPago,
        LocalDateTime fechaPago,
        String observacion
) {
}