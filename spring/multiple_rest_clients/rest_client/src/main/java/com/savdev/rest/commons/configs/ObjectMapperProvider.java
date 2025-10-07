package com.savdev.rest.commons.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

  private final ObjectMapper mapper;

  public ObjectMapperProvider(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public ObjectMapper getContext(Class<?> aClass) {
    return ObjectMapperFactory.instance();
  }
}
