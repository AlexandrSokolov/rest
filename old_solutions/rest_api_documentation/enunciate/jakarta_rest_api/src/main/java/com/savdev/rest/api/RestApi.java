package com.savdev.rest.api;


import com.savdev.rest.api.model.Message;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * This rest api illustrates a demo rest service that returns message
 */
@Path(RestApi.SERVICE_REST_URL)
@Produces(MediaType.APPLICATION_JSON)
public interface RestApi {
  String SERVICE_REST_URL = "/api";

  /**
   * This method is used to get a message
   *
   * @return returns a message
   */
  @GET
  Message get();
}
