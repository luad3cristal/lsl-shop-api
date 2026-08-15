package com.lslshop.auth.exception;

import com.lslshop.auth.exception.template.BusinessException;

public class InvalidTokenException extends BusinessException {

  public InvalidTokenException() {
    super("Invalid or expired token", "INVALID_TOKEN", 401);
  }

  public InvalidTokenException(String message) {
    super(message, "INVALID_TOKEN", 401);
  }

}
