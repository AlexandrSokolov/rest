package com.savdev.rest.client.resteasy.httpclient;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.savdev.rest.client.ResteasyProxyClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

@WireMockTest
public class ResteasyHttpClientTest {

  public static final String BODY = "test body";

  @Test
  public void testResteasyHttpClient(WireMockRuntimeInfo wmRuntimeInfo) {
    WireMock wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(get(urlPathEqualTo(ResteasyHttpClientTestRestApi.BASE_URL))
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
          .withHeader(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.name())
          .withBody(BODY)));

    try (ResteasyProxyClient<ResteasyHttpClientTestRestApi> client = new ResteasyHttpClientProvider()
      .provide(
        wmRuntimeInfo.getHttpBaseUrl(),
      ResteasyHttpClientTestRestApi.class)) {
      Assertions.assertEquals(BODY, client.proxy().get());
    }
  }
}
