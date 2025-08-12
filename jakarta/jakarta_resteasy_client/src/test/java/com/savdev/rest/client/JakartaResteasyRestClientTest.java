package com.savdev.rest.client;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.savdev.rest.api.RestCrudApi;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@WireMockTest
class JakartaResteasyRestClientTest {

  private static final Logger logger = LogManager.getLogger();

  public static final Long ITEM_ID = 777L;
  public static final Long LAST_ITEM_ID = 999L;
  public static final String ITEM_NAME = "test item";
  public static final String ITEM_BODY_FILE_NAME = "item.json";
  public static final String ITEMS_LIST_BODY_FILE_NAME = "items.list.json";

  @BeforeAll
  static void beforeAll() {
    logger.debug(() -> "test logging configuration");
  }

  @Test
  void getItems(WireMockRuntimeInfo wmRuntimeInfo) throws IOException {
    var wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(get(urlPathEqualTo(RestCrudApi.BASE_URL))
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
          .withHeader(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.name())
          .withBodyFile(ITEMS_LIST_BODY_FILE_NAME)));

    try (var restClient = new JakartaResteasyRestClient(wmRuntimeInfo.getHttpBaseUrl())) {
      var items = restClient.restApiProxy().getItems();
      Assertions.assertNotNull(items);
      Assertions.assertEquals(3, items.size());
      Assertions.assertEquals(LAST_ITEM_ID, items.getLast().id());
    }
  }

  @Test
  void getItemById(WireMockRuntimeInfo wmRuntimeInfo) throws IOException {
    var wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(get(urlMatching(RestCrudApi.BASE_URL + "/" + ITEM_ID))
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
          .withHeader(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.name())
          .withBodyFile(ITEM_BODY_FILE_NAME)));

    try (var restClient = new JakartaResteasyRestClient(wmRuntimeInfo.getHttpBaseUrl())) {
      var item = restClient.restApiProxy().getById(ITEM_ID);
      Assertions.assertNotNull(item);
      Assertions.assertEquals(ITEM_ID, item.id());
      Assertions.assertEquals(ITEM_NAME, item.name());
      Assertions.assertNotNull(item.time());
    }
  }
}