package com.savdev.rest.client;

import javax.ws.rs.client.Client;

public class CsRestProxyClient<T> implements AutoCloseable {

  private final Client client;

  private final T jaxRsInterfaceProxy;

  public static <T> CsRestProxyClientBuilder<T> builder(
    final String domain,
    final Class<T> jaxRsInterfaceProxy) {
    return CsRestProxyClientBuilder.instance(domain, jaxRsInterfaceProxy);
  }

  public CsRestProxyClient(
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
