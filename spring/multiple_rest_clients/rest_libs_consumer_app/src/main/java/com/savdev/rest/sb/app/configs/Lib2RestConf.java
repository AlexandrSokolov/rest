package com.savdev.rest.sb.app.configs;

import com.savdev.rest.client.lib2.config.Lib2RestClientConfiguration;
import jakarta.ws.rs.client.ClientRequestFilter;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Lib2RestConf implements Lib2RestClientConfiguration {

  @Override
  public String serverUrl() {
    return "/t2222tttt";
  }

  @Override
  public ClientRequestFilter authFilter() {
    return null;
  }
}
