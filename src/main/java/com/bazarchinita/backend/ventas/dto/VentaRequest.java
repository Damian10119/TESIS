package com.bazarchinita.backend.ventas.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class VentaRequest {

    @NotNull(message = "El cliente es obligatorio")
    private Integer idCliente;

    @NotEmpty(message = "La venta debe tener al menos un producto")
    @Valid
    private List<VentaDetalleRequest> detalles;

    @NotEmpty(message = "La venta debe tener al menos un pago")
    @Valid
    private List<VentaPagoRequest> pagos;

    @Size(max = 30, message = "El tipo de comprobante no debe superar los 30 caracteres")
    private String tipoComprobante;

    @Size(max = 250, message = "La observación no debe superar los 250 caracteres")
    private String observacion;

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public List<VentaDetalleRequest> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<VentaDetalleRequest> detalles) {
        this.detalles = detalles;
    }

    public List<VentaPagoRequest> getPagos() {
        return pagos;
    }

    public void setPagos(List<VentaPagoRequest> pagos) {
        this.pagos = pagos;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
