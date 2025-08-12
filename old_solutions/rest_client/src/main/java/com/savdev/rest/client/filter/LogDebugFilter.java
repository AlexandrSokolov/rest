package com.savdev.rest.client.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;

public class LogDebugFilter implements ClientResponseFilter {

  private final Logger logger;
  private final ObjectMapper objectMapper;

  private final int debugLogEntrySize;

  public static LogDebugFilter instance(
    final Class<?> clazz,
    final ObjectMapper objectMapper,
    final int debugLogEntrySize) {
    return new LogDebugFilter(clazz, objectMapper, debugLogEntrySize);
  }

  private LogDebugFilter(
    final Class<?> clazz,
    final ObjectMapper objectMapper, int debugLogEntrySize) {
    this.logger = LogManager.getLogger(clazz);
    this.objectMapper = objectMapper;
    this.debugLogEntrySize = debugLogEntrySize;
  }

  @Override
  public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) {
    logger.debug(() -> RequestResponseInfo.clientInstance(
      debugLogEntrySize, objectMapper, requestContext, responseContext));
  }
}
