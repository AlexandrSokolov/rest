package com.savdev.rest.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CustomObjectMapperProvider implements ContextResolver<ObjectMapper> {

  private final ObjectMapper objectMapper = DefaultObjectMapper.instance();

  @Override
  public ObjectMapper getContext(Class<?> aClass) {
    return objectMapper;
  }
}
