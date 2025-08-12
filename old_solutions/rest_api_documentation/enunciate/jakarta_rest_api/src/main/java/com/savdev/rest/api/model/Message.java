package com.savdev.rest.api.model;

import java.time.ZonedDateTime;

/**
 * This model represents a message
 *
 * @param message
 * @param time
 */
public record Message (
  String message,
  ZonedDateTime time) {
}
