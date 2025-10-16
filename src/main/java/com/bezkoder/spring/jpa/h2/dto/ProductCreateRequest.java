package com.bezkoder.spring.jpa.h2.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductCreateRequest {

  @NotBlank(message = "name is required")
  private String name;

  @NotNull(message = "price is required")
  @DecimalMin(value = "0.0", inclusive = false, message = "price must be greater than zero")
  private BigDecimal price;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }
}
