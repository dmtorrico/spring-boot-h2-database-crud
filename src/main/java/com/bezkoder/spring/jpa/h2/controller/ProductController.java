package com.bezkoder.spring.jpa.h2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.jpa.h2.dto.PageResponse;
import com.bezkoder.spring.jpa.h2.dto.ProductResponse;
import com.bezkoder.spring.jpa.h2.service.ProductService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public ResponseEntity<PageResponse<ProductResponse>> getProducts(
      @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be greater than or equal to 0") int page,
      @RequestParam(defaultValue = "10") @Min(value = 1, message = "size must be greater than or equal to 1") @Max(value = 50, message = "size must be less than or equal to 50") int size) {
    return ResponseEntity.ok(productService.getProducts(page, size));
  }
}
