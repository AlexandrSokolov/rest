package com.savdev.rest.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savdev.rest.client.jackson.JacksonObjectMapperProvider;
import com.savdev.rest.client.jax.rs.filter.ErrorFilter;
import com.savdev.rest.client.jax.rs.filter.LogDebugFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class ResteasyProxyClient {

  private final Logger logger = LogManager.getLogger(ResteasyProxyClient.class);

  public <T> T proxy(
    final String domain,
    final ObjectMapper objectMapper,
    final Class<T> jaxRsInterface) {

    try {
      Client client = ClientBuilder.newClient();

      client.register(new JacksonObjectMapperProvider(objectMapper));
      client.register(LogDebugFilter.instance(ResteasyProxyClient.class, objectMapper));
      client.register(ErrorFilter.instance(
        objectMapper,
        logger::error)); //log the request and response

      ResteasyWebTarget target = (ResteasyWebTarget) client.target(domain);

      return target.proxy(jaxRsInterface);
    } catch (Exception e){
      throw new IllegalStateException("Could not create proxy for the rest interface: '"
        + jaxRsInterface.getName() + "'. Reason: '" + e.getMessage()
        + "'. Exception type: " + e.getClass().getCanonicalName());
    }
  }
}
