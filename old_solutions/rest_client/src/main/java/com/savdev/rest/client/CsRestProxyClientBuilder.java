package com.savdev.rest.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savdev.rest.client.filter.ErrorHeaderSaverFilter;
import com.savdev.rest.client.filter.LogDebugFilter;
import com.savdev.rest.client.jackson.JacksonObjectMapperProvider;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

import static com.savdev.rest.client.RestClientDefaults.CONNECTION_POOL;
import static com.savdev.rest.client.RestClientDefaults.DEFAULT_DEBUG_LOG_ENTRY_SIZE;

public class CsRestProxyClientBuilder<T> {

  Client client;
  String domain;
  ObjectMapper objectMapper;
  Class<T> jaxRsInterface;

  Collection<ClientRequestFilter> clientRequestFilters = new LinkedList<>();
  Collection<ClientResponseFilter> clientResponseFilters = new LinkedList<>();

  Class<?  extends Annotation> contextRoot;

  int debugLogEntrySize = DEFAULT_DEBUG_LOG_ENTRY_SIZE;

  public static <T> CsRestProxyClientBuilder<T> instance(
    String domain,
    Class<T> jaxRsInterface) {
    CsRestProxyClientBuilder<T> builder = new CsRestProxyClientBuilder<>();
    builder.domain = domain;
    builder.jaxRsInterface = jaxRsInterface;
    builder.objectMapper = RestClientDefaults.objectMapper();
    return builder;
  }

  public CsRestProxyClientBuilder<T> client(final Client client) {
    this.client = client;
    return this;
  }

  public CsRestProxyClientBuilder<T> objectMapper(final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    return this;
  }

  public CsRestProxyClientBuilder<T> clientRequestFilters(final ClientRequestFilter ...clientFilters) {
    clientRequestFilters.addAll(Arrays.asList(clientFilters));
    return this;
  }

  public CsRestProxyClientBuilder<T> clientResponseFilters(final ClientResponseFilter ...clientFilters) {
    clientResponseFilters.addAll(Arrays.asList(clientFilters));
    return this;
  }

  public CsRestProxyClientBuilder<T> contextRoot(Class<?  extends Annotation> contextRoot) {
    this.contextRoot = contextRoot;
    return this;
  }

  public CsRestProxyClientBuilder<T> debugLogEntrySize(int debugLogEntrySize) {
    this.debugLogEntrySize = debugLogEntrySize;
    return this;
  }



  public CsRestProxyClient<T> build() {
    try {
      if (client == null) {
        client = new ResteasyClientBuilder().connectionPoolSize(CONNECTION_POOL).build();
      }
      client.register(new JacksonObjectMapperProvider(objectMapper));
      client.register(LogDebugFilter.instance(jaxRsInterface, objectMapper, debugLogEntrySize));
      client.register(ErrorHeaderSaverFilter.instance(objectMapper, debugLogEntrySize));
      clientRequestFilters.forEach(client::register);
      clientResponseFilters.forEach(client::register);
      ResteasyWebTarget target = (ResteasyWebTarget) client.target(
        domain + contextRoot().orElse(""));
      T jaxRsInterfaceProxy = target.proxy(jaxRsInterface);
      return new CsRestProxyClient<>(client, jaxRsInterfaceProxy);
    } catch (Exception e){
      Optional.ofNullable(client).ifPresent(Client::close);
      throw new IllegalStateException("Could not create proxy for the rest interface: '"
        + Optional.ofNullable(jaxRsInterface).orElseThrow(()
          -> new IllegalStateException("Rest proxy interface cannot be null"))
        .getName() + "'. Reason: '" + e.getMessage()
        + "'. Exception type: " + e.getClass().getCanonicalName());
    }
  }

  private Optional<String> contextRoot() {
    return Optional.ofNullable(contextRoot)
      .map(contextRootAnnotation -> {
        try {
          Method m = contextRootAnnotation.getMethod("value");
          return (String) m.invoke(Optional.ofNullable(
              jaxRsInterface.getAnnotation(contextRootAnnotation))
            .orElseThrow(() -> new IllegalStateException(
              "Context root annotation of " + contextRootAnnotation.getName()
                + " type expected, but not applied to the rest api interface. " +
                "Either remove the annotation configuration in the builder " +
                "or add the annotation on the rest interface")));
        } catch (ReflectiveOperationException e) {
          throw new IllegalStateException(e);
        }});
  }
}
