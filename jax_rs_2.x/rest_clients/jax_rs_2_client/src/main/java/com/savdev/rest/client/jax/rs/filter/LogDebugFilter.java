package com.savdev.rest.client.jax.rs.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;

class LogDebugFilter implements ClientResponseFilter {

  private final Logger logger;
  private final ObjectMapper objectMapper;

  public static LogDebugFilter instance(
    final Class<?> clazz,
    final ObjectMapper objectMapper) {
    return new LogDebugFilter(clazz, objectMapper);
  }

  private LogDebugFilter(
    final Class<?> clazz,
    final ObjectMapper objectMapper) {
    this.logger = LogManager.getLogger(clazz);
    this.objectMapper = objectMapper;
  }

  @Override
  public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) {
    logger.debug(() -> RequestResponseInfo.clientInstance(
      objectMapper, requestContext, responseContext));
  }
}
