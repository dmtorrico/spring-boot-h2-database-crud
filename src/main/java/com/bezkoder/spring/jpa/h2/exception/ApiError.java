package com.bezkoder.spring.jpa.h2.exception;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

  private final Instant timestamp;
  private final int status;
  private final String error;
  private final String message;
  private final String path;
  private final List<FieldValidationError> fieldErrors;

  private ApiError(Builder builder) {
    this.timestamp = builder.timestamp;
    this.status = builder.status;
    this.error = builder.error;
    this.message = builder.message;
    this.path = builder.path;
    this.fieldErrors = builder.fieldErrors;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public int getStatus() {
    return status;
  }

  public String getError() {
    return error;
  }

  public String getMessage() {
    return message;
  }

  public String getPath() {
    return path;
  }

  public List<FieldValidationError> getFieldErrors() {
    return fieldErrors;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private Instant timestamp = Instant.now();
    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldValidationError> fieldErrors;

    public Builder status(int status) {
      this.status = status;
      return this;
    }

    public Builder error(String error) {
      this.error = error;
      return this;
    }

    public Builder message(String message) {
      this.message = message;
      return this;
    }

    public Builder path(String path) {
      this.path = path;
      return this;
    }

    public Builder fieldErrors(List<FieldValidationError> fieldErrors) {
      this.fieldErrors = fieldErrors == null ? null : List.copyOf(fieldErrors);
      return this;
    }

    public ApiError build() {
      if (fieldErrors != null && fieldErrors.isEmpty()) {
        this.fieldErrors = Collections.emptyList();
      }
      return new ApiError(this);
    }
  }
}
