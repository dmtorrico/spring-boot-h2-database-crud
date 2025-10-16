package com.bezkoder.spring.jpa.h2.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bezkoder.spring.jpa.h2.dto.UserResponse;
import com.bezkoder.spring.jpa.h2.exception.ResourceNotFoundException;
import com.bezkoder.spring.jpa.h2.model.AppUser;
import com.bezkoder.spring.jpa.h2.repository.AppUserRepository;

@Service
public class UserService {

  private final AppUserRepository userRepository;

  public UserService(AppUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  public UserResponse getUserById(Long id) {
    AppUser user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User %d not found".formatted(id)));
    return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getFullName());
  }
}
