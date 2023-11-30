package com.savdev.rest.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.springframework.stereotype.Component;


@Component
@ApplicationPath(JakartaWsConfiguration.APPLICATION_PATH)
public class JakartaWsConfiguration extends Application {
  public static final String APPLICATION_PATH = "/rest";
}
