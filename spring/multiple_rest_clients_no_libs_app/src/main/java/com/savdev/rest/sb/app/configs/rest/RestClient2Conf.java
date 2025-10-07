package com.savdev.rest.sb.app.configs.rest;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class RestClient2Conf implements RestClientConfiguration {

  @Override
  public String serverUrl() {
    return "/t2222tttt";
  }

  @Override
  public ClientRequestFilter authFilter() {
    return new ClientRequestFilter() {
      @Override
      public void filter(ClientRequestContext clientRequestContext) throws IOException {

      }
    };
  }
}
