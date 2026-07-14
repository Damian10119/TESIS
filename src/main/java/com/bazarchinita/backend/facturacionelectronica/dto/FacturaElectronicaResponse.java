package com.bazarchinita.backend.facturacionelectronica.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FacturaElectronicaResponse(
        Integer idFacturaElectronica,
        Integer idComprobante,
        Integer idVenta,
        String tipoComprobante,
        String numeroComprobante,
        LocalDateTime fechaEmision,
        String cliente,
        String identificacionCliente,
        BigDecimal total,
        String claveAcceso,
        String numeroAutorizacion,
        LocalDateTime fechaAutorizacion,
        String estadoSri,
        String mensajeSri,
        String rutaXml,
        String rutaPdf
) {
}