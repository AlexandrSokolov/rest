package com.savdev.rest.commons;

import jakarta.ws.rs.client.Client;

public class RestClient <T> implements AutoCloseable {

  private final Client client;

  private final T restApi;

  public static <T> RestClientBuilder<T> builder(
    final String serverUrl,
    final Class<T> restApi) {
    return RestClientBuilder.instance(
      serverUrl, restApi);
  }

  public T restApi() {
    return this.restApi;
  }

  RestClient(
    final Client client,
    T restApi) {
    this.client = client;
    this.restApi = restApi;
  }

  @Override
  public void close() {
    client.close();
  }
}
