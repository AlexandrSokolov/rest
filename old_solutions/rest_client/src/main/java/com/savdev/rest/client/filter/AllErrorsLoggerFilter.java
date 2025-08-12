package com.savdev.rest.client.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.savdev.rest.client.RestClientDefaults;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.Response;

import static com.savdev.rest.client.RestClientDefaults.DEFAULT_DEBUG_LOG_ENTRY_SIZE;

public class AllErrorsLoggerFilter implements ClientResponseFilter {

  private final Logger logger;
  final private ObjectMapper objectMapper;

  final private int debugLogEntrySize;

  private AllErrorsLoggerFilter(
    final Class<?> clazz, final ObjectMapper objectMapper, int debugLogEntrySize) {
    this.logger = LogManager.getLogger(clazz);
    this.objectMapper = objectMapper;
    this.debugLogEntrySize = debugLogEntrySize;
  }

  /**
   *
   * @param objectMapper
   * @return
   */
  public static AllErrorsLoggerFilter instance(
    final Class<?> clazz,
    final ObjectMapper objectMapper,
    final int debugLogEntrySize){
    return new AllErrorsLoggerFilter(clazz, objectMapper, debugLogEntrySize);
  }

  public static AllErrorsLoggerFilter instance(
    final Class<?> clazz){
    return new AllErrorsLoggerFilter(clazz,
      RestClientDefaults.objectMapper(),
      DEFAULT_DEBUG_LOG_ENTRY_SIZE);
  }

  @Override
  public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) {
    if (Sets.newHashSet(
        Response.Status.Family.CLIENT_ERROR,
        Response.Status.Family.SERVER_ERROR)
      .contains(Response.Status.Family.familyOf(responseContext.getStatus()))) {
      logger.error(() -> RequestResponseInfo.clientInstance(
        debugLogEntrySize, objectMapper, requestContext, responseContext));
    }
  }
}
