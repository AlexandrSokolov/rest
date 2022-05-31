package com.savdev.rest.client.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import static com.savdev.rest.client.api.QueryParamRestApi.PATH;

@Path(PATH)
@Produces(MediaType.APPLICATION_JSON)
public interface QueryParamRestApi {

  String PATH = "/query/param";

  String QUERY_PARAM_NAME = "name";
  String QUERY_PARAM_LIMIT = "limit";

  @GET
  String getQueryParam(@QueryParam(QUERY_PARAM_NAME) String param, @QueryParam(QUERY_PARAM_LIMIT) int limit);
}
