package com.savdev.rest.client.resteasy.httpclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(ResteasyHttpClientTestRestApi.BASE_URL)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ResteasyHttpClientTestRestApi {
  String BASE_URL = "/resteasy/httpclient";

  @GET
  String get();
}
