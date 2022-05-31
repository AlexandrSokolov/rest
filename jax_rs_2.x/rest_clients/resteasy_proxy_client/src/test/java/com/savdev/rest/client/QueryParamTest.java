package com.savdev.rest.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.savdev.rest.client.api.QueryParamRestApi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.absent;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.or;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

@WireMockTest
public class QueryParamTest {

  public static final String RESPONSE_BODY = "query param test";

  ResteasyProxyClient resteasyClient = new ResteasyProxyClient();

  @Test
  public void testQueryParam(WireMockRuntimeInfo wmRuntimeInfo) {
    WireMock wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(get(urlPathEqualTo(QueryParamRestApi.PATH))
        .withQueryParam(QueryParamRestApi.QUERY_PARAM_NAME, or(matching("[a-z]+"), absent()))
        .withQueryParam(QueryParamRestApi.QUERY_PARAM_LIMIT, or(matching("\\d+"), absent()))
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
          .withHeader(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.name())
          .withBody(RESPONSE_BODY)));

    String responseBody = resteasyClient.proxy(
        wmRuntimeInfo.getHttpBaseUrl(),
        new ObjectMapper(),
        QueryParamRestApi.class)
      .getQueryParam("alex", 100);

    Assertions.assertEquals(RESPONSE_BODY, responseBody);
  }
}
