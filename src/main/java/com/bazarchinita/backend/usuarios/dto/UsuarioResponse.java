package com.bazarchinita.backend.usuarios.dto;

import java.time.LocalDateTime;

public class UsuarioResponse {

    private Integer idUsuario;
    private Integer idRol;
    private String nombreRol;
    private String nombreUsuario;
    private String nombres;
    private String correo;
    private Boolean estado;
    private LocalDateTime fechaCreacion;

    public UsuarioResponse() {}

    public UsuarioResponse(
            Integer idUsuario,
            Integer idRol,
            String nombreRol,
            String nombreUsuario,
            String nombres,
            String correo,
            Boolean estado,
            LocalDateTime fechaCreacion
    ) {
        this.idUsuario = idUsuario;
        this.idRol = idRol;
        this.nombreRol = nombreRol;
        this.nombreUsuario = nombreUsuario;
        this.nombres = nombres;
        this.correo = correo;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public Integer getIdRol() { return idRol; }
    public void setIdRol(Integer idRol) { this.idRol = idRol; }

    public String getNombreRol() { return nombreRol; }
    public void setNombreRol(String nombreRol) { this.nombreRol = nombreRol; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}