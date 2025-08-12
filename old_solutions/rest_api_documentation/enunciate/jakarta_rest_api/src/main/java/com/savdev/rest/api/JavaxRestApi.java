package com.savdev.rest.api;


import com.savdev.rest.api.model.Message;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * This rest api illustrates a demo rest service that returns message
 */
@Path(JavaxRestApi.SERVICE_REST_URL)
@Produces(MediaType.APPLICATION_JSON)
public interface JavaxRestApi {
  String SERVICE_REST_URL = "/javaxapi";

  /**
   * This method is used to get a message
   *
   * @return returns a message
   */
  @GET
  Message get();
}
