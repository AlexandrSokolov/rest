package com.savdev.rest.client.jax.rs.filter;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;

class TestClientResponseFilter implements ClientResponseFilter {

  private RequestResponseInfo requestResponseInfo;
  private ObjectMapper objectMapper;

  public static TestClientResponseFilter instance(final ObjectMapper objectMapper) {
    TestClientResponseFilter f = new TestClientResponseFilter();
    f.objectMapper = objectMapper;
    return f;
  }

  @Override
  public void filter(ClientRequestContext clientRequestContext, ClientResponseContext clientResponseContext) {
    requestResponseInfo = RequestResponseInfo.clientInstance(
      this.objectMapper, clientRequestContext, clientResponseContext);
  }

  public RequestResponseInfo getRequestResponseInfo() {
    return requestResponseInfo;
  }
}
