package com.savdev.rest.sb.app.rest.client1;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path(RestApi1.BASE_URL)
@Produces(MediaType.APPLICATION_JSON)
public interface RestApi1 {

  String BASE_URL = "/api1";

  @GET
  String getStrValue();
}
