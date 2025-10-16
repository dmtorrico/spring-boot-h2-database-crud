package com.bezkoder.spring.jpa.h2.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.bezkoder.spring.jpa.h2.dto.OrderRequest;
import com.bezkoder.spring.jpa.h2.model.AppUser;
import com.bezkoder.spring.jpa.h2.model.Product;
import com.bezkoder.spring.jpa.h2.repository.AppUserRepository;
import com.bezkoder.spring.jpa.h2.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private AppUserRepository userRepository;

  @Autowired
  private ProductRepository productRepository;

  @Test
  void createOrderReturnsCreatedWithLocation() throws Exception {
    AppUser user = buildUser("orderUser");
    AppUser savedUser = userRepository.save(user);

    Product firstProduct = buildProduct("First Product", BigDecimal.valueOf(9.99));
    Product secondProduct = buildProduct("Second Product", BigDecimal.valueOf(19.50));
    Product savedFirst = productRepository.save(firstProduct);
    Product savedSecond = productRepository.save(secondProduct);

    OrderRequest request = new OrderRequest();
    request.setUserId(savedUser.getId());
    request.setProductIds(List.of(savedFirst.getId(), savedSecond.getId()));

    mockMvc.perform(post("/api/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", matchesPattern(".*/api/orders/\\d+")))
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.orderNumber").isString())
        .andExpect(jsonPath("$.userId").value(savedUser.getId()))
        .andExpect(jsonPath("$.productIds").value(containsInAnyOrder(
            savedFirst.getId().intValue(), savedSecond.getId().intValue())))
        .andExpect(jsonPath("$.totalAmount").isNumber());
  }

  @Test
  void createOrderWithInvalidPayloadReturnsBadRequest() throws Exception {
    OrderRequest request = new OrderRequest();

    mockMvc.perform(post("/api/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.fieldErrors").isArray());
  }

  private AppUser buildUser(String base) {
    AppUser user = new AppUser();
    String suffix = base + "_" + System.nanoTime();
    user.setUsername(suffix);
    user.setEmail(suffix + "@example.com");
    user.setFullName("Order Test User");
    return user;
  }

  private Product buildProduct(String name, BigDecimal price) {
    Product product = new Product();
    product.setName(name + "_" + System.nanoTime());
    product.setPrice(price);
    return product;
  }
}
