package com.savdev.rest.sb.app.configs;

import com.savdev.rest.client.lib1.config.Lib1RestClientConfiguration;
import jakarta.ws.rs.client.ClientRequestFilter;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Lib1RestConf implements Lib1RestClientConfiguration {

  @Override
  public String serverUrl() {
    return "/fdsfs";
  }

  @Override
  public ClientRequestFilter authFilter() {
    return null;
  }
}
