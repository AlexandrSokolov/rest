package com.savdev.rest.client.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import javax.ws.rs.ext.Provider;

@Provider
public class JacksonObjectMapperProvider extends JacksonJaxbJsonProvider {

  public JacksonObjectMapperProvider(final ObjectMapper mapper) {
    super();
    _mapperConfig.setMapper(mapper);
  }
}