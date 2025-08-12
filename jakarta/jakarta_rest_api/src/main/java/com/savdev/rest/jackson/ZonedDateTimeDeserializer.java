package com.savdev.rest.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * By default, ZonedDateTime is deserialized from
 *  `2025-08-24T18:35:06.091903206+02:00[Europe/Berlin]`
 * with time offset and time zone lost as:
 *  `2025-08-24T16:35:06.091903206Z`
 * expected ZonedDateTime keeps both time offset and time zone
 */
public class ZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {

  @Override
  public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
    return ZonedDateTime.parse(jsonParser.getValueAsString());
  }
}
