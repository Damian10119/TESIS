package com.bazarchinita.backend.roles.dto;

public class RolResponse {

    private Integer idRol;
    private String nombreRol;
    private String descripcion;
    private Boolean estado;

    public RolResponse() {}

    public RolResponse(Integer idRol, String nombreRol, String descripcion, Boolean estado) {
        this.idRol = idRol;
        this.nombreRol = nombreRol;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public Integer getIdRol() { return idRol; }
    public void setIdRol(Integer idRol) { this.idRol = idRol; }

    public String getNombreRol() { return nombreRol; }
    public void setNombreRol(String nombreRol) { this.nombreRol = nombreRol; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
}
