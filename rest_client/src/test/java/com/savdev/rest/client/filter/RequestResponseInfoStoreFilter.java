package com.savdev.rest.client.filter;

import com.savdev.rest.client.RestClientDefaults;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;


/**
 * The only purpose of this filter is to create RequestResponseInfo for test purposes
 */
class RequestResponseInfoStoreFilter implements ClientResponseFilter {

  private RequestResponseInfo requestResponseInfo;

  public static RequestResponseInfoStoreFilter instance() {
    return new RequestResponseInfoStoreFilter();
  }

  @Override
  public void filter(ClientRequestContext clientRequestContext, ClientResponseContext clientResponseContext) {
    requestResponseInfo = RequestResponseInfo.clientInstance(
      RestClientDefaults.DEFAULT_DEBUG_LOG_ENTRY_SIZE,
      RestClientDefaults.objectMapper(),
      clientRequestContext,
      clientResponseContext);
  }

  public RequestResponseInfo getRequestResponseInfo() {
    return requestResponseInfo;
  }
}
