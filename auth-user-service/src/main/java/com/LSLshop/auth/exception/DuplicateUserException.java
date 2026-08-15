package com.lslshop.auth.exception;

import com.lslshop.auth.exception.template.BusinessException;

public class DuplicateUserException extends BusinessException {

  public DuplicateUserException(String email) {
    super("User already registered with email: " + email, "DUPLICATE_USER", 409);
  }

}
