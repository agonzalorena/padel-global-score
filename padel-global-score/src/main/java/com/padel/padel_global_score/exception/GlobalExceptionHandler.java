package com.padel.padel_global_score.exception;

import com.padel.padel_global_score.presentation.dto.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //default exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Internal Server Error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(500)
                .body(new ErrorResponse(500, "Internal Server Error"));
    }

    // Url no encontrada (desactivar en application.yaml respuesta automatica de spring)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return ResponseEntity.status(404)
                .body(new ErrorResponse(404, "Not Found: " + ex.getRequestURL()));
    }

    // Maneja errores de tipo de parametro, ejemplo: /players?id=abc y se espera un Long
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.info("Parameter type mismatch: {}", ex.getMessage());
        return ResponseEntity.status(400)
                .body(new ErrorResponse(400, "Bad Request: Parameter type mismatch"));
    }

    // Maneja parametros faltantes en la solicitud,ejemplo: /matches?teamA=1 y no mandar teamA=
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.info("Missing request parameter: {}", ex.getMessage());
        return ResponseEntity.status(400)
                .body(new ErrorResponse(400, "Bad Request: Missing parameter " + ex.getParameterName()));
    }

    // Maneja JSON mal formado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex) {
        log.warn("JSON inválido recibido: {}", ex.getMessage());
        return ResponseEntity.status(400)
                .body(new ErrorResponse(400, "JSON inválido o mal formado"));
    }

    // Maneja errores de validacion de argumentos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.info("Validation failed: {}", ex.getMessage());
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");
        return ResponseEntity.status(400)
                .body(new ErrorResponse(400, "Bad Request: " + errorMessage));
    }

    // Maneja metodo HTTP no soportado, ej: POST en un endpoint que solo soporta PUT
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.info("Method not allowed: {}", ex.getMessage());
        return ResponseEntity.status(405)
                .body(new ErrorResponse(405, "Method Not Allowed: " + ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.info("Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(404)
                .body(new ErrorResponse(404, "Not Found: " + ex.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        log.info("Bad request: {}", ex.getMessage());
        return ResponseEntity.status(400)
                .body(new ErrorResponse(400, "Bad Request: " + ex.getMessage()));
    }

    // Manejo de excepcion para credenciales incorrectas
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        log.warn("Bad credentials attempt: {}", ex.getMessage());
        return ResponseEntity.status(401)
                .body(new ErrorResponse(401, "Usuario o contraseña incorrectos"));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex) {
        log.warn("Forbidden access attempt: {}", ex.getMessage());
        return ResponseEntity.status(403)
                .body(new ErrorResponse(403, "Acceso prohibido: " + ex.getMessage()));
    }

}
