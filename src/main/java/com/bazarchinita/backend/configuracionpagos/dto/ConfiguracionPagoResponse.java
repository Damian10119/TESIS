package com.bazarchinita.backend.configuracionpagos.dto;

public class ConfiguracionPagoResponse {

    private Integer idConfiguracionPago;
    private Integer idMetodoPago;
    private String nombreMetodo;
    private Boolean requiereReferencia;
    private String nombreCuenta;
    private String entidad;
    private String numeroCuenta;
    private String urlQr;
    private String instrucciones;
    private Boolean estado;

    public ConfiguracionPagoResponse() {
    }

    public ConfiguracionPagoResponse(
            Integer idConfiguracionPago,
            Integer idMetodoPago,
            String nombreMetodo,
            Boolean requiereReferencia,
            String nombreCuenta,
            String entidad,
            String numeroCuenta,
            String urlQr,
            String instrucciones,
            Boolean estado
    ) {
        this.idConfiguracionPago = idConfiguracionPago;
        this.idMetodoPago = idMetodoPago;
        this.nombreMetodo = nombreMetodo;
        this.requiereReferencia = requiereReferencia;
        this.nombreCuenta = nombreCuenta;
        this.entidad = entidad;
        this.numeroCuenta = numeroCuenta;
        this.urlQr = urlQr;
        this.instrucciones = instrucciones;
        this.estado = estado;
    }

    public Integer getIdConfiguracionPago() {
        return idConfiguracionPago;
    }

    public void setIdConfiguracionPago(Integer idConfiguracionPago) {
        this.idConfiguracionPago = idConfiguracionPago;
    }

    public Integer getIdMetodoPago() {
        return idMetodoPago;
    }

    public void setIdMetodoPago(Integer idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    public String getNombreMetodo() {
        return nombreMetodo;
    }

    public void setNombreMetodo(String nombreMetodo) {
        this.nombreMetodo = nombreMetodo;
    }

    public Boolean getRequiereReferencia() {
        return requiereReferencia;
    }

    public void setRequiereReferencia(Boolean requiereReferencia) {
        this.requiereReferencia = requiereReferencia;
    }

    public String getNombreCuenta() {
        return nombreCuenta;
    }

    public void setNombreCuenta(String nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getUrlQr() {
        return urlQr;
    }

    public void setUrlQr(String urlQr) {
        this.urlQr = urlQr;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
