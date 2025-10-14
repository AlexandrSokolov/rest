package com.savdev.rest.sb.app.rest;

import com.savdev.rest.sb.app.rest.jackson.ObjectMapperProvider;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.ClientRequestFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

public abstract class BaseRestClientService<T> {

  private static final Logger logger = LogManager.getLogger();

  private final String serverUrl;
  private final ClientRequestFilter authFilter;

  private Client client;
  private T proxyRestApi;

  public BaseRestClientService(
    String serverUrl,
    ClientRequestFilter authFilter) {
    this.serverUrl = serverUrl;
    this.authFilter = authFilter;
  }

  public T proxyRestApi() {
    return proxyRestApi;
  }

  @PostConstruct
  public void init() {
    try {
      this.client = ClientBuilder.newClient();
      Optional.ofNullable(authFilter)
        .ifPresent(auth -> this.client.register(authFilter));
      client.register(ObjectMapperProvider.class);
      ResteasyWebTarget target = (ResteasyWebTarget) client.target(serverUrl);
      this.proxyRestApi = target.proxy(getParameterClass());
    } catch (Exception e){
      Optional.ofNullable(client).ifPresent(Client::close);
      throw new IllegalStateException("Could not create proxy for the rest interface: '"
        + Optional.ofNullable(getParameterClass())
        .map(Class::getName)
        .orElseThrow(() -> new IllegalStateException("Rest proxy interface cannot be null"))
        + "'. Reason: '" + e.getMessage()
        + "'. Cause exception: '" + e.getClass().getCanonicalName() + "'");
    }
    logger.debug(() -> "Rest client initialization complete for: '" + getParameterClass() + "'");
  }

  @PreDestroy
  public void destroy() {
    Optional.ofNullable(client).ifPresent(Client::close);
    logger.debug(() -> "Rest client de-initialization complete for: '" + getParameterClass() + "'");
  }

  private Class<T> getParameterClass() {
    return (Class<T>) ((ParameterizedType) getClass()
      .getGenericSuperclass())
      .getActualTypeArguments()[0];
  }
}
