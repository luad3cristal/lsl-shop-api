package com.lslshop.auth.exception;

import com.lslshop.auth.exception.template.BusinessException;

public class UnauthorizedException extends BusinessException {

  public UnauthorizedException() {
    super("Access denied. Authentication required.", "UNAUTHORIZED", 403);
  }

  public UnauthorizedException(String message) {
    super(message, "UNAUTHORIZED", 403);
  }

}
