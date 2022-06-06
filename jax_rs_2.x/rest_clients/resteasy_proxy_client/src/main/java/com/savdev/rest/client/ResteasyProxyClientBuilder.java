package com.savdev.rest.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savdev.rest.client.jackson.JacksonObjectMapperProvider;
import com.savdev.rest.client.jax.rs.filter.ErrorFilter;
import com.savdev.rest.client.jax.rs.filter.LogDebugFilter;
import org.apache.logging.log4j.LogManager;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

public class ResteasyProxyClientBuilder<T> {

  String domain;
  ObjectMapper objectMapper;
  ErrorFilter errorFilter;
  Class<T> jaxRsInterface;

  Collection<ClientRequestFilter> clientRequestFilters = new LinkedList<>();
  Collection<ClientResponseFilter> clientResponseFilters = new LinkedList<>();

  public static <T> ResteasyProxyClientBuilder<T> instance(
    String domain,
    Class<T> jaxRsInterface) {
    ResteasyProxyClientBuilder<T> builder = new ResteasyProxyClientBuilder<>();
    builder.domain = domain;
    builder.jaxRsInterface = jaxRsInterface;
    builder.objectMapper = new ObjectMapper();
    return builder;
  }

  public ResteasyProxyClientBuilder<T> objectMapper(final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    return this;
  }

  public ResteasyProxyClientBuilder<T> errorFilter(final ErrorFilter errorFilter) {
    this.errorFilter = errorFilter;
    return this;
  }

  public ResteasyProxyClientBuilder<T> clientRequestFilters(final ClientRequestFilter ...clientFilters) {
    clientRequestFilters.addAll(Arrays.asList(clientFilters));
    return this;
  }

  public ResteasyProxyClientBuilder<T> clientResponseFilters(final ClientResponseFilter ...clientFilters) {
    clientResponseFilters.addAll(Arrays.asList(clientFilters));
    return this;
  }

  public ResteasyProxyClient<T> build() {
    Client client = null;
    try {
      client = ClientBuilder.newClient();
      client.register(new JacksonObjectMapperProvider(objectMapper));
      client.register(LogDebugFilter.instance(jaxRsInterface, objectMapper));
      if (this.errorFilter == null) {
        //default error filter: log the request and response for all 4xx and 5xx responses
        errorFilter = ErrorFilter.instance(
          objectMapper,
          (rrInfo) -> LogManager.getLogger(jaxRsInterface).error(rrInfo));
      }
      client.register(errorFilter);
      clientRequestFilters.forEach(client::register);
      clientResponseFilters.forEach(client::register);
      ResteasyWebTarget target = (ResteasyWebTarget) client.target(domain);
      T jaxRsInterfaceProxy = target.proxy(jaxRsInterface);
      return new ResteasyProxyClient<>(client, jaxRsInterfaceProxy);
    } catch (Exception e){
      Optional.ofNullable(client).ifPresent(Client::close);
      throw new IllegalStateException("Could not create proxy for the rest interface: '"
        + Optional.ofNullable(jaxRsInterface).orElseThrow(()
          -> new IllegalStateException("Rest proxy interface cannot be null"))
        .getName() + "'. Reason: '" + e.getMessage()
        + "'. Exception type: " + e.getClass().getCanonicalName());
    }
  }
}
