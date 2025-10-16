package com.bezkoder.spring.jpa.h2.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bezkoder.spring.jpa.h2.dto.OrderRequest;
import com.bezkoder.spring.jpa.h2.dto.OrderResponse;
import com.bezkoder.spring.jpa.h2.exception.ResourceNotFoundException;
import com.bezkoder.spring.jpa.h2.model.AppUser;
import com.bezkoder.spring.jpa.h2.model.CustomerOrder;
import com.bezkoder.spring.jpa.h2.model.Product;
import com.bezkoder.spring.jpa.h2.repository.AppUserRepository;
import com.bezkoder.spring.jpa.h2.repository.CustomerOrderRepository;
import com.bezkoder.spring.jpa.h2.repository.ProductRepository;

@Service
public class OrderService {

  private final AppUserRepository userRepository;
  private final ProductRepository productRepository;
  private final CustomerOrderRepository orderRepository;

  public OrderService(AppUserRepository userRepository, ProductRepository productRepository,
      CustomerOrderRepository orderRepository) {
    this.userRepository = userRepository;
    this.productRepository = productRepository;
    this.orderRepository = orderRepository;
  }

  @Transactional
  public OrderResponse createOrder(OrderRequest request) {
    AppUser user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new ResourceNotFoundException("User %d not found".formatted(request.getUserId())));

    List<Long> requestedIds = request.getProductIds();
    List<Long> distinctIds = requestedIds.stream().distinct().toList();
    List<Product> products = productRepository.findAllById(distinctIds);

    if (products.size() != distinctIds.size()) {
      throw new ResourceNotFoundException("One or more products not found");
    }

    CustomerOrder order = new CustomerOrder();
    order.setOrderNumber(generateOrderNumber());
    order.setUser(user);
    order.setProducts(new HashSet<>(products));
    order.setTotalAmount(calculateTotal(requestedIds, products));

    CustomerOrder saved = orderRepository.save(order);
    return toResponse(saved);
  }

  private BigDecimal calculateTotal(List<Long> requestedIds, List<Product> products) {
    var productById = products.stream()
        .collect(Collectors.toMap(Product::getId, Product::getPrice));

    return requestedIds.stream()
        .map(productById::get)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private String generateOrderNumber() {
    return UUID.randomUUID().toString();
  }

  private OrderResponse toResponse(CustomerOrder order) {
    List<Long> productIds = order.getProducts().stream()
        .map(Product::getId)
        .sorted()
        .collect(Collectors.toList());
    return new OrderResponse(order.getId(), order.getOrderNumber(), order.getUser().getId(), productIds,
        order.getTotalAmount(), order.getCreatedAt());
  }
}
