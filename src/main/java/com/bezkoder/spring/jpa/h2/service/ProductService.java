package com.bezkoder.spring.jpa.h2.service;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bezkoder.spring.jpa.h2.dto.PageResponse;
import com.bezkoder.spring.jpa.h2.dto.ProductCreateRequest;
import com.bezkoder.spring.jpa.h2.dto.ProductResponse;
import com.bezkoder.spring.jpa.h2.model.Product;
import com.bezkoder.spring.jpa.h2.repository.ProductRepository;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Transactional(readOnly = true)
  public PageResponse<ProductResponse> getProducts(int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
    Page<Product> products = productRepository.findAll(pageable);
    return new PageResponse<>(
        products.getContent().stream()
            .map(this::toResponse)
            .collect(Collectors.toList()),
        products.getNumber(),
        products.getSize(),
        products.getTotalElements(),
        products.getTotalPages(),
        products.hasNext(),
        products.hasPrevious());
  }

  @Transactional
  public ProductResponse createProduct(ProductCreateRequest request) {
    Product product = new Product();
    product.setName(request.getName());
    product.setPrice(request.getPrice());
    Product saved = productRepository.save(product);
    return toResponse(saved);
  }

  private ProductResponse toResponse(Product product) {
    return new ProductResponse(product.getId(), product.getName(), product.getPrice());
  }
}
