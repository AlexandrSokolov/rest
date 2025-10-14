package com.savdev.rest.sb.app.configs.rest;

import jakarta.ws.rs.client.ClientRequestFilter;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestClient2Conf {

  public static final String BASE_URL_WITH_CUSTOM_CONTEXT = "http://localhost:8080/some/custom/context";

  public String serverUrl2() {
    return BASE_URL_WITH_CUSTOM_CONTEXT;
  }

  public ClientRequestFilter authFilter() {
    return null;
  }
}
