package com.bazarchinita.backend.configuracionnegocio.dto;

public class ConfiguracionNegocioResponse {

    private Integer idConfiguracion;
    private String nombreNegocio;
    private String ruc;
    private String direccion;
    private String telefono;
    private String correo;
    private String ambienteFacturacion;
    private Boolean obligadoContabilidad;
    private Boolean estado;

    public ConfiguracionNegocioResponse() {
    }

    public ConfiguracionNegocioResponse(
            Integer idConfiguracion,
            String nombreNegocio,
            String ruc,
            String direccion,
            String telefono,
            String correo,
            String ambienteFacturacion,
            Boolean obligadoContabilidad,
            Boolean estado
    ) {
        this.idConfiguracion = idConfiguracion;
        this.nombreNegocio = nombreNegocio;
        this.ruc = ruc;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.ambienteFacturacion = ambienteFacturacion;
        this.obligadoContabilidad = obligadoContabilidad;
        this.estado = estado;
    }

    public Integer getIdConfiguracion() {
        return idConfiguracion;
    }

    public void setIdConfiguracion(Integer idConfiguracion) {
        this.idConfiguracion = idConfiguracion;
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

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
