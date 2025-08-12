package com.savdev.rest.client.api;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static com.savdev.rest.client.api.CookieParamRestApi.PATH;

@Path(PATH)
@Produces(MediaType.APPLICATION_JSON)
public interface CookieParamRestApi {

  String PATH = "/cookie/param";

  String COOKIE_PARAM_NAME = "test_cookie";

  @GET
  String getCookieParam(@CookieParam(COOKIE_PARAM_NAME) String cookie);
}
