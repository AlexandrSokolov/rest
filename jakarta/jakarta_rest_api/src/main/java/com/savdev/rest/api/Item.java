package com.savdev.rest.api;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record Item(
  Long id,
  String name,
  ZonedDateTime time,
  BigDecimal amount) {
}
