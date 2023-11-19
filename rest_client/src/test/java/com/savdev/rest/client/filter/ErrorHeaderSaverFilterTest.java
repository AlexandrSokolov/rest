package com.savdev.rest.client.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.google.common.collect.Sets;
import com.savdev.rest.commons.test.BaseTest;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.savdev.rest.client.filter.RequestResponseInfoTest.WIRE_MOCK_RESOURSES_FILES;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WireMockTest
public class ErrorHeaderSaverFilterTest extends BaseTest {

//  public static final String ERROR_URL = "/error";
//
//  public static final String ERROR_JSON = "responses/error.json";
//
//  @Test
//  public void testDefaultErrorPredicate(WireMockRuntimeInfo wmRuntimeInfo) {
//    WireMock wireMock = wmRuntimeInfo.getWireMock();
//    wireMock.register(get(ERROR_URL)
//      .willReturn(
//        aResponse().withStatus(Response.Status.CONFLICT.getStatusCode())
//          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
//          .withBodyFile(ERROR_JSON)));
//
//    //Mockito cannot spy lambdas, must define via functional interface:
//    Consumer<RequestResponseInfo> errorConsumer = Mockito.spy(
//      new Consumer<RequestResponseInfo>() {
//        @Override
//        public void accept(RequestResponseInfo requestResponseInfo) {
//          Assertions.assertEquals(Response.Status.CONFLICT.getStatusCode(), requestResponseInfo.getResponseStatus());
//          try {
//            Assertions.assertEquals(
//              IOUtils.toString(testInputStream(WIRE_MOCK_RESOURSES_FILES + ERROR_JSON),
//                StandardCharsets.UTF_8),
//              requestResponseInfo.getResponseBody().get());
//          } catch (IOException e) {
//            throw new IllegalStateException(e);
//          }
//        }
//      });
//
//    ErrorFilter logErrorFilter = Mockito.spy(ErrorFilter.instance(
//      new ObjectMapper(), errorConsumer));
//
//    Client client = ClientBuilder.newBuilder()
//      .build();
//    client.register(logErrorFilter);
//
//    WebTarget target = client.target(wmRuntimeInfo.getHttpBaseUrl() + ERROR_URL);
//    Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
//
//    verify(logErrorFilter, times(1))
//      .filter(
//        any(ClientRequestContext.class),
//        any(ClientResponseContext.class));
//
//    verify(errorConsumer, times(1))
//      .accept(any(RequestResponseInfo.class));
//
//    //make sure we still can read the entity:
//    Assertions.assertFalse(response.readEntity(String.class).isEmpty());
//  }
//
//  @Test
//  public void testCustomErrorPredicateConsumerSkipped(WireMockRuntimeInfo wmRuntimeInfo) {
//    WireMock wireMock = wmRuntimeInfo.getWireMock();
//    wireMock.register(get(ERROR_URL)
//      .willReturn(
//        aResponse().withStatus(Response.Status.NOT_FOUND.getStatusCode())
//          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
//          .withBodyFile(ERROR_JSON)));
//
//    //Mockito cannot spy lambdas, must define via functional interface:
//    Consumer<RequestResponseInfo> errorConsumer = Mockito.spy(
//      new Consumer<RequestResponseInfo>() {
//        @Override
//        public void accept(RequestResponseInfo requestResponseInfo) {
//          Assertions.fail("Must not be invoked for status = " + Response.Status.NOT_FOUND.getStatusCode());
//        }
//      });
//
//    //define a custom error predicate, NOT FOUND is not considered as an error in this case
//    //error consumer must not be invoked
//    ErrorFilter logErrorFilter = Mockito.spy(ErrorFilter.instance(
//      new ObjectMapper(),
//      errorConsumer,
//      (responseContext) ->
//        responseContext.getStatus() != Response.Status.NOT_FOUND.getStatusCode()
//        && Sets.newHashSet(
//            Response.Status.Family.CLIENT_ERROR,
//            Response.Status.Family.SERVER_ERROR)
//          .contains(Response.Status.Family.familyOf(responseContext.getStatus()))));
//
//    Client client = ClientBuilder.newBuilder()
//      .build();
//    client.register(logErrorFilter);
//
//    WebTarget target = client.target(wmRuntimeInfo.getHttpBaseUrl() + ERROR_URL);
//    Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
//
//    verify(logErrorFilter, times(1))
//      .filter(
//        any(ClientRequestContext.class),
//        any(ClientResponseContext.class));
//
//    //error consumer must not be invoked
//    verify(errorConsumer, times(0))
//      .accept(any(RequestResponseInfo.class));
//
//    //make sure we still can read the entity:
//    Assertions.assertFalse(response.readEntity(String.class).isEmpty());
//  }
//
//  @Test
//  public void testCustomErrorPredicateConsumerInvoked(WireMockRuntimeInfo wmRuntimeInfo) {
//    WireMock wireMock = wmRuntimeInfo.getWireMock();
//    wireMock.register(get(ERROR_URL)
//      .willReturn(
//        aResponse().withStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
//          .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
//          .withBodyFile(ERROR_JSON)));
//
//    //Mockito cannot spy lambdas, must define via functional interface:
//    Consumer<RequestResponseInfo> errorConsumer = Mockito.spy(
//      new Consumer<RequestResponseInfo>() {
//        @Override
//        public void accept(RequestResponseInfo requestResponseInfo) {
//          Assertions.assertEquals(
//            Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
//            requestResponseInfo.getResponseStatus());
//        }
//      });
//
//    //define a custom error predicate, NOT FOUND is not considered as an error in this case
//    //error consumer is invoked, cause another error status is returned
//    ErrorFilter logErrorFilter = Mockito.spy(ErrorFilter.instance(
//      new ObjectMapper(),
//      errorConsumer,
//      (responseContext) ->
//        responseContext.getStatus() != Response.Status.NOT_FOUND.getStatusCode()
//          && Sets.newHashSet(
//            Response.Status.Family.CLIENT_ERROR,
//            Response.Status.Family.SERVER_ERROR)
//          .contains(Response.Status.Family.familyOf(responseContext.getStatus()))));
//
//    Client client = ClientBuilder.newBuilder()
//      .build();
//    client.register(logErrorFilter);
//
//    WebTarget target = client.target(wmRuntimeInfo.getHttpBaseUrl() + ERROR_URL);
//    Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
//
//    verify(logErrorFilter, times(1))
//      .filter(
//        any(ClientRequestContext.class),
//        any(ClientResponseContext.class));
//
//    verify(errorConsumer, times(1))
//      .accept(any(RequestResponseInfo.class));
//
//    //make sure we still can read the entity:
//    Assertions.assertFalse(response.readEntity(String.class).isEmpty());
//  }
}
