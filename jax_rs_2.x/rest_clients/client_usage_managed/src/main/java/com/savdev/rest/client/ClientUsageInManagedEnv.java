package com.savdev.rest.client;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN) //to make client accessible by multiple threads
public class ClientUsageInManagedEnv {

  public static final String URL = "http://my.domain.com";

  private ResteasyProxyClient<ClientUsageRestApi> client;

  @PostConstruct
  public void init() {
    this.client = ResteasyProxyClientBuilder.instance(URL, ClientUsageRestApi.class)
      .build();
  }

  @Produces
  public ClientUsageRestApi produce() {
    return client.proxy();
  }

  @PreDestroy
  public void destroy() {
    client.close();
  }
}
