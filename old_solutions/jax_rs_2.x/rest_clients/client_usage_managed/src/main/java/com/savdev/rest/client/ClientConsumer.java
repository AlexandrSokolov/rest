package com.savdev.rest.client;

import javax.inject.Inject;

public class ClientConsumer {

  @Inject
  ClientUsageRestApi restApi;

  public void consumeRestClient() {
    String response = restApi.get().readEntity(String.class);
    System.out.println(response);
  }
}
