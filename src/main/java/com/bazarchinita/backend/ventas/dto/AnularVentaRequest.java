package com.bazarchinita.backend.ventas.dto;

import jakarta.validation.constraints.Size;

public class AnularVentaRequest {

    @Size(max = 250, message = "El motivo no debe superar los 250 caracteres")
    private String motivo;

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}