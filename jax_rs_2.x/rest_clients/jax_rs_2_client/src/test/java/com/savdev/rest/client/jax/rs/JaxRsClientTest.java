package com.savdev.rest.client.jax.rs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

@WireMockTest
public class JaxRsClientTest {

  public static final String HTTP_URL = "/test";
  public static final String USER_JSON = "responses/user.json";

  JaxRsClient jaxRsClient = new JaxRsClient();

  @Test
  public void testSendRequest(WireMockRuntimeInfo wmRuntimeInfo) {
    WireMock wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(post(urlPathEqualTo(HTTP_URL))
      .withQueryParam("testParam", equalTo("some Value"))
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
          .withBodyFile(USER_JSON)));

    jaxRsClient.handleRequest(
      wmRuntimeInfo.getHttpBaseUrl() + HTTP_URL,
      HttpMethod.POST,
      new MultivaluedHashMap<>(ImmutableMap.of(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)),
      new AbstractMap.SimpleEntry<>("testParam", "some Value"),
      MediaType.APPLICATION_JSON_TYPE,
      MediaType.APPLICATION_JSON_TYPE,
      objectMapper(),
      Entity.entity(
        ImmutableMap.of(
          "address", "Town",
          "currentTime", LocalDateTime.now()),
        MediaType.APPLICATION_JSON),
      response -> {
        Assertions.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        String responseValue = response.readEntity(String.class);
        Assertions.assertTrue(Optional.ofNullable(responseValue).isPresent());
        Assertions.assertFalse(responseValue.isEmpty());
      });
  }

  @Test
  public void testSendRequestFails(WireMockRuntimeInfo wmRuntimeInfo) {
    WireMock wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(post(urlPathEqualTo(HTTP_URL))
      .withQueryParam("testParam", equalTo("Value"))
      .willReturn(
        aResponse()
          .withStatus(Response.Status.CONFLICT.getStatusCode())));

    jaxRsClient.handleRequest(
      wmRuntimeInfo.getHttpBaseUrl() + HTTP_URL,
      HttpMethod.POST,
      new MultivaluedHashMap<>(ImmutableMap.of(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)),
      new AbstractMap.SimpleEntry<>("testParam", "Value"),
      MediaType.APPLICATION_JSON_TYPE,
      MediaType.APPLICATION_JSON_TYPE,
      objectMapper(),
      Entity.entity(
        ImmutableMap.of("address", "Town"),
        MediaType.APPLICATION_JSON),
      response -> {
        Assertions.assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
        String responseValue = response.readEntity(String.class);
        Assertions.assertTrue(Optional.ofNullable(responseValue).isPresent());
        Assertions.assertTrue(responseValue.isEmpty());
      });
  }

  private ObjectMapper objectMapper() {
    return new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }
}
