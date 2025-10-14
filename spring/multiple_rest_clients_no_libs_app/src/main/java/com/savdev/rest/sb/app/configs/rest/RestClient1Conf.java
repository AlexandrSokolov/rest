package com.savdev.rest.sb.app.configs.rest;

import jakarta.ws.rs.client.ClientRequestFilter;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestClient1Conf {

  public static final String  BASE_URL = "http://localhost:8080";

  public String serverUrl1() {
    return BASE_URL;
  }

  public ClientRequestFilter authFilter() {
    return clientRequestContext -> {

    };
  }
}
