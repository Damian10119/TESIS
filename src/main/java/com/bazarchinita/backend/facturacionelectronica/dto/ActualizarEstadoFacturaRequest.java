package com.bazarchinita.backend.facturacionelectronica.dto;

import jakarta.validation.constraints.Size;

public class ActualizarEstadoFacturaRequest {

    @Size(max = 60, message = "El número de autorización no debe superar los 60 caracteres")
    private String numeroAutorizacion;

    private String mensajeSri;

    @Size(max = 255, message = "La ruta XML no debe superar los 255 caracteres")
    private String rutaXml;

    @Size(max = 255, message = "La ruta PDF no debe superar los 255 caracteres")
    private String rutaPdf;

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public String getMensajeSri() {
        return mensajeSri;
    }

    public void setMensajeSri(String mensajeSri) {
        this.mensajeSri = mensajeSri;
    }

    public String getRutaXml() {
        return rutaXml;
    }

    public void setRutaXml(String rutaXml) {
        this.rutaXml = rutaXml;
    }

    public String getRutaPdf() {
        return rutaPdf;
    }

    public void setRutaPdf(String rutaPdf) {
        this.rutaPdf = rutaPdf;
    }
}