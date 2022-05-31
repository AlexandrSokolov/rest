package com.savdev.rest.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.savdev.rest.client.api.MatrixParamsRestApi;
import org.jboss.resteasy.specimpl.PathSegmentImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

@WireMockTest
public class MatrixParamsTest {

  public static final String RESPONSE_BODY = "matrix param test";
  public static final String ALL_URL = "/categories;name=foo/objects;name=green/attributes;name=size";

  ResteasyProxyClient resteasyClient = new ResteasyProxyClient();

  /*
  Url: /categories/objects;name=green
   */
  @Test
  public void testMatrixParam(WireMockRuntimeInfo wmRuntimeInfo) {
    WireMock wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(get(urlPathEqualTo(MatrixParamsRestApi.PATH + ";name=green"))
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
          .withHeader(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.name())
          .withBody(RESPONSE_BODY)));

    String responseBody = resteasyClient.proxy(
        wmRuntimeInfo.getHttpBaseUrl(),
        new ObjectMapper(),
        MatrixParamsRestApi.class)
      .getMatrixParam("green");

    Assertions.assertEquals(RESPONSE_BODY, responseBody);
  }

  /*
  Url: `/categories;name=foo/objects;name=green`
   */
  @Test
  public void testMatrixObjectsByCategories(WireMockRuntimeInfo wmRuntimeInfo) {
    WireMock wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(get(urlPathEqualTo("/categories;name=foo/objects;name=green"))
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
          .withHeader(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.name())
          .withBody(RESPONSE_BODY)));

    //`PathSegmentImpl` resteasy specific!
    PathSegment pathSegment = PathSegmentImpl.parseSegments("/categories;name=foo", false).get(0);

    String responseBody = resteasyClient.proxy(
        wmRuntimeInfo.getHttpBaseUrl(),
        new ObjectMapper(),
        MatrixParamsRestApi.class)
      .objectsByCategory(pathSegment, "green");

    Assertions.assertEquals(RESPONSE_BODY, responseBody);
  }

  /**
   * Url `all/categories;name=foo/objects;name=green/attributes;name=size`
   */
  @Test
  @Disabled //not working, in case method defines it encodes url into:
  //`/all/%5B/categories;name=foo,%20/objects;name=green,%20/attributes;name=foo%5D`
  public void testMatrixAllSegments(WireMockRuntimeInfo wmRuntimeInfo) {
    WireMock wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(get(urlPathEqualTo("/all" + ALL_URL))
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
          .withHeader(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.name())
          .withBody(RESPONSE_BODY)));

    String responseBody = resteasyClient.proxy(
        wmRuntimeInfo.getHttpBaseUrl(),
        new ObjectMapper(),
        MatrixParamsRestApi.class)
      .allSegments(PathSegmentImpl.parseSegments(
        "/categories;name=foo/objects;name=green/attributes;name=size",
        false));

    Assertions.assertEquals(RESPONSE_BODY, responseBody);
  }
}
