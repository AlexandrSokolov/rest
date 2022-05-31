package com.savdev.rest.client.api;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static com.savdev.rest.client.api.HeaderParamRestApi.PATH;

@Path(PATH)
@Produces(MediaType.APPLICATION_JSON)
public interface HeaderParamRestApi {

  String PATH = "/header/param";

  String HEADER_PARAM = "test_header";

  @GET
  String getHeaderParam(@HeaderParam(HEADER_PARAM) String header);
}
