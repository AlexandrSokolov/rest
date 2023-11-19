package com.savdev.rest.client.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.Response;

import static com.savdev.rest.client.RestClientDefaults.ERROR_HEADER;

public class ErrorHeaderSaverFilter implements ClientResponseFilter {

  final private ObjectMapper objectMapper;

  final private int debugLogEntrySize;

  private ErrorHeaderSaverFilter(
    final ObjectMapper objectMapper, int debugLogEntrySize) {
    this.objectMapper = objectMapper;
    this.debugLogEntrySize = debugLogEntrySize;
  }

  /**
   * Allows to control, which exact response statuses are considered as errors
   *
   * @param objectMapper
   * @return
   */
  public static ErrorHeaderSaverFilter instance(
    final ObjectMapper objectMapper,
    final int debugLogEntrySize){
    return new ErrorHeaderSaverFilter(objectMapper, debugLogEntrySize);
  }


  @Override
  public void filter(ClientRequestContext clientRequestContext, ClientResponseContext clientResponseContext) {
    if (Sets.newHashSet(
        Response.Status.Family.CLIENT_ERROR,
        Response.Status.Family.SERVER_ERROR)
      .contains(Response.Status.Family.familyOf(clientResponseContext.getStatus()))) {
      clientResponseContext
        .getHeaders()
        .putSingle(
          ERROR_HEADER,
          RequestResponseInfo.clientInstance(
              debugLogEntrySize, objectMapper, clientRequestContext, clientResponseContext)
            .toString());
    }
  }
}
