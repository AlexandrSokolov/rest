package com.savdev.rest.client;

import javax.ws.rs.client.Client;

public class ResteasyProxyClient <T> implements AutoCloseable {

  private final Client client;

  private final T jaxRsInterfaceProxy;

  public static <T> ResteasyProxyClientBuilder<T> builder(
    final String domain,
    final Class<T> jaxRsInterfaceProxy) {
    return ResteasyProxyClientBuilder.instance(domain, jaxRsInterfaceProxy);
  }

  public ResteasyProxyClient(
    final Client client,
    T jaxRsInterfaceProxy) {
    this.client = client;
    this.jaxRsInterfaceProxy = jaxRsInterfaceProxy;
  }

  public T proxy() {
    return jaxRsInterfaceProxy;
  }

  @Override
  public void close() {
    client.close();
  }
}
