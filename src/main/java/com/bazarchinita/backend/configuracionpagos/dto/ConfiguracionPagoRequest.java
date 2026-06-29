package com.bazarchinita.backend.configuracionpagos.dto;

import jakarta.validation.constraints.Size;

public class ConfiguracionPagoRequest {

    @Size(max = 100, message = "El nombre de la cuenta no debe superar los 100 caracteres")
    private String nombreCuenta;

    @Size(max = 100, message = "La entidad no debe superar los 100 caracteres")
    private String entidad;

    @Size(max = 50, message = "El número de cuenta no debe superar los 50 caracteres")
    private String numeroCuenta;

    @Size(max = 255, message = "La URL o ruta del QR no debe superar los 255 caracteres")
    private String urlQr;

    @Size(max = 250, message = "Las instrucciones no deben superar los 250 caracteres")
    private String instrucciones;

    private Boolean estado;

    public ConfiguracionPagoRequest() {
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
