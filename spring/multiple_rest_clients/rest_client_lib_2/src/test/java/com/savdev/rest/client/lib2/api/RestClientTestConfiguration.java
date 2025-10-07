package com.savdev.rest.client.lib2.api;

import com.savdev.rest.client.lib2.config.Lib2RestClientConfiguration;
import jakarta.ws.rs.client.ClientRequestFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.savdev.rest.client.lib2")
public class RestClientTestConfiguration implements Lib2RestClientConfiguration {


  @Override
  public String serverUrl() {
    return "/todo";
  }

  @Override
  public ClientRequestFilter authFilter() {
    return null;
  }
}
