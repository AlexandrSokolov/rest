package com.savdev.rest.sb.app.rest.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

  private static final ObjectMapper INSTANCE = new ObjectMapper();

  static {
    INSTANCE.registerModule(new JavaTimeModule());
    INSTANCE.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    INSTANCE.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }


  @Override
  public ObjectMapper getContext(Class<?> aClass) {
    return INSTANCE;
  }
}
