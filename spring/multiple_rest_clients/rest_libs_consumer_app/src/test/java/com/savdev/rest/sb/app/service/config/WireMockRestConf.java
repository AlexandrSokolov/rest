package com.savdev.rest.sb.app.service.config;

import com.savdev.rest.client.lib1.config.Lib1RestClientConfiguration;
import com.savdev.rest.client.lib2.config.Lib2RestClientConfiguration;
import jakarta.ws.rs.client.ClientRequestFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@Primary
public class WireMockRestConf implements Lib1RestClientConfiguration, Lib2RestClientConfiguration {

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
