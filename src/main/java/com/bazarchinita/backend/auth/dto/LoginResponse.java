package com.bazarchinita.backend.auth.dto;

public class LoginResponse {

    private String token;
    private String tipoToken;
    private Long expiraEn;
    private Integer idUsuario;
    private String nombreUsuario;
    private String nombres;
    private Integer idRol;
    private String nombreRol;

    public LoginResponse() {
    }

    public LoginResponse(
            String token,
            String tipoToken,
            Long expiraEn,
            Integer idUsuario,
            String nombreUsuario,
            String nombres,
            Integer idRol,
            String nombreRol
    ) {
        this.token = token;
        this.tipoToken = tipoToken;
        this.expiraEn = expiraEn;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.nombres = nombres;
        this.idRol = idRol;
        this.nombreRol = nombreRol;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public void setTipoToken(String tipoToken) {
        this.tipoToken = tipoToken;
    }

    public Long getExpiraEn() {
        return expiraEn;
    }

    public void setExpiraEn(Long expiraEn) {
        this.expiraEn = expiraEn;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }
}
