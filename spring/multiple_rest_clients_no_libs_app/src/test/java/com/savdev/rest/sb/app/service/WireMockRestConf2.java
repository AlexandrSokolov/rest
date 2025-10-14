package com.savdev.rest.sb.app.service;

import com.savdev.rest.sb.app.configs.rest.RestClient2Conf;
import jakarta.ws.rs.client.ClientRequestFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@Primary
public class WireMockRestConf2 extends RestClient2Conf {

  @Value("${wiremock.server.baseUrl}")
  private String wireMockUrl;

  @Override
  public String serverUrl2() {
    return wireMockUrl;
  }

  @Override
  public ClientRequestFilter authFilter() {
    return null;
  }
}
