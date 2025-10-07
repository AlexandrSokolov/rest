package com.savdev.rest.commons;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ws.rs.client.ClientRequestFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

public abstract class BaseRestClientService<T> {

  private static final Logger logger = LogManager.getLogger();

  private final String serverUrl;
  private final ClientRequestFilter authFilter;

  private RestClient<T> restClient;

  public BaseRestClientService(
    RestClientConfiguration restClientConfiguration) {
    this.serverUrl = restClientConfiguration.serverUrl();
    this.authFilter = restClientConfiguration.authFilter();
  }

  public T proxyRestApi() {
    return Optional.ofNullable(this.restClient)
      .map(RestClient::restApi)
      .orElseThrow(() -> new  IllegalStateException(
        "Not initialized rest client for '" + getParameterClass()
          + "'. Run `init()` method to initialize it."));
  }

  @PostConstruct
  public void init() {
    restClient = RestClientBuilder.instance(this.serverUrl, getParameterClass())
      .withAuth(authFilter)
      .build();
    logger.debug(() -> "Rest client initialization complete for: '" + getParameterClass() + "'");
  }

  @PreDestroy
  public void destroy() {
    Optional.ofNullable(restClient).ifPresent(RestClient::close);
    logger.debug(() -> "Rest client de-initialization complete for: '" + getParameterClass() + "'");
  }

  private Class<T> getParameterClass() {
    return (Class<T>) ((ParameterizedType) getClass()
      .getGenericSuperclass())
      .getActualTypeArguments()[0];
  }
}
