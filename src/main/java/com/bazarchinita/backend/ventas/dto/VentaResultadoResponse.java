package com.bazarchinita.backend.ventas.dto;

import java.math.BigDecimal;

public class VentaResultadoResponse {

    private Integer idVentaGenerada;
    private String numeroComprobanteGenerado;
    private BigDecimal totalVenta;
    private BigDecimal totalPagado;

    public VentaResultadoResponse() {
    }

    public VentaResultadoResponse(
            Integer idVentaGenerada,
            String numeroComprobanteGenerado,
            BigDecimal totalVenta,
            BigDecimal totalPagado
    ) {
        this.idVentaGenerada = idVentaGenerada;
        this.numeroComprobanteGenerado = numeroComprobanteGenerado;
        this.totalVenta = totalVenta;
        this.totalPagado = totalPagado;
    }

    public Integer getIdVentaGenerada() {
        return idVentaGenerada;
    }

    public void setIdVentaGenerada(Integer idVentaGenerada) {
        this.idVentaGenerada = idVentaGenerada;
    }

    public String getNumeroComprobanteGenerado() {
        return numeroComprobanteGenerado;
    }

    public void setNumeroComprobanteGenerado(String numeroComprobanteGenerado) {
        this.numeroComprobanteGenerado = numeroComprobanteGenerado;
    }

    public BigDecimal getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(BigDecimal totalVenta) {
        this.totalVenta = totalVenta;
    }

    public BigDecimal getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(BigDecimal totalPagado) {
        this.totalPagado = totalPagado;
    }
}