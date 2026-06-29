package com.bazarchinita.backend.clientes.dto;

import java.time.LocalDateTime;

public class ClienteResponse {

    private Integer idCliente;
    private String tipoIdentificacion;
    private String identificacion;
    private String nombres;
    private String direccion;
    private String telefono;
    private String correo;
    private Boolean esConsumidorFinal;
    private Boolean estado;
    private LocalDateTime fechaCreacion;

    public ClienteResponse() {
    }

    public ClienteResponse(
            Integer idCliente,
            String tipoIdentificacion,
            String identificacion,
            String nombres,
            String direccion,
            String telefono,
            String correo,
            Boolean esConsumidorFinal,
            Boolean estado,
            LocalDateTime fechaCreacion
    ) {
        this.idCliente = idCliente;
        this.tipoIdentificacion = tipoIdentificacion;
        this.identificacion = identificacion;
        this.nombres = nombres;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.esConsumidorFinal = esConsumidorFinal;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
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

    public Boolean getEsConsumidorFinal() {
        return esConsumidorFinal;
    }

    public void setEsConsumidorFinal(Boolean esConsumidorFinal) {
        this.esConsumidorFinal = esConsumidorFinal;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
