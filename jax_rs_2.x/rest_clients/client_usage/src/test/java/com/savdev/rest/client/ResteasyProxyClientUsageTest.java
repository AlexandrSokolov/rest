package com.savdev.rest.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.test.commons.test.BaseTest;
import org.apache.commons.io.IOUtils;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient43Engine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

@WireMockTest
public class ResteasyProxyClientUsageTest extends BaseTest {

  public static final String BODY = "test body";
  public static final String ERROR = "Connection is still allocated";

  @BeforeEach
  public void init(WireMockRuntimeInfo wmRuntimeInfo) {
    WireMock wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(get(urlPathEqualTo(ClientUsageTestRestApi.BASE_URL))
      .willReturn(
        ok()
          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
          .withHeader(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.name())
          .withBody(BODY)));
  }

  /**
   * Client is reused
   *
   * Every Response object gets  closed
   * @param wmRuntimeInfo
   */
  @Test
  public void testResponseReleasedConnection(WireMockRuntimeInfo wmRuntimeInfo) {
    try (ResteasyProxyClient<ClientUsageTestRestApi> client = resteasyProxyClient(wmRuntimeInfo)){
      ClientUsageTestRestApi restApiProxy = client.proxy();
      try (Response response = restApiProxy.get()) {
        Assertions.assertEquals(BODY, response.readEntity(String.class));
      }
      try (Response response = restApiProxy.get()) {
        Assertions.assertEquals(BODY, response.readEntity(String.class));
      }
    }
  }

  /**
   * Client is reused
   *
   * Every Response and InputStream object gets closed
   *
   * @param wmRuntimeInfo
   */
  @Test
  public void testResponseAndInputStreamReleasedConnection(WireMockRuntimeInfo wmRuntimeInfo) throws IOException {
    try (ResteasyProxyClient<ClientUsageTestRestApi> client = resteasyProxyClient(wmRuntimeInfo)){
      ClientUsageTestRestApi restApiProxy = client.proxy();
      try (Response response = restApiProxy.get();
        InputStream is = response.readEntity(InputStream.class)) {
        Assertions.assertEquals(BODY, IOUtils.toString(is, StandardCharsets.UTF_8.name()));
      }
      try (Response response = restApiProxy.get();
           InputStream is = response.readEntity(InputStream.class)) {
        Assertions.assertEquals(BODY, IOUtils.toString(is, StandardCharsets.UTF_8.name()));
      }
    }
  }

  /**
   * Connection is not released
   *
   * @param wmRuntimeInfo
   */
  @Test
  public void testResponseNotReleasedConnection(WireMockRuntimeInfo wmRuntimeInfo) {
    Exception thrown = Assertions.assertThrows(Exception.class, () -> {
      ResteasyClient client = resteasyProxySingleClient(); //not closable in Java 8
      try {
        ResteasyWebTarget target = client.target(wmRuntimeInfo.getHttpBaseUrl());
        ClientUsageTestRestApi restApiProxy = target.proxy(ClientUsageTestRestApi.class);;
        Response notClosed = restApiProxy.get();
        notClosed = restApiProxy.get();
        Assertions.fail("Must throw an exception, connection is not released.");
      } finally {
        Optional.ofNullable(client).ifPresent(Client::close);
      }
    });
    Assertions.assertEquals(
      new IllegalStateException(ERROR).getMessage(),
      thrown.getCause().getMessage());
  }

  private ResteasyProxyClient<ClientUsageTestRestApi> resteasyProxyClient(
    WireMockRuntimeInfo wmRuntimeInfo) {
    return ResteasyProxyClient.builder(
        wmRuntimeInfo.getHttpBaseUrl(),
        ClientUsageTestRestApi.class)
      .build();
  }

  private ResteasyClient resteasyProxySingleClient() {
    HttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager();
    ApacheHttpClient43Engine engine = new ApacheHttpClient43Engine(
      HttpClients.custom().setConnectionManager(connectionManager).build());

    return  ((ResteasyClientBuilder) ClientBuilder.newBuilder()).httpEngine(engine).build();
  }
}
