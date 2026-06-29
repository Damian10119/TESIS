package com.bazarchinita.backend.ventas.dto;

public class AnularVentaResponse {

    private Integer idVentaAnulada;
    private String estadoFinal;
    private String mensaje;

    public AnularVentaResponse() {
    }

    public AnularVentaResponse(Integer idVentaAnulada, String estadoFinal, String mensaje) {
        this.idVentaAnulada = idVentaAnulada;
        this.estadoFinal = estadoFinal;
        this.mensaje = mensaje;
    }

    public Integer getIdVentaAnulada() {
        return idVentaAnulada;
    }

    public void setIdVentaAnulada(Integer idVentaAnulada) {
        this.idVentaAnulada = idVentaAnulada;
    }

    public String getEstadoFinal() {
        return estadoFinal;
    }

    public void setEstadoFinal(String estadoFinal) {
        this.estadoFinal = estadoFinal;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
