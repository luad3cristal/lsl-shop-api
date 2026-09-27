package com.lslshop.gateway.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.lslshop.gateway.security.JwtUtil;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
  private final JwtUtil jwtUtil;

  public JwtAuthenticationFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    String path = request.getURI().getPath();

    if (path.startsWith("/auth/"))
      return chain.filter(exchange);

    String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (authHeader == null || !authHeader.startsWith("Bearer "))
      return onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);

    String token = authHeader.substring(7);
    if (!jwtUtil.validateToken(token))
      return onError(exchange, "Invalid or expired token", HttpStatus.UNAUTHORIZED);

    Long userId = jwtUtil.getUserIdFromToken(token);
    String email = jwtUtil.getEmailFromToken(token);
    String role = jwtUtil.getRoleFromToken(token);

    ServerHttpRequest modifiedRequest = request.mutate()
        .header("X-User-Id", String.valueOf(userId))
        .header("X-User-Email", email)
        .header("X-User-Role", role)
        .build();

    return chain.filter(exchange.mutate().request(modifiedRequest).build());
  }

  public Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(status);
    response.getHeaders().add("Content-Type", "application/json");

    String body = "{\"error\":\"" + message + "\",\"status\":" + status.value() + "}";

    return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
  }

  @Override
  public int getOrder() {
    return -1;
  }

}
