package com.bezkoder.spring.jpa.h2.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class OrderRequest {

  @NotNull(message = "userId is required")
  private Long userId;

  @NotEmpty(message = "productIds must not be empty")
  private List<@NotNull(message = "productId must not be null") Long> productIds;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public List<Long> getProductIds() {
    return productIds;
  }

  public void setProductIds(List<Long> productIds) {
    this.productIds = productIds;
  }
}
