package com.lslshop.gateway.security;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;

@Component
public class JwtUtil {
  @Value("${jwt.secret}")
  private String secretKey;

  private Key key;

  @PostConstruct
  public void init() {
    this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token);

      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private Claims getClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public String getEmailFromToken(String token) {
    return getClaims(token).getSubject();
  }

  public Long getUserIdFromToken(String token) {
    return getClaims(token).get("userId", Long.class);
  }

  public String getRoleFromToken(String token) {
    return getClaims(token).get("role", String.class);
  }
}
