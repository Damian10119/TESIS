package com.bazarchinita.backend.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.bazarchinita.backend.common.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public SecurityExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            org.springframework.security.core.AuthenticationException authException
    ) throws IOException {
        escribirRespuesta(
                response,
                HttpStatus.UNAUTHORIZED,
                "No autenticado. Debe iniciar sesión para acceder a este recurso"
        );
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        escribirRespuesta(
                response,
                HttpStatus.FORBIDDEN,
                "No tiene permisos para acceder a este recurso"
        );
    }

    private void escribirRespuesta(
            HttpServletResponse response,
            HttpStatus status,
            String mensaje
    ) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ApiResponse<Object> body = ApiResponse.error(mensaje);

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}