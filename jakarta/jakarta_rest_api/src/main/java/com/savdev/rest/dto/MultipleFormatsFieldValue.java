package com.savdev.rest.dto;

import java.math.BigDecimal;
import java.util.Map;

public record MultipleFormatsFieldValue<T> (T value) {
  public BigDecimal asDecimal() {
    if (value instanceof BigDecimal) {
      return (BigDecimal)value;
    }
    throw new IllegalStateException("Value is not BigDecimal");
  }

  public String asText() {
    if (value instanceof String) {
      return (String)value;
    }
    throw new IllegalStateException("Value is not String");
  }

  @SuppressWarnings("unchecked")
  public Map<String, Object> asMap() {
    if (value instanceof Map) {
      return (Map<String, Object>)value;
    }
    throw new IllegalStateException("Value is not Map");
  }
}
