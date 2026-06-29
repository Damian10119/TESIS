package com.bazarchinita.backend.ventas.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VentaConsultaResponse {

    private Integer idVenta;
    private LocalDateTime fechaVenta;
    private String cliente;
    private String identificacionCliente;
    private Boolean esConsumidorFinal;
    private String usuarioRegistra;
    private BigDecimal subtotal;
    private BigDecimal descuentoTotal;
    private BigDecimal ivaTotal;
    private BigDecimal total;
    private BigDecimal totalPagado;
    private String estadoVenta;
    private String tipoComprobante;
    private String numeroComprobante;
    private String estadoComprobante;

    public VentaConsultaResponse() {
    }

    public VentaConsultaResponse(
            Integer idVenta,
            LocalDateTime fechaVenta,
            String cliente,
            String identificacionCliente,
            Boolean esConsumidorFinal,
            String usuarioRegistra,
            BigDecimal subtotal,
            BigDecimal descuentoTotal,
            BigDecimal ivaTotal,
            BigDecimal total,
            BigDecimal totalPagado,
            String estadoVenta,
            String tipoComprobante,
            String numeroComprobante,
            String estadoComprobante
    ) {
        this.idVenta = idVenta;
        this.fechaVenta = fechaVenta;
        this.cliente = cliente;
        this.identificacionCliente = identificacionCliente;
        this.esConsumidorFinal = esConsumidorFinal;
        this.usuarioRegistra = usuarioRegistra;
        this.subtotal = subtotal;
        this.descuentoTotal = descuentoTotal;
        this.ivaTotal = ivaTotal;
        this.total = total;
        this.totalPagado = totalPagado;
        this.estadoVenta = estadoVenta;
        this.tipoComprobante = tipoComprobante;
        this.numeroComprobante = numeroComprobante;
        this.estadoComprobante = estadoComprobante;
    }

    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getIdentificacionCliente() {
        return identificacionCliente;
    }

    public void setIdentificacionCliente(String identificacionCliente) {
        this.identificacionCliente = identificacionCliente;
    }

    public Boolean getEsConsumidorFinal() {
        return esConsumidorFinal;
    }

    public void setEsConsumidorFinal(Boolean esConsumidorFinal) {
        this.esConsumidorFinal = esConsumidorFinal;
    }

    public String getUsuarioRegistra() {
        return usuarioRegistra;
    }

    public void setUsuarioRegistra(String usuarioRegistra) {
        this.usuarioRegistra = usuarioRegistra;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDescuentoTotal() {
        return descuentoTotal;
    }

    public void setDescuentoTotal(BigDecimal descuentoTotal) {
        this.descuentoTotal = descuentoTotal;
    }

    public BigDecimal getIvaTotal() {
        return ivaTotal;
    }

    public void setIvaTotal(BigDecimal ivaTotal) {
        this.ivaTotal = ivaTotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(BigDecimal totalPagado) {
        this.totalPagado = totalPagado;
    }

    public String getEstadoVenta() {
        return estadoVenta;
    }

    public void setEstadoVenta(String estadoVenta) {
        this.estadoVenta = estadoVenta;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public String getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(String numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public String getEstadoComprobante() {
        return estadoComprobante;
    }

    public void setEstadoComprobante(String estadoComprobante) {
        this.estadoComprobante = estadoComprobante;
    }
}