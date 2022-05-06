package com.savdev.rest.client.api;

import com.savdev.rest.client.dto.User;
import com.savdev.rest.client.dto.UserData;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(TestRestApi.BASE_URL)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface TestRestApi {
  String BASE_URL = "/test/proxy";

  @POST
  User create(UserData userData);
}
