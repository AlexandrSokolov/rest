package com.savdev.rest.client.filter;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.core.MultivaluedMap;
import java.net.URI;

class RequestResponseInfoBuilder {
  private URI url;
  private String httpMethod;
  private MultivaluedMap<String, String> requestHeaders;
  private Object requestBody;
  private int responseStatus;
  private MultivaluedMap<String, String> responseHeaders;

  //response body is defined as String, not as request body, to avoid extra effort for serialization
  private String responseBody;
  private ObjectMapper objectMapper;

  RequestResponseInfoBuilder objectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    return this;
  }

  RequestResponseInfoBuilder url(URI url) {
    this.url = url;
    return this;
  }

  RequestResponseInfoBuilder httpMethod(String httpMethod) {
    this.httpMethod = httpMethod;
    return this;
  }

  RequestResponseInfoBuilder requestHeaders(MultivaluedMap<String, String> requestHeaders) {
    this.requestHeaders = requestHeaders;
    return this;
  }

  RequestResponseInfoBuilder requestBody(Object requestBody) {
    this.requestBody = requestBody;
    return this;
  }

  RequestResponseInfoBuilder responseStatus(int responseStatus) {
    this.responseStatus = responseStatus;
    return this;
  }

  RequestResponseInfoBuilder responseHeaders(MultivaluedMap<String, String> responseHeaders) {
    this.responseHeaders = responseHeaders;
    return this;
  }

  RequestResponseInfoBuilder responseBody(String responseBody) {
    this.responseBody = responseBody;
    return this;
  }

  RequestResponseInfo build() {
    return new RequestResponseInfo(
      objectMapper,
      url,
      httpMethod,
      requestHeaders,
      requestBody,
      responseStatus,
      responseHeaders,
      responseBody);
  }
}
