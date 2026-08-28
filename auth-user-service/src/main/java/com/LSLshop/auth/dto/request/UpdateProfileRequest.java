package com.lslshop.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
  @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters") 
  String name,

  @Email(message = "Email should be valid") 
  String email,

  @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters") 
  String password

) {

}
