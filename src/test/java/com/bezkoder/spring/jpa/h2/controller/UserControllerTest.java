package com.bezkoder.spring.jpa.h2.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.bezkoder.spring.jpa.h2.model.AppUser;
import com.bezkoder.spring.jpa.h2.repository.AppUserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private AppUserRepository userRepository;

  @Test
  void getExistingUserReturnsDetails() throws Exception {
    AppUser user = new AppUser();
    user.setUsername("user_" + System.nanoTime());
    user.setEmail("user_" + System.nanoTime() + "@example.com");
    user.setFullName("Test User");
    AppUser saved = userRepository.save(user);

    mockMvc.perform(get("/api/users/{id}", saved.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(saved.getId()))
        .andExpect(jsonPath("$.username").value(saved.getUsername()))
        .andExpect(jsonPath("$.email").value(saved.getEmail()))
        .andExpect(jsonPath("$.fullName").value(saved.getFullName()));
  }

  @Test
  void getUnknownUserReturnsNotFound() throws Exception {
    mockMvc.perform(get("/api/users/{id}", 99999L))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("User 99999 not found"));
  }
}
