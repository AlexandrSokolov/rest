package com.savdev.rest.client;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.function.Supplier;

import static com.savdev.rest.client.RestClientDefaults.ERROR_HEADER;

public class JaxRsExceptionsUtils {

  public static <T> Optional<T> ignoreNotFound(Supplier<T> supplier) {

    try {
      return Optional.of(supplier.get());
    } catch (Exception e) {
      Optional<WebApplicationException> maybeWebException = causeWebException(e);
      if (maybeWebException.isPresent()) {
        if (maybeWebException.get().getResponse().getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
          return Optional.empty();
        }
      }
      throw e;
    }
  }

  public static Optional<WebApplicationException> causeWebException(final Exception e) {
    if (e instanceof WebApplicationException){
      return Optional.of((WebApplicationException) e);
    } else if (e.getCause() != null
      //to avoid stackoverflow:
      && e != e.getCause().getCause()){
      return causeWebException((Exception) e.getCause());
    } else {
      return Optional.empty();
    }
  }

  public static String errorFromResponse(final Response response) {
    return maybeErrorFromResponse(response)
      .orElseThrow(() -> new IllegalStateException("Not nullable instance of RequestResponseInfo is expected."));
  }

  public static Optional<String> maybeErrorFromResponse(final Response response){
    if (response.getHeaders() != null
      && response.getHeaders().containsKey(ERROR_HEADER)) {
      return Optional.of(
        response
          .getHeaders()
          .getFirst(ERROR_HEADER)
          .toString());
    } else {
      return Optional.empty();
    }
  }
}
