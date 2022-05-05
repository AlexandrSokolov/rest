package com.savdev.rest.client.jax.rs.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.test.commons.test.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.savdev.rest.client.jax.rs.filter.RequestResponseInfoTest.USER_JSON;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WireMockTest
public class LogDebugFilterTest extends BaseTest {

  public static final String HTTP_URL = "/log";

  @Test
  public void testFilter(WireMockRuntimeInfo wmRuntimeInfo) {
    WireMock wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(get(HTTP_URL)
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_ENCODING, MediaType.APPLICATION_JSON)
          .withBodyFile(USER_JSON)));

    LogDebugFilter logDebugFilter = Mockito.spy(LogDebugFilter.instance(
      LogDebugFilterTest.class,
      new ObjectMapper()));

    Client client = ClientBuilder.newBuilder()
      .build();
    client.register(logDebugFilter);

    WebTarget target = client.target(wmRuntimeInfo.getHttpBaseUrl() + HTTP_URL);
    try (Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get()) {

      verify(logDebugFilter, times(1))
        .filter(
          any(ClientRequestContext.class),
          any(ClientResponseContext.class));

      //make sure we still can read the entity:
      Assertions.assertFalse(response.readEntity(String.class).isEmpty());
    }
  }
}
