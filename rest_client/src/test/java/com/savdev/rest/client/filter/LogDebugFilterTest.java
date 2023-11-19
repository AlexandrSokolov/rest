package com.savdev.rest.client.filter;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.savdev.rest.commons.test.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
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
import static com.savdev.rest.client.RestClientDefaults.DEFAULT_DEBUG_LOG_ENTRY_SIZE;
import static com.savdev.rest.client.RestClientDefaults.objectMapper;
import static com.savdev.rest.client.filter.RequestResponseInfoTest.USER_JSON;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WireMockTest
public class LogDebugFilterTest extends BaseTest {

  public static final String HTTP_URL = "/log";

  @Test
  @DisplayName("LogDebugFilter is triggered for json")
  public void testFilter(WireMockRuntimeInfo wmRuntimeInfo) {
    WireMock wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(get(HTTP_URL)
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
          .withBodyFile(USER_JSON)));

    LogDebugFilter logDebugFilter = Mockito.spy(LogDebugFilter.instance(
      LogDebugFilterTest.class,
      objectMapper(),
      DEFAULT_DEBUG_LOG_ENTRY_SIZE));

    Client client = ClientBuilder.newBuilder().register(logDebugFilter).build();
    WebTarget target = client.target(wmRuntimeInfo.getHttpBaseUrl() + HTTP_URL);
    Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();

    verify(logDebugFilter, times(1))
      .filter(
        any(ClientRequestContext.class),
        any(ClientResponseContext.class));

    //make sure we still can read the entity:
    Assertions.assertFalse(response.readEntity(String.class).isEmpty());
  }
}
