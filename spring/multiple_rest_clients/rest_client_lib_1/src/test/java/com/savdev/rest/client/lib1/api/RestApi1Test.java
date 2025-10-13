package com.savdev.rest.client.lib1.api;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.wiremock.spring.EnableWireMock;

import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ContextConfiguration(classes = { WireMockRestClientTestConfiguration.class })
@EnableWireMock
class RestApi1Test {

  public static final String STR_VALUE = "\"rest value #1\"";

  @Autowired
  private RestApi1 restApi1;

  @Test
  public void getStrValue() {
    stubFor(get(urlPathMatching(RestApi1.BASE_URL))
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
          .withHeader(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.name())
          .withBody(STR_VALUE)));

    assertEquals(STR_VALUE, restApi1.getStrValue());
  }

}