package com.savdev.rest.client.jax.rs.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.test.commons.test.BaseTest;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.savdev.rest.client.jax.rs.filter.RequestResponseInfoTest.USER_JSON;
import static com.savdev.rest.client.jax.rs.filter.RequestResponseInfoTest.WIRE_MOCK_RESOURSES_FILES;

public class RequestResponseInfoGzipTest extends BaseTest {

  public static final String GZIP_URL = "/gzip";

  @RegisterExtension
  WireMockExtension withGzipTransformer = WireMockExtension.newInstance()
    .options(wireMockConfig().dynamicPort().extensions(GZipTransformer.class))
    .build();

  @Test
  public void testGzipResponse() throws IOException {
    WireMock wireMock = withGzipTransformer.getRuntimeInfo().getWireMock();
    wireMock.register(get(GZIP_URL)
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
          .withBodyFile(USER_JSON)
          .withTransformers(GZipTransformer.NAME)
      ));

    TestClientResponseFilter f = TestClientResponseFilter.instance(new ObjectMapper());

    Assertions.assertNull(f.getRequestResponseInfo());

    Client client = ClientBuilder.newBuilder()
      .build();
    client.register(f);

    WebTarget target = client.target(withGzipTransformer.getRuntimeInfo().getHttpBaseUrl() + GZIP_URL);
    try (Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get()) {
      //make sure response looks as a plain text
      Assertions.assertEquals(
        IOUtils.toString(testInputStream(WIRE_MOCK_RESOURSES_FILES + USER_JSON),
          StandardCharsets.UTF_8),
        f.getRequestResponseInfo().getResponseBody()
          .orElseThrow(() -> new IllegalStateException("Response cannot be empty")));

      //make sure we still can read the entity:
      Assertions.assertFalse(response.readEntity(String.class).isEmpty());
    }
  }
}
