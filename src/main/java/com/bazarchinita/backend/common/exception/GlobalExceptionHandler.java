package com.bazarchinita.backend.common.exception;

import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.dao.DataAccessException;

import com.bazarchinita.backend.common.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<Object>> manejarResponseStatusException(ResponseStatusException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());

        String mensaje = ex.getReason() != null
                ? ex.getReason()
                : "Ocurrió un error en la solicitud";

        return ResponseEntity
                .status(status)
                .body(ApiResponse.error(mensaje));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> manejarValidaciones(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(mensaje));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> manejarErroresBaseDatos(DataIntegrityViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("No se pudo completar la operación por una restricción de la base de datos"));
    }

    @ExceptionHandler(DataAccessException.class)
        public ResponseEntity<ApiResponse<Object>> manejarErroresPostgresql(DataAccessException ex) {
        String mensaje = "Error al procesar la operación en la base de datos";

        if (ex.getMostSpecificCause() != null && ex.getMostSpecificCause().getMessage() != null) {
                mensaje = ex.getMostSpecificCause().getMessage();

                if (mensaje.startsWith("ERROR: ")) {
                mensaje = mensaje.replaceFirst("ERROR: ", "");
                }

                int posicionWhere = mensaje.indexOf("Where:");
                if (posicionWhere != -1) {
                mensaje = mensaje.substring(0, posicionWhere).trim();
                }
        }

    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(mensaje));
}

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> manejarErroresGenerales(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Ocurrió un error interno en el servidor"));
    }
}
