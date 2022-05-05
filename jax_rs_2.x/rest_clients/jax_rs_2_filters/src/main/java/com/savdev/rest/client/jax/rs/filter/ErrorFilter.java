package com.savdev.rest.client.jax.rs.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.Response;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ErrorFilter implements ClientResponseFilter {

  final private ObjectMapper objectMapper;
  final private Consumer<RequestResponseInfo> errorConsumer;

  final private Predicate<ClientResponseContext> errorPredicate;

  private ErrorFilter(
    final ObjectMapper objectMapper,
    final Consumer<RequestResponseInfo> errorConsumer,
    final Predicate<ClientResponseContext> errorPredicate) {
    this.objectMapper = objectMapper;
    this.errorConsumer = errorConsumer;
    this.errorPredicate = errorPredicate;
  }

  /**
   * Considers each not successful response as an error
   */
  public static ErrorFilter instance(
    final ObjectMapper objectMapper,
    final Consumer<RequestResponseInfo> errorConsumer){
    return new ErrorFilter(
      objectMapper,
      errorConsumer,
      (responseContext) ->
        Sets.newHashSet(
            Response.Status.Family.CLIENT_ERROR,
            Response.Status.Family.SERVER_ERROR)
          .contains(Response.Status.Family.familyOf(responseContext.getStatus())));
  }

  /**
   * Allows to control, which exact response statuses are considered as errors
   * @param objectMapper
   * @param errorConsumer
   * @return
   */
  public static ErrorFilter instance(
    final ObjectMapper objectMapper,
    final Consumer<RequestResponseInfo> errorConsumer,
    final Predicate<ClientResponseContext> errorPredicate){
    return new ErrorFilter(objectMapper, errorConsumer, errorPredicate);
  }


  @Override
  public void filter(ClientRequestContext clientRequestContext, ClientResponseContext clientResponseContext) {
    if (errorPredicate.test(clientResponseContext)) {
      errorConsumer.accept(RequestResponseInfo.clientInstance(
        objectMapper, clientRequestContext, clientResponseContext));
    }
  }
}
