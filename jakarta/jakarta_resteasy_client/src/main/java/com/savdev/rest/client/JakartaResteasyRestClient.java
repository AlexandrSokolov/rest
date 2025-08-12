package com.savdev.rest.client;

import com.savdev.rest.api.MultiPartFileUploadingApi;
import com.savdev.rest.api.RestCrudApi;
import com.savdev.rest.jackson.CustomObjectMapperProvider;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;

public class JakartaResteasyRestClient implements Closeable {

  final Client client = ClientBuilder.newClient();
  final ResteasyWebTarget target;

  public JakartaResteasyRestClient(String restUrl) {
    client.register(CustomObjectMapperProvider.class);
    this.target = (ResteasyWebTarget) client.target(restUrl);

  }

  public RestCrudApi restCrudApiProxy() {
    return target.proxy(RestCrudApi.class);
  }

  public MultiPartFileUploadingApi restMultiPartFileUploadingApiProxy() {
    return target.proxy(MultiPartFileUploadingApi.class);
  }

  @Override
  public void close() throws IOException {
    Optional.ofNullable(client).ifPresent(Client::close);
  }
}
