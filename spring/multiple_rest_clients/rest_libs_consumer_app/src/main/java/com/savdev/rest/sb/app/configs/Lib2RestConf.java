package com.savdev.rest.sb.app.configs;

import com.savdev.rest.client.lib2.config.Lib2RestClientConfiguration;
import jakarta.ws.rs.client.ClientRequestFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static com.savdev.rest.sb.app.configs.AppConfiguration.SERVER_URL;

@Configuration
@ComponentScan({
  "com.savdev.rest.client.lib2.service",
})
public class Lib2RestConf implements Lib2RestClientConfiguration {

  @Override
  public String serverUrl() {
    return SERVER_URL;
  }

  @Override
  public ClientRequestFilter authFilter() {
    return null;
  }
}
