package com.savdev.rest.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.savdev.rest.client.api.HeaderParamRestApi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

@WireMockTest
public class HeaderParamTest {

  public static final String RESPONSE_BODY = "header param test";
  public static final String HEADER_VALUE = "test header";

  ResteasyProxyClient resteasyClient = new ResteasyProxyClient();

  @Test
  public void testCookieParam(WireMockRuntimeInfo wmRuntimeInfo) {
    WireMock wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(get(urlPathEqualTo(HeaderParamRestApi.PATH))
        .withHeader(HeaderParamRestApi.HEADER_PARAM, equalTo(HEADER_VALUE))
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
          .withHeader(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.name())
          .withBody(RESPONSE_BODY)));

    String responseBody = resteasyClient.proxy(
        wmRuntimeInfo.getHttpBaseUrl(),
        new ObjectMapper(),
        HeaderParamRestApi.class)
      .getHeaderParam(HEADER_VALUE);

    Assertions.assertEquals(RESPONSE_BODY, responseBody);
  }
}
