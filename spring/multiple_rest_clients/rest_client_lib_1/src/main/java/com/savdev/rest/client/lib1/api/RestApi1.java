package com.savdev.rest.client.lib1.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static com.savdev.rest.client.lib1.api.RestApi1.BASE_URL;

@Path(BASE_URL)
@Produces(MediaType.APPLICATION_JSON)
public interface RestApi1 {

  String BASE_URL = "/api1";

  @GET
  String getStrValue();
}
