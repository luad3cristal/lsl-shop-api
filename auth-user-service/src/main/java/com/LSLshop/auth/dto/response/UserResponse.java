package com.lslshop.auth.dto.response;

import java.time.LocalDateTime;

import com.lslshop.auth.models.Role;

public record UserResponse(
  Long id,
  String name,
  String email,
  Role role,
  boolean active, 
  LocalDateTime createdAt

) {
}
