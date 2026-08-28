package com.lslshop.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lslshop.auth.dto.request.LoginRequest;
import com.lslshop.auth.dto.request.SignupRequest;
import com.lslshop.auth.dto.response.LoginResponse;
import com.lslshop.auth.exception.DuplicateUserException;
import com.lslshop.auth.exception.InvalidCredentialsException;
import com.lslshop.auth.exception.UserNotFoundException;
import com.lslshop.auth.mapper.UserMapper;
import com.lslshop.auth.models.User;
import com.lslshop.auth.repository.UserRepository;
import com.lslshop.auth.security.JwtProvider;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;
  private final AuthenticationManager authenticationManager;

  @Transactional
  public LoginResponse signup(SignupRequest request) {
    if (userRepository.existsByEmail(request.email()))
      throw new DuplicateUserException(request.email());

    User newUser = UserMapper.createNewUser(request, passwordEncoder);
    User savedUser = userRepository.save(newUser);

    String token = jwtProvider.generateTokenFromUserId(
        savedUser.getId(),
        savedUser.getEmail(),
        savedUser.getRole().name());

    return new LoginResponse(
        token,
        savedUser.getId(),
        savedUser.getName(),
        savedUser.getEmail(),
        savedUser.getRole());

  }

  public LoginResponse login(LoginRequest request) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

      User user = userRepository.findByEmailAndActiveTrue(request.email())
          .orElseThrow(() -> new UserNotFoundException(request.email()));

      String token = jwtProvider.generateTokenFromUserId(
          user.getId(),
          user.getEmail(),
          user.getRole().name());

      return new LoginResponse(
          token,
          user.getId(),
          user.getName(),
          user.getEmail(),
          user.getRole());

    } catch (AuthenticationException exception) {
      throw new InvalidCredentialsException();
    }
  }

  public boolean validateUserExists(Long userId) {
    return userRepository.existsByIdAndActiveTrue(userId);
  }

  public User getAuthenticatedUser(String email) {
    return userRepository.findByEmailAndActiveTrue(email).orElseThrow(() -> new UserNotFoundException(email));
  }

}
