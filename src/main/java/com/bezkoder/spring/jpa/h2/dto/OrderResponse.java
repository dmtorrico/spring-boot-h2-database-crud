package com.bezkoder.spring.jpa.h2.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class OrderResponse {

  private final Long id;
  private final String orderNumber;
  private final Long userId;
  private final List<Long> productIds;
  private final BigDecimal totalAmount;
  private final Instant createdAt;

  public OrderResponse(Long id, String orderNumber, Long userId, List<Long> productIds, BigDecimal totalAmount,
      Instant createdAt) {
    this.id = id;
    this.orderNumber = orderNumber;
    this.userId = userId;
    this.productIds = List.copyOf(productIds);
    this.totalAmount = totalAmount;
    this.createdAt = createdAt;
  }

  public Long getId() {
    return id;
  }

  public String getOrderNumber() {
    return orderNumber;
  }

  public Long getUserId() {
    return userId;
  }

  public List<Long> getProductIds() {
    return productIds;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
