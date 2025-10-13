package com.savdev.rest.sb.app.service;

import com.savdev.rest.client.lib1.api.RestApi1;
import com.savdev.rest.client.lib2.api.RestApi2;
import com.savdev.rest.sb.app.service.config.Lib2WireMockConf;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.wiremock.spring.EnableWireMock;

import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EnableWireMock
class RestLibsConsumerServiceTest {

  private static final String STR_VALUE1 = "\"value 1\"";
  private static final String STR_VALUE2 = "\"value 2\"";

  @Autowired
  private RestLibsConsumerService service;

  @Test
  void lib1StrValue() {
    stubFor(get(urlPathMatching(RestApi1.BASE_URL))
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
          .withHeader(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.name())
          .withBody(STR_VALUE1)));

    assertEquals(STR_VALUE1, service.lib1StrValue());
  }

  @Test
  void lib2StrValue() {
    stubFor(get(urlPathMatching(RestApi2.BASE_URL))
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
          .withHeader(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.name())
          .withBody(STR_VALUE2)));

    assertEquals(STR_VALUE2, service.lib2StrValue());
  }
}