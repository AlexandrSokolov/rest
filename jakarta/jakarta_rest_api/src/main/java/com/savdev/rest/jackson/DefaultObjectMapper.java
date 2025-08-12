package com.savdev.rest.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class DefaultObjectMapper {

  public static ObjectMapper instance() {
    return new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .registerModule(new SimpleModule()
        .addSerializer(BigDecimal.class, new MoneySerializer())
        .addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer())
        .addDeserializer(BigDecimal.class, new MoneyDeserializer())
        .addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer()));
  }
}
