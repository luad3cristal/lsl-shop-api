package com.lslshop.auth.exception;

import com.lslshop.auth.exception.template.BusinessException;

public class InvalidCredentialsException extends BusinessException {

  public InvalidCredentialsException() {
    super("Invalid email or password", "INVALID_CREDENTIALS", 401);
  }

  public InvalidCredentialsException(String message) {
    super(message, "INVALID_CREDENTIALS", 401);
  }

}
