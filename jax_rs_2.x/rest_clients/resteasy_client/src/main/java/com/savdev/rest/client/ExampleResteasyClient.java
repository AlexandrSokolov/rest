package com.savdev.rest.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savdev.rest.client.jackson.JacksonObjectMapperProvider;
import com.savdev.rest.client.jax.rs.filter.ErrorFilter;
import com.savdev.rest.client.jax.rs.filter.LogDebugFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class ExampleResteasyClient {

  private final Logger logger = LogManager.getLogger(ExampleResteasyClient.class);

  public <T> void handleRequest(
    final String url,
    final String httpMethod,
    final MultivaluedMap<String, Object> headers,
    final Map.Entry<String, Object> queryParameter,
    final MediaType requestMediaType,
    final MediaType acceptMediaType,
    final ObjectMapper objectMapper,
    final Entity<T> requestBody,
    final Consumer<Response> responseHandler) {

    ResteasyClient client = null;

    try {
      client = (ResteasyClient) ClientBuilder.newBuilder().build();

      //register object mapper
      client.register(new JacksonObjectMapperProvider(objectMapper));
      client.register(LogDebugFilter.instance(ExampleResteasyClient.class, objectMapper));
      client.register(ErrorFilter.instance(
        objectMapper,
        logger::error)); //log the request and response as error

      ResteasyWebTarget target = Optional.ofNullable(queryParameter).isPresent() ?
        client.target(url)
          .queryParam(queryParameter.getKey(), queryParameter.getValue()) :
        client.target(url);

      Invocation.Builder invocationBuilder = target
        .request(requestMediaType)
        .headers(headers)
        .accept(acceptMediaType);
      try (Response response = getResponse(httpMethod, invocationBuilder, requestBody)) {
        responseHandler.accept(response);
      }
    } finally {
      Optional.ofNullable(client).ifPresent(Client::close);
    }
  }

  private <T> Response getResponse(
    final String httpMethod,
    final Invocation.Builder invocationBuilder,
    final Entity<T> requestBody) {
    switch (httpMethod) {
      case HttpMethod.GET:
        return invocationBuilder.get();
      case HttpMethod.HEAD:
        return invocationBuilder.head();
      case HttpMethod.DELETE:
        return invocationBuilder.delete();
      case HttpMethod.POST:
        return invocationBuilder.post(requestBody);
      case HttpMethod.PUT:
        return invocationBuilder.put(requestBody);
      case HttpMethod.PATCH:
        return invocationBuilder.method(HttpMethod.PATCH, requestBody);
      default:
        throw new IllegalStateException("Not supported http method: " + httpMethod);
    }
  }
}
