package com.savdev.rest.sb.app.configs.rest;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class RestClient1Conf implements RestClientConfiguration {

  @Override
  public String serverUrl() {
    return "/fdsfs";
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
