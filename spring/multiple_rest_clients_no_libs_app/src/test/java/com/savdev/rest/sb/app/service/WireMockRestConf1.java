package com.savdev.rest.sb.app.service;

import com.savdev.rest.sb.app.configs.rest.RestClient1Conf;
import jakarta.ws.rs.client.ClientRequestFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@Primary
public class WireMockRestConf1 extends RestClient1Conf {

  @Value("${wiremock.server.baseUrl}")
  private String wireMockUrl;

  @Override
  public String serverUrl1() {
    return wireMockUrl;
  }

  @Override
  public ClientRequestFilter authFilter() {
    return null;
  }
}
