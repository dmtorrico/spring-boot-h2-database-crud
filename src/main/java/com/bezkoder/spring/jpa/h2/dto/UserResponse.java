package com.bezkoder.spring.jpa.h2.dto;

public class UserResponse {

  private final Long id;
  private final String username;
  private final String email;
  private final String fullName;

  public UserResponse(Long id, String username, String email, String fullName) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.fullName = fullName;
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getFullName() {
    return fullName;
  }
}
