package com.savdev.rest.client.lib2.api;

import com.savdev.rest.client.lib2.config.Lib2RestClientConfiguration;
import jakarta.ws.rs.client.ClientRequestFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.savdev.rest.client.lib2")
public class WireMockRestClientTestConfiguration implements Lib2RestClientConfiguration {

  @Value("${wiremock.server.baseUrl}")
  private String wireMockUrl;

  @Override
  public String serverUrl() {
    return wireMockUrl;
  }

  @Override
  public ClientRequestFilter authFilter() {
    return null;
  }
}
