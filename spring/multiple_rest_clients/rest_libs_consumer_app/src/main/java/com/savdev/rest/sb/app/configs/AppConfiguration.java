package com.savdev.rest.sb.app.configs;

import com.savdev.rest.client.lib1.api.RestApi1;
import com.savdev.rest.client.lib1.service.RestApi1Service;
import com.savdev.rest.client.lib2.api.RestApi2;
import com.savdev.rest.client.lib2.service.RestApi2Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({
  "com.savdev.rest.client.lib1",
  "com.savdev.rest.client.lib2"
})
public class AppConfiguration {
}
