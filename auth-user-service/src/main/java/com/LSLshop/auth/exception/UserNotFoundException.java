package com.lslshop.auth.exception;

import com.lslshop.auth.exception.template.BusinessException;

public class UserNotFoundException extends BusinessException {

  public UserNotFoundException(String email) {
    super("User not found with email: " + email, "USER_NOT_FOUND", 404);
  }

  public UserNotFoundException(Long id) {
    super("User not found with ID: " + id, "USER_NOT_FOUND", 404);
  }
}
