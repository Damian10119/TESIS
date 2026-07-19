package com.bazarchinita.backend.auth.controller;

import org.springframework.web.bind.annotation.*;

import com.bazarchinita.backend.auth.dto.LoginRequest;
import com.bazarchinita.backend.auth.dto.LoginResponse;
import com.bazarchinita.backend.auth.service.AuthService;
import com.bazarchinita.backend.common.response.ApiResponse;

import jakarta.validation.Valid;

import com.bazarchinita.backend.auth.dto.CambiarContrasenaRequest;
import com.bazarchinita.backend.auth.dto.CambiarContrasenaResponse;
import com.bazarchinita.backend.auth.dto.PerfilUsuarioResponse;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ApiResponse.success("Inicio de sesión correcto", response);
    }

    @GetMapping("/perfil")
    public ApiResponse<PerfilUsuarioResponse> obtenerPerfilActual() {
        return ApiResponse.success(
                "Perfil del usuario autenticado consultado correctamente",
                authService.obtenerPerfilActual()
        );
    }

    @PatchMapping("/cambiar-contrasena")
    public ApiResponse<CambiarContrasenaResponse> cambiarContrasena(
            @Valid @RequestBody CambiarContrasenaRequest request
    ) {
        return ApiResponse.success(
                "Contraseña actualizada correctamente",
                authService.cambiarContrasena(request)
        );
}
}