package com.bazarchinita.backend.common.exception;

import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import com.bazarchinita.backend.common.response.ApiResponse;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<Object>> manejarResponseStatusException(
            ResponseStatusException ex
    ) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());

        String mensaje = ex.getReason() != null
                ? ex.getReason()
                : "Ocurrió un error en la solicitud";

        return ResponseEntity
                .status(status)
                .body(ApiResponse.error(mensaje));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> manejarValidaciones(
            MethodArgumentNotValidException ex
    ) {
        String mensaje = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatearErrorCampo)
                .collect(Collectors.joining("; "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(mensaje));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> manejarConstraintViolation(
            ConstraintViolationException ex
    ) {
        String mensaje = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining("; "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(mensaje));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> manejarTipoParametroInvalido(
            MethodArgumentTypeMismatchException ex
    ) {
        String nombreParametro = ex.getName();

        String mensaje = "El parámetro '" + nombreParametro + "' tiene un formato inválido";

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(mensaje));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Object>> manejarParametroFaltante(
            MissingServletRequestParameterException ex
    ) {
        String mensaje = "El parámetro '" + ex.getParameterName() + "' es obligatorio";

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(mensaje));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> manejarJsonInvalido(
            HttpMessageNotReadableException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("El cuerpo de la petición no tiene un formato JSON válido"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> manejarMetodoNoPermitido(
            HttpRequestMethodNotSupportedException ex
    ) {
        String mensaje = "Método HTTP no permitido para esta ruta: " + ex.getMethod();

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.error(mensaje));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> manejarRestriccionesBD(
            DataIntegrityViolationException ex
    ) {
        String mensaje = extraerMensajeBaseDatos(ex);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(mensaje));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<Object>> manejarErroresBD(
            DataAccessException ex
    ) {
        String mensaje = extraerMensajeBaseDatos(ex);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(mensaje));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> manejarNoAutenticado(
            AuthenticationException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("No autenticado. Debe iniciar sesión para acceder a este recurso"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> manejarAccesoDenegado(
            AccessDeniedException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error("No tiene permisos para acceder a este recurso"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> manejarErrorGeneral(
            Exception ex
    ) {
        ex.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Ocurrió un error interno en el servidor"));
    }

    private String formatearErrorCampo(FieldError error) {
        return error.getField() + ": " + error.getDefaultMessage();
    }

    private String extraerMensajeBaseDatos(Throwable ex) {
        Throwable causa = ex;

        while (causa.getCause() != null) {
            causa = causa.getCause();
        }

        String mensaje = causa.getMessage();

        if (mensaje == null || mensaje.isBlank()) {
            return "Ocurrió un error al procesar la información en la base de datos";
        }

        return limpiarMensajeBaseDatos(mensaje);
    }

    private String limpiarMensajeBaseDatos(String mensaje) {
        String mensajeLimpio = mensaje;

        if (mensajeLimpio.contains("ERROR:")) {
            mensajeLimpio = mensajeLimpio.substring(mensajeLimpio.indexOf("ERROR:") + 6);
        }

        if (mensajeLimpio.contains("Where:")) {
            mensajeLimpio = mensajeLimpio.substring(0, mensajeLimpio.indexOf("Where:"));
        }

        if (mensajeLimpio.contains("Detail:")) {
            mensajeLimpio = mensajeLimpio.substring(0, mensajeLimpio.indexOf("Detail:"));
        }

        return mensajeLimpio
                .replace("\n", " ")
                .replace("\r", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
}
