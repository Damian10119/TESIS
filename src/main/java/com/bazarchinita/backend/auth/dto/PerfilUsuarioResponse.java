package com.bazarchinita.backend.auth.dto;

public record PerfilUsuarioResponse(
        Integer idUsuario,
        String nombreUsuario,
        String nombres,
        String correo,
        Integer idRol,
        String nombreRol,
        Boolean estado
) {
}