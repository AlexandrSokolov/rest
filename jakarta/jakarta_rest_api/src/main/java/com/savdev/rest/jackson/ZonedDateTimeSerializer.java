package com.savdev.rest.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * By default, ZonedDateTime is serialized as:
 *  `2025-08-24T18:35:06.091903206+02:00`
 *  time zone is lost
 *  expected value for the same time:
 *  `2025-08-24T18:35:06.091903206+02:00[Europe/Berlin]`
 */
public class ZonedDateTimeSerializer extends JsonSerializer<ZonedDateTime> {

  // Define the desired date-time format for deserialization
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(
    "yyyy-MM-dd'T'HH:mm:ss.SSSSXXX'['VV']'");

  @Override
  public void serialize(
    ZonedDateTime zonedDateTime,
    JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeString(FORMATTER.format(zonedDateTime));
  }
}
