package com.savdev.rest.client.filter;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.savdev.rest.commons.test.BaseTest;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.savdev.rest.client.RestClientDefaults.DEFAULT_DEBUG_LOG_ENTRY_SIZE;
import static com.savdev.rest.client.RestClientDefaults.objectMapper;
import static com.savdev.rest.client.filter.RequestResponseInfo.NO_REQUEST_BODY;
import static com.savdev.rest.client.filter.RequestResponseInfo.NO_RESPONSE_BODY;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@WireMockTest
public class RequestResponseInfoTest extends BaseTest {

  public static final String HTTP_URL = "http://localhost";
  public static final String TEST_URL = "/test";

  public static final String TO_STRING_WITH_BODY_FN = "to.string.request.body.txt";
  public static final String TO_STRING_WITH_FORM_FN = "to.string.request.form.txt";

  public static final String WIRE_MOCK_RESOURSES_FILES = "__files/";

  public static final String USER_JSON = "responses/user.json";

  @Test
  @DisplayName("Json request, no response from server")
  public void testToStringWithBody() throws IOException {
    ClientRequestContext clientRequestContext = mockRequest();
    when(clientRequestContext.getStringHeaders()).thenReturn(
      new MultivaluedHashMap<>(ImmutableMap.of(
        HttpHeaders.CONTENT_TYPE,
        MediaType.APPLICATION_JSON)));
    when(clientRequestContext.getEntity()).thenReturn(ImmutableMap.of("name", "Alex"));

    ClientResponseContext clientResponseContext = mockResponse();

    RequestResponseInfo info = RequestResponseInfo.clientInstance(
      DEFAULT_DEBUG_LOG_ENTRY_SIZE,
      objectMapper(),
      clientRequestContext,
      clientResponseContext);

    Assertions.assertEquals(
      IOUtils.toString(testInputStream(TO_STRING_WITH_BODY_FN),
        StandardCharsets.UTF_8),
      info.toString());
  }

  @Test
  @DisplayName("Request as form (" + MediaType.APPLICATION_FORM_URLENCODED + "), no response from server")
  public void testToStringWithForm() throws IOException {
    ClientRequestContext clientRequestContext = mockRequest();
    when(clientRequestContext.getStringHeaders()).thenReturn(
      new MultivaluedHashMap<>(ImmutableMap.of(
        HttpHeaders.CONTENT_TYPE,
        MediaType.APPLICATION_FORM_URLENCODED,
        HttpHeaders.ACCEPT,
        MediaType.APPLICATION_JSON)));
    when(clientRequestContext.getEntity()).thenReturn(new Form("user", "Alex S"));

    ClientResponseContext clientResponseContext = mockResponse();

    RequestResponseInfo info = RequestResponseInfo.clientInstance(
      DEFAULT_DEBUG_LOG_ENTRY_SIZE,
      objectMapper(),
      clientRequestContext,
      clientResponseContext);

    Assertions.assertEquals(
      IOUtils.toString(testInputStream(TO_STRING_WITH_FORM_FN),
        StandardCharsets.UTF_8),
      info.toString());
  }

  @Test
  @DisplayName("No request, json response from server")
  public void testJsonRequestJsonResponse(WireMockRuntimeInfo wmRuntimeInfo) throws IOException {
    WireMock wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(get(TEST_URL)
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
          .withBodyFile(USER_JSON)));

    RequestResponseInfoStoreFilter f = RequestResponseInfoStoreFilter.instance();

    Assertions.assertNull(f.getRequestResponseInfo());

    Client client = ClientBuilder.newBuilder().register(f).build();

    WebTarget target = client.target(wmRuntimeInfo.getHttpBaseUrl() + TEST_URL);
    Response response = target.request().get();

    RequestResponseInfo rri = f.getRequestResponseInfo();

    Assertions.assertNotNull(rri);

    //check requests information
    Assertions.assertEquals(HttpMethod.GET, rri.getHttpMethod());
    Assertions.assertEquals(
      URI.create(wmRuntimeInfo.getHttpBaseUrl() + TEST_URL),
      rri.getUrl());
    Assertions.assertTrue(rri.getRequestHeaders().containsKey("Accept-Encoding"));
    Assertions.assertIterableEquals(
      Lists.newArrayList("gzip, deflate"),
      rri.getRequestHeaders().get("Accept-Encoding"));
    Assertions.assertEquals(NO_REQUEST_BODY, rri.getRequestBody());

    //check response information
    Assertions.assertEquals(Response.Status.OK.getStatusCode(), rri.getResponseStatus());
    Assertions.assertNotNull(rri.getResponseBody());
    Assertions.assertEquals(
      IOUtils.toString(testInputStream(WIRE_MOCK_RESOURSES_FILES + USER_JSON),
        StandardCharsets.UTF_8),
      rri.getResponseBody().toString());
    Assertions.assertFalse(rri.getResponseHeaders().isEmpty());

    //make sure we still can read the entity:
    Assertions.assertFalse(response.readEntity(String.class).isEmpty());
  }

  @Test
  @DisplayName("No request, not textual response from server")
  public void testNotTextualResponse(WireMockRuntimeInfo wmRuntimeInfo) {
    WireMock wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(get(TEST_URL)
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
          .withBodyFile(USER_JSON)));

    RequestResponseInfoStoreFilter f = RequestResponseInfoStoreFilter.instance();

    Assertions.assertNull(f.getRequestResponseInfo());

    Client client = ClientBuilder.newBuilder().register(f).build();

    WebTarget target = client.target(wmRuntimeInfo.getHttpBaseUrl() + TEST_URL);
    Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();

    RequestResponseInfo rri = f.getRequestResponseInfo();

    Assertions.assertNotNull(rri);

    //check requests information
    Assertions.assertEquals(NO_REQUEST_BODY, rri.getRequestBody());

    //check response information
    Assertions.assertEquals(Response.Status.OK.getStatusCode(), rri.getResponseStatus());

    //key check here, response should not be parsed, if content is not text-based
    Assertions.assertEquals(NO_RESPONSE_BODY, rri.getResponseBody().toString());

    //make sure we still can read the entity:
    Assertions.assertFalse(response.readEntity(String.class).isEmpty());
  }

  private ClientRequestContext mockRequest() {
    ClientRequestContext clientRequestContext = mock(ClientRequestContext.class);
    when(clientRequestContext.getUri()).thenReturn(URI.create(HTTP_URL));
    when(clientRequestContext.getMethod()).thenReturn(HttpMethod.POST);
    return clientRequestContext;
  }

  private ClientResponseContext mockResponse() {
    ClientResponseContext clientResponseContext = mock(ClientResponseContext.class);
    when(clientResponseContext.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
    when(clientResponseContext.getHeaders()).thenReturn(
      new MultivaluedHashMap<>(ImmutableMap.of(
        HttpHeaders.CONTENT_TYPE,
        MediaType.APPLICATION_JSON)));
    when(clientResponseContext.hasEntity()).thenReturn(false);
    return clientResponseContext;
  }
}
