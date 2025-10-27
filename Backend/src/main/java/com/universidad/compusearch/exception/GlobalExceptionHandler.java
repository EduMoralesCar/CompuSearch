package com.universidad.compusearch.exception;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.universidad.compusearch.dto.ErrorResponse;

/**
 * Maneja de forma global las excepciones en los controladores REST de la aplicación.
 *
 * Esta clase intercepta excepciones lanzadas en los endpoints y devuelve
 * respuestas consistentes en formato {@link ErrorResponse}.
 *
 * Métodos principales:
 * <ul>
 *     <li>{@link #handleCustomException(CustomException)}: Maneja excepciones personalizadas que extienden {@link CustomException}.</li>
 *     <li>{@link #handleGeneral(Exception)}: Captura cualquier excepción no controlada y devuelve un error 500.</li>
 *     <li>{@link #handleValidation(MethodArgumentNotValidException)}: Captura errores de validación de Spring (@Valid) y devuelve detalles de los campos inválidos.</li>
 * </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones personalizadas de tipo {@link CustomException}.
     *
     * @param ex La excepción personalizada capturada
     * @return ResponseEntity con la información del error y el código HTTP correspondiente
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        String message = StringUtils.defaultIfBlank(ex.getMessage(), "Ha ocurrido un error");
        ErrorResponse response = new ErrorResponse(
                false,
                message,
                ex.getStatus(),
                Map.of("code", ex.getCode()));

        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getStatus()));
    }

    /**
     * Maneja cualquier excepción general no controlada.
     *
     * @param ex La excepción capturada
     * @return ResponseEntity con mensaje genérico de error interno del servidor
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        ErrorResponse response = new ErrorResponse(
                false,
                "Error interno del servidor",
                500,
                Map.of("code", "INTERNAL_SERVER_ERROR"));

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Maneja errores de validación de los campos de entrada (@Valid).
     *
     * @param ex La excepción de validación capturada
     * @return ResponseEntity con detalles de los campos que no pasaron la validación
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponse response = new ErrorResponse(
                false,
                "Error de validación",
                400,
                Map.of("details", message));

        return ResponseEntity.badRequest().body(response);
    }
}
