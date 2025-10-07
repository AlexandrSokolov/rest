package com.savdev.rest.sb.app.rest.client2;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path(RestApi2.BASE_URL)
@Produces(MediaType.APPLICATION_JSON)
public interface RestApi2 {

  String BASE_URL = "/api2";

  @GET
  String getStrValue();
}
