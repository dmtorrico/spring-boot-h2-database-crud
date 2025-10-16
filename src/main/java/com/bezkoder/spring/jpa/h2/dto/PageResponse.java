package com.bezkoder.spring.jpa.h2.dto;

import java.util.List;

public class PageResponse<T> {

  private final List<T> data;
  private final int page;
  private final int size;
  private final long totalElements;
  private final int totalPages;
  private final boolean hasNext;
  private final boolean hasPrevious;

  public PageResponse(List<T> data, int page, int size, long totalElements, int totalPages, boolean hasNext,
      boolean hasPrevious) {
    this.data = List.copyOf(data);
    this.page = page;
    this.size = size;
    this.totalElements = totalElements;
    this.totalPages = totalPages;
    this.hasNext = hasNext;
    this.hasPrevious = hasPrevious;
  }

  public List<T> getData() {
    return data;
  }

  public int getPage() {
    return page;
  }

  public int getSize() {
    return size;
  }

  public long getTotalElements() {
    return totalElements;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public boolean isHasNext() {
    return hasNext;
  }

  public boolean isHasPrevious() {
    return hasPrevious;
  }
}
