package com.lslshop.auth.exception.template;

public class BusinessException extends RuntimeException {

  private final String errorCode;
  private final int statusCode;

  public BusinessException(String message) {
    super(message);
    this.errorCode = "BUSINESS_ERROR";
    this.statusCode = 400;
  }

  public BusinessException(String message, String errorCode, int statusCode) {
    super(message);
    this.errorCode = errorCode;
    this.statusCode = statusCode;
  }

  public String getErrorCode() {
    return this.errorCode;
  }

  public int getStatusCode() {
    return this.statusCode;
  }

}
