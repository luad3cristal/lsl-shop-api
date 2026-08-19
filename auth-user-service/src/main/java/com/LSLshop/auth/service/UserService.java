package com.lslshop.auth.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lslshop.auth.dto.request.UpdateProfileRequest;
import com.lslshop.auth.dto.response.UserResponse;
import com.lslshop.auth.exception.DuplicateUserException;
import com.lslshop.auth.exception.UserNotFoundException;
import com.lslshop.auth.mapper.UserMapper;
import com.lslshop.auth.models.User;
import com.lslshop.auth.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  // Usuário autenticado
  public User getCurrentUserEntity() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated())
      throw new UsernameNotFoundException("User not authenticated");

    String email = authentication.getName(); // name é o email do usuário
    return userRepository.findByEmailAndActiveTrue(email).orElseThrow(() -> new UserNotFoundException(email));
  }

  // Usuário atual como DTO de resposta
  public UserResponse getCurrentUser() {
    return UserMapper.toResponse(getCurrentUserEntity());
  }

  // Busca usuário por ID
  public User getUserById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  // Busca usuário por ID e retorna DTO de resposta
  public UserResponse getUserResponseById(Long id) {
    return UserMapper.toResponse(getUserById(id));
  }

  // Busca usuário por email
  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
  }

  // Busca usuário por email e retorna DTO de resposta
  public UserResponse getUserResponseByEmail(String email) {
    return UserMapper.toResponse(getUserByEmail(email));
  }
  
  // Soft delete do usuário
  @Transactional
  public void deactivateCurrentUser() {
    User user = getCurrentUserEntity();
    user.setActivate(false);
    userRepository.save(user);
  }

  // Lista todos os usuário ativos
  public List<UserResponse> getAllActiveUsers() {
    return userRepository.findAllByActiveTrue().stream().map(UserMapper::toResponse).collect(Collectors.toList());
  }

  // verifica se o usuário existe e está ativo
  public boolean existsById(Long userId) {
    return userRepository.existsByIdAndActiveTrue(userId);
  }

  // verifica se o email está disponível
  public boolean isEmailAvailable(String email) {
    return !userRepository.existsByEmailAndActiveTrue(email);
  }

  // Atualiza o perfil do usuário
  public UserResponse updateProfile(UpdateProfileRequest request) {
    User user = getCurrentUserEntity();

    if (request.name() != null && !request.name().isBlank())
      user.setName(request.name());

    if (request.email() != null && !request.email().isBlank() && !request.email().equals(user.getEmail())) {
      if (isEmailAvailable(request.email()))
        throw new DuplicateUserException(request.email());

      user.setEmail(request.email());
    }

    if (request.password() != null && !request.password().isBlank())
      user.setPassword(passwordEncoder.encode(request.password()));

    User updatedUser = userRepository.save(user);

    return UserMapper.toResponse(updatedUser);
  }

  // Métodos para o ADMIN
  // Desativa um usuário
  @Transactional
  public void deactivateUser(Long userId) {
    User user = getUserById(userId);
    user.setActivate(false);
    userRepository.save(user);
  }

  // Reativa um usuário
  @Transactional
  public void activateUser(Long userId) {
    User user = getUserById(userId);
    user.setActivate(true);
    userRepository.save(user);
  }

}
