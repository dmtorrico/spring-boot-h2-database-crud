package com.bezkoder.spring.jpa.h2.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.bezkoder.spring.jpa.h2.model.Product;
import com.bezkoder.spring.jpa.h2.repository.ProductRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ProductRepository productRepository;

  @BeforeEach
  void setUp() {
    productRepository.deleteAll();
    for (int i = 0; i < 3; i++) {
      Product product = new Product();
      product.setName("Product-" + i + "-" + System.nanoTime());
      product.setPrice(BigDecimal.valueOf(10 + i));
      productRepository.save(product);
    }
  }

  @Test
  void getProductsWithDefaultsReturnsAll() throws Exception {
    mockMvc.perform(get("/api/products"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page").value(0))
        .andExpect(jsonPath("$.size").value(10))
        .andExpect(jsonPath("$.totalElements").value(3))
        .andExpect(jsonPath("$.data", hasSize(3)));
  }

  @Test
  void getProductsWithPaginationReturnsPagedResult() throws Exception {
    mockMvc.perform(get("/api/products")
        .param("page", "0")
        .param("size", "2")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page").value(0))
        .andExpect(jsonPath("$.size").value(2))
        .andExpect(jsonPath("$.totalPages").value(2))
        .andExpect(jsonPath("$.data", hasSize(2)));
  }

  @Test
  void getProductsOutOfRangeReturnsEmptyData() throws Exception {
    mockMvc.perform(get("/api/products")
        .param("page", "5")
        .param("size", "2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page").value(5))
        .andExpect(jsonPath("$.data", hasSize(0)));
  }

  @Test
  void getProductsWithInvalidSizeReturnsBadRequest() throws Exception {
    mockMvc.perform(get("/api/products")
        .param("page", "0")
        .param("size", "0"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.fieldErrors", hasSize(1)));
  }
}
