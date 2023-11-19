package com.savdev.rest.client.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedMap;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.savdev.rest.client.filter.RequestResponseUtils.extractResponseBody;

public class RequestResponseInfo {
  public static final String NO_REQUEST_BODY = "NO REQUEST BODY";
  public static final String NO_RESPONSE_BODY = "\"NO RESPONSE BODY\"";

  public static RequestResponseInfo clientInstance(
    final int debugLogEntrySize,
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
      .responseBody(extractResponseBody(debugLogEntrySize, responseContext))
      .build();
  }

  @JsonIgnore
  final private ObjectMapper objectMapper;

  @JsonProperty("URL")
  final private URI url;

  @JsonProperty("HTTP Method")
  final private String httpMethod;

  @JsonProperty("Request Headers")
  final private MultivaluedMap<String, String> requestHeaders;

  @JsonProperty("Request Body")
  final private Object requestBody;

  @JsonProperty("Response Status")
  final private int responseStatus;

  @JsonProperty("Response Headers")
  final private MultivaluedMap<String, String> responseHeaders;

  @JsonProperty("Response Body")
  private Object responseBody;

  RequestResponseInfo(
    ObjectMapper objectMapper,
    URI url,
    String httpMethod,
    MultivaluedMap<String, String> requestHeaders,
    Object requestBody,
    int responseStatus,
    MultivaluedMap<String, String> responseHeaders,
    Object responseBody) {
    this.objectMapper = objectMapper;
    this.url = url;
    this.httpMethod = httpMethod;
    this.requestHeaders = requestHeaders;
    this.requestBody = Optional.ofNullable(
      requestBody instanceof Form ? formToMap((Form) requestBody) : requestBody)
      .orElse(NO_REQUEST_BODY);
    this.responseStatus = responseStatus;
    this.responseHeaders = responseHeaders;
    try {
      this.responseBody = objectMapper.readTree(
        Optional.ofNullable(responseBody)
          .orElse(NO_RESPONSE_BODY)
          .toString());
    } catch (JsonProcessingException e) {
      //the size of DEFAULT_MAX_ENTITY_SIZE buffer is too small for json, or the string is not json
      this.responseBody = Optional.ofNullable(responseBody).orElse(NO_RESPONSE_BODY);
    }
  }

  @Override
  public String toString() {
    return stringify(this.objectMapper, this);
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

  public Object getRequestBody() {
    return requestBody;
  }

  public int getResponseStatus() {
    return responseStatus;
  }

  public MultivaluedMap<String, String> getResponseHeaders() {
    return responseHeaders;
  }

  public Object getResponseBody() {
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

  public String stringify(
    final ObjectMapper objectMapper,
    final Object object){
    try {
      return objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValueAsString(object)
        .replace("\\\"", "\"") //"\"2022-11-02T01:57:33\"" -> "2022-11-02T01:57:33"
        .replaceAll("^\"|\"$", "");
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Could not stringify object: "
        + object.getClass().getCanonicalName()
        + ". Reason: " + e.getMessage(), e);
    }
  }
}
