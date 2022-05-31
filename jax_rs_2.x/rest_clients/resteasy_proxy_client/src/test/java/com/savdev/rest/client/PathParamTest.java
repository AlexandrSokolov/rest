package com.savdev.rest.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.savdev.rest.client.api.PathParamRestApi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

@WireMockTest
public class PathParamTest {

  public static final String RESPONSE_BODY = "path param test";

  ResteasyProxyClient resteasyClient = new ResteasyProxyClient();

  @Test
  public void testPathParam(WireMockRuntimeInfo wmRuntimeInfo) {
    WireMock wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(get(urlMatching(PathParamRestApi.PATH + "/([a-z]*)"))
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
          .withHeader(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.name())
          .withBody(RESPONSE_BODY)));

    String responseBody = resteasyClient.proxy(
        wmRuntimeInfo.getHttpBaseUrl(),
        new ObjectMapper(),
        PathParamRestApi.class)
      .getPathParam("alex");

    Assertions.assertEquals(RESPONSE_BODY, responseBody);
  }
}
