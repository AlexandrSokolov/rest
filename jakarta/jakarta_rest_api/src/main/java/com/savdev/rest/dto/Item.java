package com.savdev.rest.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record Item(
  Long id,
  String name,
  ZonedDateTime time,
  BigDecimal amount,
  ItemState state,
  DistanceInMeters distance) {
}
