package com.savdev.rest.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.savdev.rest.client.jax.rs.filter.ErrorFilter;
import org.apache.logging.log4j.LogManager;

public class ResteasyProxyClientBaseTest<T> {

  ResteasyProxyClient<T> resteasyProxyClient(
    WireMockRuntimeInfo wmRuntimeInfo,
    Class<T> jaxRsProxyInterface) {
    return ResteasyProxyClient.builder(
        wmRuntimeInfo.getHttpBaseUrl(),
        jaxRsProxyInterface)
      .objectMapper(objectMapper())
      .errorFilter(ErrorFilter.instance(
        objectMapper(),
        (rrInfo) -> LogManager.getLogger(jaxRsProxyInterface).error(rrInfo)))
      .build();
  }

  private ObjectMapper objectMapper() {
    return new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }
}
