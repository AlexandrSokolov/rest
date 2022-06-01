package com.savdev.rest.client.api;

import org.jboss.resteasy.annotations.Body;
import org.jboss.resteasy.annotations.ResponseObject;
import org.jboss.resteasy.annotations.Status;
import org.jboss.resteasy.client.jaxrs.internal.ClientResponse;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.HttpHeaders;

@ResponseObject
public interface ResponseObjectApi {

  @Status
  int status();

  @Body
  String body();

  @HeaderParam(HttpHeaders.CONTENT_TYPE)
  String contentType();

  ClientResponse response();
}
