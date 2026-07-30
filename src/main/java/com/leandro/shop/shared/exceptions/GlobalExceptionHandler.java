package com.leandro.shop.shared.exceptions;

import com.leandro.shop.shared.payload.AppResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.security.access.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<AppResponse<Void>> handleApiException(ApiException ex){
        AppResponse<Void> response = AppResponse.error(ex.getMessage());
        return ResponseEntity
                .status(ex.getStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AppResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        // Buscamos el primer error de validación y armamos un mensaje limpio
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        // Usamos tu clase ApiResponse
        AppResponse<Void> response = AppResponse.error(errorMessage);

        // Devolvemos un 400 Bad Request
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<AppResponse<Void>> handleMalformedJson(HttpMessageNotReadableException ex) {

        AppResponse<Void> response = AppResponse.error("Malformed JSON request. Please check your syntax.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<AppResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(AppResponse.error("You do not have permission to access or modify this resource"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppResponse<Void>> handleGenericException(Exception ex){
        AppResponse<Void> response = AppResponse.error("An unexpected error occurred. Please try again later: "+ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

}
