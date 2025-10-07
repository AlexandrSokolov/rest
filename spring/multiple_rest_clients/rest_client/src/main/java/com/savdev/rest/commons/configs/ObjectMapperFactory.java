package com.savdev.rest.commons.configs;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ObjectMapperFactory {

  private static final ObjectMapper INSTANCE = new ObjectMapper();

  static {
    INSTANCE.registerModule(new JavaTimeModule());
    INSTANCE.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    INSTANCE.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public static ObjectMapper instance() {
    return INSTANCE;
  }

  private ObjectMapperFactory() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }
}
