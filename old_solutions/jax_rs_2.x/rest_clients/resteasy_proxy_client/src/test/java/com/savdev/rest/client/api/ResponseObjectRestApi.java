package com.savdev.rest.client.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import static com.savdev.rest.client.api.ResponseObjectRestApi.PATH;

@Path(PATH)
public interface ResponseObjectRestApi {

  String PATH = "/full/response";

  @GET
  ResponseObjectApi get();
}
