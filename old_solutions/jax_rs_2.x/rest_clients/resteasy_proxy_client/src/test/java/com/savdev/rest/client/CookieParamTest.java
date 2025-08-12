package com.savdev.rest.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.savdev.rest.client.api.CookieParamRestApi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.savdev.rest.client.api.CookieParamRestApi.COOKIE_PARAM_NAME;

@WireMockTest
public class CookieParamTest extends ResteasyProxyClientBaseTest<CookieParamRestApi> {

  public static final String RESPONSE_BODY = "cookie param test";
  public static final String COOKIE_VALUE = "test value";

  @Test
  public void testCookieParam(WireMockRuntimeInfo wmRuntimeInfo) {
    WireMock wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(get(urlPathEqualTo(CookieParamRestApi.PATH))
        .withCookie(COOKIE_PARAM_NAME, equalTo(COOKIE_VALUE))
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
          .withHeader(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.name())
          .withBody(RESPONSE_BODY)));

    try (ResteasyProxyClient<CookieParamRestApi> resteasyClient = resteasyProxyClient(
      wmRuntimeInfo, CookieParamRestApi.class)) {
      String responseBody = resteasyClient.proxy()
        .getCookieParam(COOKIE_VALUE);

      Assertions.assertEquals(RESPONSE_BODY, responseBody);
    }
  }
}
