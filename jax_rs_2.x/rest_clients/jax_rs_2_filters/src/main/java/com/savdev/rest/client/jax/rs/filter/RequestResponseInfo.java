package com.savdev.rest.client.jax.rs.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static com.savdev.rest.client.jax.rs.filter.RequestResponseUtils.extractResponseBody;
import static java.util.stream.Collectors.toMap;

public class RequestResponseInfo {

  public static final String NO_REQUEST_BODY = "NO REQUEST BODY";
  public static final String NO_RESPONSE_BODY = "NO RESPONSE BODY";

  public static RequestResponseInfo clientInstance(
    final ObjectMapper objectMapper,
    final ClientRequestContext requestContext,
    final ClientResponseContext responseContext){

    return new RequestResponseInfoBuilder()
      .objectMapper(objectMapper)
      .url(requestContext.getUri())
      .httpMethod(requestContext.getMethod().toUpperCase(Locale.ENGLISH))
      .requestHeaders(requestContext.getStringHeaders())
      .requestBody(requestContext.getEntity())
      .responseStatus(responseContext.getStatus())
      .responseHeaders(responseContext.getHeaders())
      .responseBody(extractResponseBody(responseContext))
      .build();
  }

  private ObjectMapper objectMapper;

  private URI url;

  private String httpMethod;

  private MultivaluedMap<String, String> requestHeaders;

  private Optional<?> requestBody;

  private int responseStatus;

  private MultivaluedMap<String, String> responseHeaders;

  private Optional<String> responseBody;

  RequestResponseInfo(
    ObjectMapper objectMapper,
    URI url,
    String httpMethod,
    MultivaluedMap<String, String> requestHeaders,
    Object requestBody,
    int responseStatus,
    MultivaluedMap<String, String> responseHeaders,
    String responseBody) {
    this.objectMapper = objectMapper;
    this.url = url;
    this.httpMethod = httpMethod;
    this.requestHeaders = requestHeaders;
    this.requestBody = Optional.ofNullable(
      requestBody instanceof Form ? formToMap((Form) requestBody) : requestBody);
    this.responseStatus = responseStatus;
    this.responseHeaders = responseHeaders;
    this.responseBody = Optional.ofNullable(responseBody);
  }

  @Override
  public String toString() {
    return new StringJoiner(",\n\t", RequestResponseInfo.class.getSimpleName() + " {", "\n}")
      .add("\n\tURL = " + url)
      .add("HTTP Method = '" + httpMethod + "'")
      .add("Request Headers = " + requestHeaders.entrySet().stream()
        .filter(entry -> !HttpHeaders.AUTHORIZATION.equals(entry.getKey()))
        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue)))
      .add("Request Body = " +
        ( requestBody.isPresent() ?
          stringify(
            this.objectMapper,
            this.requestBody.get())
          : NO_REQUEST_BODY ))
      .add("Response Status=" + responseStatus)
      .add("Response Headers=" + responseHeaders
        .entrySet().stream()
        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue)))
      .add("Response Body=" + responseBody.orElse(NO_RESPONSE_BODY))
      .toString();
  }

  public URI getUrl() {
    return url;
  }

  public String getHttpMethod() {
    return httpMethod;
  }

  public MultivaluedMap<String, String> getRequestHeaders() {
    return requestHeaders;
  }

  public Optional<?> getRequestBody() {
    return requestBody;
  }

  public int getResponseStatus() {
    return responseStatus;
  }

  public MultivaluedMap<String, String> getResponseHeaders() {
    return responseHeaders;
  }

  public Optional<String> getResponseBody() {
    return responseBody;
  }

  private static Map<String, String> formToMap(Form form){
    Map<String, String> formAsMap = new LinkedHashMap<>();
    formAsMap.put("original form",
      form.asMap().entrySet().stream().map(key2list ->
          key2list.getKey() + "=" + String.join(",", key2list.getValue()))
        .collect(Collectors.joining("&")));
    formAsMap.put("url encoded",
      form.asMap().entrySet().stream().map(key2list -> {
          try {
            return key2list.getKey() + "=" + URLEncoder.encode(
              String.join(",", key2list.getValue()),
              StandardCharsets.UTF_8.toString());
          } catch (Exception e) {
            throw new IllegalStateException(e);
          }})
        .collect(Collectors.joining("&")));
    return formAsMap;
  }

  private String stringify(
    final ObjectMapper objectMapper,
    final Object object){
    try {
      return objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValueAsString(object)
        .replaceAll("^\"|\"$", "");
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Could not stringify object: "
        + object.getClass().getCanonicalName()
        + ". Reason: " + e.getMessage(), e);
    }
  }
}
