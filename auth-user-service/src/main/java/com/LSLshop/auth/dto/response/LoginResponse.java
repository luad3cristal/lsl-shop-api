package com.lslshop.auth.dto.response;

import com.lslshop.auth.models.Role;

public record LoginResponse(
  String token,
  Long userId,
  String name,
  String email,
  Role role

) {
}
