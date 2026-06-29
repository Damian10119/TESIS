package com.bazarchinita.backend.configuracionnegocio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ConfiguracionNegocioRequest {

    @NotBlank(message = "El nombre del negocio es obligatorio")
    @Size(max = 150, message = "El nombre del negocio no debe superar los 150 caracteres")
    private String nombreNegocio;

    @Size(max = 13, message = "El RUC no debe superar los 13 caracteres")
    private String ruc;

    @Size(max = 200, message = "La dirección no debe superar los 200 caracteres")
    private String direccion;

    @Size(max = 20, message = "El teléfono no debe superar los 20 caracteres")
    private String telefono;

    @Email(message = "El correo no tiene un formato válido")
    @Size(max = 100, message = "El correo no debe superar los 100 caracteres")
    private String correo;

    @Size(max = 20, message = "El ambiente de facturación no debe superar los 20 caracteres")
    private String ambienteFacturacion;

    private Boolean obligadoContabilidad;

    public ConfiguracionNegocioRequest() {
    }

    public String getNombreNegocio() {
        return nombreNegocio;
    }

    public void setNombreNegocio(String nombreNegocio) {
        this.nombreNegocio = nombreNegocio;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getAmbienteFacturacion() {
        return ambienteFacturacion;
    }

    public void setAmbienteFacturacion(String ambienteFacturacion) {
        this.ambienteFacturacion = ambienteFacturacion;
    }

    public Boolean getObligadoContabilidad() {
        return obligadoContabilidad;
    }

    public void setObligadoContabilidad(Boolean obligadoContabilidad) {
        this.obligadoContabilidad = obligadoContabilidad;
    }
}
