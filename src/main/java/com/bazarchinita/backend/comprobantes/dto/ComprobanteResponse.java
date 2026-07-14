package com.bazarchinita.backend.comprobantes.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ComprobanteResponse(
        Integer idComprobante,
        Integer idVenta,
        String tipoComprobante,
        String numeroComprobante,
        LocalDateTime fechaEmision,
        String cliente,
        String identificacionCliente,
        BigDecimal subtotal,
        BigDecimal iva,
        BigDecimal total,
        String estadoComprobante,
        String claveAcceso,
        String estadoSri,
        String numeroAutorizacion,
        LocalDateTime fechaAutorizacion
) {
}
