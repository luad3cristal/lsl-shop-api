package com.lslshop.auth.mapper;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.lslshop.auth.dto.request.SignupRequest;
import com.lslshop.auth.dto.response.UserResponse;
import com.lslshop.auth.models.Role;
import com.lslshop.auth.models.User;

public class UserMapper {

  public static User createNewUser(SignupRequest request, PasswordEncoder enconder) {
    User newUser = new User();
    newUser.setName(request.name());
    newUser.setEmail(request.email());
    newUser.setPassword(enconder.encode(request.password()));
    newUser.setRole(Role.USER);
    newUser.setCreatedAt(LocalDateTime.now());
    newUser.setActivate(true);
    return newUser;
  }

  public static UserResponse toResponse (User user) {
    return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.isActive(), user.getCreatedAt());
  }

}
