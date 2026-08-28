package com.lslshop.auth.exception.template;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {

  private final String errorCode;
  private final String message;
  private final int statusCode;
  private final LocalDateTime timestamp;
  private final Map<String, String> validationErrors;

  public ErrorResponse(String errorCode, String message, int statusCode) {
    this.errorCode = errorCode;
    this.message = message;
    this.statusCode = statusCode;
    this.timestamp = LocalDateTime.now();
    this.validationErrors = null;
  }

  public ErrorResponse(String errorCode, String message, int statusCode, Map<String, String> validationErrors) {
    this.errorCode = errorCode;
    this.message = message;
    this.statusCode = statusCode;
    this.timestamp = LocalDateTime.now();
    this.validationErrors = validationErrors;
  }

  public String getErrorCode() {
    return this.errorCode;
  }

  public String getMessage() {
    return this.message;
  }

  public int getStatusCode() {
    return this.statusCode;
  }

  public LocalDateTime getTimestamp() {
    return this.timestamp;
  }

  public Map<String, String> validationErrors() {
    return this.validationErrors;
  }

}
