package com.savdev.rest.client.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import static com.savdev.rest.client.api.PathParamRestApi.PATH;

@Path(PATH)
@Produces(MediaType.APPLICATION_JSON)
public interface PathParamRestApi {

  String PATH = "/path/param";
  String PATH_PARAM_NAME = "name";

  @GET
  @Path("{" + PATH_PARAM_NAME + "}")
  String getPathParam(@PathParam(PATH_PARAM_NAME) String name);
}
