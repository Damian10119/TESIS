package com.bazarchinita.backend.ventas.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DetalleVentaConsultaResponse {

    private Integer idVenta;
    private LocalDateTime fechaVenta;
    private String cliente;
    private String usuarioRegistra;
    private String codigoProducto;
    private String nombreProducto;
    private String nombreCategoria;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuento;
    private BigDecimal subtotal;
    private BigDecimal iva;
    private BigDecimal totalLinea;
    private String estadoVenta;

    public DetalleVentaConsultaResponse() {
    }

    public DetalleVentaConsultaResponse(
            Integer idVenta,
            LocalDateTime fechaVenta,
            String cliente,
            String usuarioRegistra,
            String codigoProducto,
            String nombreProducto,
            String nombreCategoria,
            Integer cantidad,
            BigDecimal precioUnitario,
            BigDecimal descuento,
            BigDecimal subtotal,
            BigDecimal iva,
            BigDecimal totalLinea,
            String estadoVenta
    ) {
        this.idVenta = idVenta;
        this.fechaVenta = fechaVenta;
        this.cliente = cliente;
        this.usuarioRegistra = usuarioRegistra;
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.nombreCategoria = nombreCategoria;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuento = descuento;
        this.subtotal = subtotal;
        this.iva = iva;
        this.totalLinea = totalLinea;
        this.estadoVenta = estadoVenta;
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

    public String getUsuarioRegistra() {
        return usuarioRegistra;
    }

    public void setUsuarioRegistra(String usuarioRegistra) {
        this.usuarioRegistra = usuarioRegistra;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getTotalLinea() {
        return totalLinea;
    }

    public void setTotalLinea(BigDecimal totalLinea) {
        this.totalLinea = totalLinea;
    }

    public String getEstadoVenta() {
        return estadoVenta;
    }

    public void setEstadoVenta(String estadoVenta) {
        this.estadoVenta = estadoVenta;
    }
}