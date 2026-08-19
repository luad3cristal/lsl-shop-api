package com.lslshop.auth.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lslshop.auth.dto.request.UpdateProfileRequest;
import com.lslshop.auth.dto.response.UserResponse;
import com.lslshop.auth.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/me")
  public ResponseEntity<UserResponse> getCurrentUser() {
    UserResponse response = userService.getCurrentUser();
    return ResponseEntity.ok(response);
  }

  @PutMapping("/me")
  public ResponseEntity<UserResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
    UserResponse response = userService.updateProfile(request);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/me")
  public ResponseEntity<Void> deactivateCurrentUser() {
    userService.deactivateCurrentUser();
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}/exists")
  public ResponseEntity<Boolean> userExists(@PathVariable Long id) {
    boolean exists = userService.existsById(id);
    return ResponseEntity.ok(exists);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
    UserResponse response = userService.getUserResponseById(id);
    return ResponseEntity.ok(response);
  }

  // Endpoints para o admin
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/admin/all")
  public ResponseEntity<List<UserResponse>> getAllActivateUsers() {
    List<UserResponse> users = userService.getAllActiveUsers();
    return ResponseEntity.ok(users);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/admin/{id}/activate")
  public ResponseEntity<Void> activateUser(@PathVariable Long id) {
    userService.activateUser(id);
    return ResponseEntity.ok().build();
  }
  
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/admin/{id}/deactivate")
  public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
    userService.deactivateUser(id);
    return ResponseEntity.ok().build();
  }

}
