package com.savdev.rest.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savdev.rest.commons.configs.ObjectMapperFactory;
import com.savdev.rest.commons.configs.ObjectMapperProvider;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.ClientRequestFilter;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class RestClientBuilder<T> {
  Client client;

  String serverUrl;

  Class<T> restApi;
  ObjectMapper objectMapper;

  List<ClientRequestFilter> clientRequestFilters = new LinkedList<>();


  public static <T> RestClientBuilder<T> instance(
    String serverUrl,
    Class<T> restApi) {
    RestClientBuilder<T> builder = new RestClientBuilder<>();

    builder.serverUrl = serverUrl;
    builder.restApi = restApi;

    //defaults:
    builder.client = ClientBuilder.newClient();
    builder.objectMapper = ObjectMapperFactory.instance();

    return builder;
  }

  public RestClientBuilder<T> withClient(final Client client) {
    this.client = client;
    return this;
  }

  public RestClientBuilder<T> withAuth(final ClientRequestFilter authFilter) {
    clientRequestFilters.add(authFilter);
    return this;
  }

  public RestClientBuilder<T> withObjectMapper(final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    return this;
  }

  public RestClient<T> build() {
    try {
      client.register(new ObjectMapperProvider(this.objectMapper));
      ResteasyWebTarget target = (ResteasyWebTarget) client.target(serverUrl);
      return new RestClient<>(
        client,
        target.proxy(restApi));
    } catch (Exception e){
      Optional.ofNullable(client).ifPresent(Client::close);
      throw new IllegalStateException("Could not create proxy for the rest interface: '"
        + Optional.ofNullable(restApi)
          .map(Class::getName)
          .orElseThrow(() -> new IllegalStateException("Rest proxy interface cannot be null"))
        + "'. Reason: '" + e.getMessage()
        + "'. Cause exception: '" + e.getClass().getCanonicalName() + "'");
    }
  }


}
