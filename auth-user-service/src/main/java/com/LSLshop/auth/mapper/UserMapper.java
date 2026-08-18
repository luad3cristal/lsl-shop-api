package com.lslshop.auth.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.lslshop.auth.dto.request.SignupRequest;
import com.lslshop.auth.models.User;

public class UserMapper {

  public static User createNewUser(SignupRequest request, PasswordEncoder enconder) {
    User newUser = new User();
    newUser.setName(request.name());
    newUser.setEmail(request.email());
    newUser.setPassword(enconder.encode(request.password()));
    newUser.setRole();
    newUser.setCreatedAt();
    return newUser;
  }
}
