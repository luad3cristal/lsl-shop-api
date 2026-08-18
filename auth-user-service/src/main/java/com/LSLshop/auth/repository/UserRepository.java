package com.lslshop.auth.repository;

import com.lslshop.auth.models.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

  Optional<User> findByEmailAndActiveTrue(String email);

  Optional<User> findAllByActiveTrue();

  boolean existsByEmail(String email);
}
