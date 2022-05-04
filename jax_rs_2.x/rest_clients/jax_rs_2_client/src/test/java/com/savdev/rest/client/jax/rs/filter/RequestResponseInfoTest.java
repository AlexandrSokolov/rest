package com.savdev.rest.client.jax.rs.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.google.common.collect.ImmutableMap;
import com.test.commons.test.BaseTest;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@WireMockTest(httpPort = RequestResponseInfoTest.HTTP_PORT)
public class RequestResponseInfoTest extends BaseTest {

  public static final int HTTP_PORT = 8081;
  public static final String HTTP_URL = "http://localhost";
  public static final String TEST_URL = "/test";

  public static final String TO_STRING_WITH_BODY_FN = "to.string.request.body.txt";
  public static final String TO_STRING_WITH_FORM_FN = "to.string.request.form.txt";

  public static final String WIRE_MOCK_RESOURSES_FILES = "__files/";

  public static final String USER_JSON = "responses/user.json";

  @BeforeAll
  public static void init() {
    System.setProperty(
      ClientBuilder.JAXRS_DEFAULT_CLIENT_BUILDER_PROPERTY,
      ResteasyClientBuilderImpl.class.getCanonicalName());
  }

  @Test
  public void testToStringWithBody() throws IOException {
    ClientRequestContext clientRequestContext = mockRequest();
    when(clientRequestContext.getStringHeaders()).thenReturn(
      new MultivaluedHashMap<>(ImmutableMap.of(
        HttpHeaders.CONTENT_ENCODING,
        MediaType.APPLICATION_JSON,
        HttpHeaders.ACCEPT_ENCODING,
        MediaType.APPLICATION_JSON)));
    when(clientRequestContext.getEntity()).thenReturn(ImmutableMap.of("name", "Alex"));

    ClientResponseContext clientResponseContext = mockResponse();

    RequestResponseInfo info = RequestResponseInfo.clientInstance(
      new ObjectMapper(), clientRequestContext, clientResponseContext);

    Assertions.assertEquals(
      IOUtils.toString(testInputStream(TO_STRING_WITH_BODY_FN),
        StandardCharsets.UTF_8),
      info.toString());
  }

  @Test
  public void testToStringWithForm() throws IOException {
    ClientRequestContext clientRequestContext = mockRequest();
    when(clientRequestContext.getStringHeaders()).thenReturn(
      new MultivaluedHashMap<>(ImmutableMap.of(
        HttpHeaders.CONTENT_ENCODING,
        MediaType.APPLICATION_FORM_URLENCODED,
        HttpHeaders.ACCEPT_ENCODING,
        MediaType.APPLICATION_JSON)));
    when(clientRequestContext.getEntity()).thenReturn(new Form("user", "Alex S"));

    ClientResponseContext clientResponseContext = mockResponse();

    RequestResponseInfo info = RequestResponseInfo.clientInstance(
      new ObjectMapper(), clientRequestContext, clientResponseContext);

    Assertions.assertEquals(
      IOUtils.toString(testInputStream(TO_STRING_WITH_FORM_FN),
        StandardCharsets.UTF_8),
      info.toString());
  }

  @Test
  public void testJsonRequestJsonResponse(WireMockRuntimeInfo wmRuntimeInfo) throws IOException {
    WireMock wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(get(TEST_URL)
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_ENCODING, MediaType.APPLICATION_JSON)
          .withBodyFile(USER_JSON)));

    TestClientResponseFilter f = TestClientResponseFilter.instance(new ObjectMapper());

    Assertions.assertNull(f.getRequestResponseInfo());

    Client client = ClientBuilder.newBuilder()
      .build();
    client.register(f);

    WebTarget target = client.target(wmRuntimeInfo.getHttpBaseUrl() + TEST_URL);
    try (Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get()) {

      RequestResponseInfo rri = f.getRequestResponseInfo();

      Assertions.assertNotNull(rri);

      //check requests information
      Assertions.assertEquals(HttpMethod.GET, rri.getHttpMethod());
      Assertions.assertEquals(
        URI.create(wmRuntimeInfo.getHttpBaseUrl() + TEST_URL),
        rri.getUrl());
      Assertions.assertEquals(
        new MultivaluedHashMap<>(ImmutableMap.of(
          HttpHeaders.ACCEPT,
          MediaType.APPLICATION_JSON)),
        rri.getRequestHeaders());
      Assertions.assertFalse(rri.getRequestBody().isPresent());

      //check response information
      Assertions.assertEquals(HttpStatus.SC_OK, rri.getResponseStatus());
      Assertions.assertTrue(rri.getResponseBody().isPresent());
      Assertions.assertEquals(
        IOUtils.toString(testInputStream(WIRE_MOCK_RESOURSES_FILES + USER_JSON),
          StandardCharsets.UTF_8),
        rri.getResponseBody().get());
      Assertions.assertFalse(rri.getResponseHeaders().isEmpty());

      //make sure we still can read the entity:
      Assertions.assertFalse(response.readEntity(String.class).isEmpty());
    }
  }

  @Test
  public void testNotTextualResponse(WireMockRuntimeInfo wmRuntimeInfo) throws IOException {
    WireMock wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(get(TEST_URL)
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_ENCODING, MediaType.APPLICATION_OCTET_STREAM)
          .withBodyFile(USER_JSON)));

    TestClientResponseFilter f = TestClientResponseFilter.instance(new ObjectMapper());

    Assertions.assertNull(f.getRequestResponseInfo());

    Client client = ClientBuilder.newBuilder()
      .build();
    client.register(f);

    WebTarget target = client.target(wmRuntimeInfo.getHttpBaseUrl() + TEST_URL);
    try (Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get()) {

      RequestResponseInfo rri = f.getRequestResponseInfo();

      Assertions.assertNotNull(rri);

      //check requests information
      Assertions.assertFalse(rri.getRequestBody().isPresent());

      //check response information
      Assertions.assertEquals(HttpStatus.SC_OK, rri.getResponseStatus());

      //key check here, response should not be parsed, if content is not text-based
      Assertions.assertFalse(rri.getResponseBody().isPresent());

      //make sure we still can read the entity:
      Assertions.assertFalse(response.readEntity(String.class).isEmpty());
    }
  }

  private ClientRequestContext mockRequest() {
    ClientRequestContext clientRequestContext = mock(ClientRequestContext.class);
    when(clientRequestContext.getUri()).thenReturn(URI.create(HTTP_URL));
    when(clientRequestContext.getMethod()).thenReturn(HttpMethod.POST);
    return clientRequestContext;
  }

  private ClientResponseContext mockResponse() {
    ClientResponseContext clientResponseContext = mock(ClientResponseContext.class);
    when(clientResponseContext.getStatus()).thenReturn(HttpStatus.SC_OK);
    when(clientResponseContext.getHeaders()).thenReturn(
      new MultivaluedHashMap<>(ImmutableMap.of(
        HttpHeaders.CONTENT_ENCODING,
        MediaType.APPLICATION_JSON)));
    when(clientResponseContext.hasEntity()).thenReturn(false);
    return clientResponseContext;
  }
}
