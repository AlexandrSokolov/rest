package com.savdev.rest.client.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * For rest clients, based on resteasy proxy generation,
 * the ObjectMapper jackson provider must specify `@Consumer` and `@Producers`
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class JacksonObjectMapperProvider extends JacksonJaxbJsonProvider {

  public JacksonObjectMapperProvider(final ObjectMapper mapper) {
    super();
    _mapperConfig.setMapper(mapper);
  }

  public JacksonObjectMapperProvider() {
  }
}