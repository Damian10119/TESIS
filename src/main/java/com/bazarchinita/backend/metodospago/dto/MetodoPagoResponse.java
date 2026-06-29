package com.bazarchinita.backend.metodospago.dto;

public class MetodoPagoResponse {

    private Integer idMetodoPago;
    private String nombreMetodo;
    private String descripcion;
    private Boolean requiereReferencia;
    private Boolean estado;

    public MetodoPagoResponse() {
    }

    public MetodoPagoResponse(
            Integer idMetodoPago,
            String nombreMetodo,
            String descripcion,
            Boolean requiereReferencia,
            Boolean estado
    ) {
        this.idMetodoPago = idMetodoPago;
        this.nombreMetodo = nombreMetodo;
        this.descripcion = descripcion;
        this.requiereReferencia = requiereReferencia;
        this.estado = estado;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getRequiereReferencia() {
        return requiereReferencia;
    }

    public void setRequiereReferencia(Boolean requiereReferencia) {
        this.requiereReferencia = requiereReferencia;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}