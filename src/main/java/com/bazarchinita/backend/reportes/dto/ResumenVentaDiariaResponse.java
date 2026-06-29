package com.bazarchinita.backend.reportes.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ResumenVentaDiariaResponse(
        LocalDate fecha,
        Integer cantidadVentas,
        BigDecimal subtotalVendido,
        BigDecimal descuentoTotal,
        BigDecimal ivaTotal,
        BigDecimal totalVendido
) {
}
