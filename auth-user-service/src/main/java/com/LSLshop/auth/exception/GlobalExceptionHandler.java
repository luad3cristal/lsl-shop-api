package com.lslshop.auth.exception;

import java.util.HashMap;
import java.util.Map;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lslshop.auth.exception.template.BusinessException;
import com.lslshop.auth.exception.template.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
    ErrorResponse error = new ErrorResponse(ex.getErrorCode(), ex.getMessage(), ex.getStatusCode());

    return ResponseEntity.status(ex.getStatusCode()).body(error);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
    ErrorResponse error = new ErrorResponse("USER_NOT_FOUND", ex.getMessage(), 404);

    return ResponseEntity.status(404).body(error);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
    ErrorResponse error = new ErrorResponse("INVALID_CREDENTIALS", "Invalid email or password", 401);

    return ResponseEntity.status(401).body(error);
  }

  // DTOs
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> validationErrors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();

      validationErrors.put(fieldName, errorMessage);
    });

    ErrorResponse error = new ErrorResponse("VALIDATION_ERROR", "Validation failed", 400, validationErrors);

    return ResponseEntity.badRequest().body(error);

  }

  // Qualquer outra exception
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    ErrorResponse error = new ErrorResponse("INTERNAL_ERROR", "An unexpected error ocurred: " + ex.getMessage(), 500);

    return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(error);
  }

}
