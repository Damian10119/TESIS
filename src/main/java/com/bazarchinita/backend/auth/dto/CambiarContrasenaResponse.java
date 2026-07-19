package com.bazarchinita.backend.auth.dto;

public record CambiarContrasenaResponse(
        Integer idUsuario,
        String nombreUsuario,
        String mensaje
) {
}