package com.bezkoder.spring.jpa.h2.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.bezkoder.spring.jpa.h2.dto.ProductCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class AdminProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void createProductWithoutAuthenticationIsUnauthorized() throws Exception {
    ProductCreateRequest request = new ProductCreateRequest();
    request.setName("Secured Product");
    request.setPrice(BigDecimal.valueOf(15.0));

    mockMvc.perform(post("/api/admin/products")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void createProductWithAdminRoleSucceeds() throws Exception {
    ProductCreateRequest request = new ProductCreateRequest();
    request.setName("Admin Product");
    request.setPrice(BigDecimal.valueOf(20.5));

    mockMvc.perform(post("/api/admin/products")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(roles = "USER")
  void createProductWithUserRoleForbidden() throws Exception {
    ProductCreateRequest request = new ProductCreateRequest();
    request.setName("Forbidden Product");
    request.setPrice(BigDecimal.valueOf(5.0));

    mockMvc.perform(post("/api/admin/products")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isForbidden());
  }
}
