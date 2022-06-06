package com.savdev.rest.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.savdev.rest.client.api.ResponseObjectApi;
import com.savdev.rest.client.api.ResponseObjectRestApi;
import org.jboss.resteasy.client.jaxrs.internal.ClientResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

@WireMockTest
public class ResponseObjectTest extends ResteasyProxyClientBaseTest<ResponseObjectRestApi> {

  public static final String RESPONSE_BODY = "response object test";

  @Test
  public void testQueryParam(WireMockRuntimeInfo wmRuntimeInfo) {
    WireMock wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(get(urlPathEqualTo(ResponseObjectRestApi.PATH))
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
          .withHeader(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.name())
          .withHeader(HttpHeaders.CONTENT_LENGTH, Integer.toString(RESPONSE_BODY.length()))
          .withBody(RESPONSE_BODY)));

    try(ResteasyProxyClient<ResponseObjectRestApi> resteasyProxyClient = resteasyProxyClient(
      wmRuntimeInfo, ResponseObjectRestApi.class)) {
      final ResponseObjectApi responseObject = resteasyProxyClient.proxy().get();
      try(ClientResponse response = responseObject.response()) {
        Assertions.assertEquals(RESPONSE_BODY, responseObject.body());
        Assertions.assertEquals(Response.Status.OK.getStatusCode(), responseObject.status());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, responseObject.contentType());
        Assertions.assertEquals(RESPONSE_BODY.length(), response.getLength());
      }
    }
  }
}
