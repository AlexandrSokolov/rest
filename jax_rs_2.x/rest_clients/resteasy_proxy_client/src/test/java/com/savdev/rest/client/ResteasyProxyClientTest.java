package com.savdev.rest.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.savdev.rest.client.api.TestRestApi;
import com.savdev.rest.client.dto.User;
import com.savdev.rest.client.dto.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.created;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

@WireMockTest
public class ResteasyProxyClientTest {

  public static final String USER_JSON = "responses/user.json";
  public static final String USER_NAME = "Test Resteasy Proxy Client";

  ResteasyProxyClient resteasyClient = new ResteasyProxyClient();

  @Test
  public void testCreateUser(WireMockRuntimeInfo wmRuntimeInfo) {
    WireMock wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(post(urlPathEqualTo(TestRestApi.BASE_URL))
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
          .withHeader(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.name())
          .withBodyFile(USER_JSON)));

    User user = resteasyClient.proxy(
      wmRuntimeInfo.getHttpBaseUrl(),
      objectMapper(),
      TestRestApi.class)
        .create(
          UserData.instance("Town", LocalDateTime.now()));

    Assertions.assertTrue(Optional.ofNullable(user).isPresent());
    Assertions.assertEquals(USER_NAME, user.getName());
  }

  private ObjectMapper objectMapper() {
    return new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }
}
