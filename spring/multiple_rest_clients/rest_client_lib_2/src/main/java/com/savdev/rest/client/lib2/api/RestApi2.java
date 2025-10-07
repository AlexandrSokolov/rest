package com.savdev.rest.client.lib2.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static com.savdev.rest.client.lib2.api.RestApi2.BASE_URL;

@Path(BASE_URL)
@Produces(MediaType.APPLICATION_JSON)
public interface RestApi2 {

  String BASE_URL = "/api2";

  @GET
  String getStrValue();
}
