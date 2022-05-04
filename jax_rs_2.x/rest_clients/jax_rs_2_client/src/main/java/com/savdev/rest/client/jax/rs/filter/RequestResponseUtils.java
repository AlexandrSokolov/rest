package com.savdev.rest.client.jax.rs.filter;

import javax.ws.rs.client.ClientResponseContext;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class RequestResponseUtils {

  private static final int DEFAULT_MAX_ENTITY_SIZE = 8 * 1024;

  public static String extractResponseBody(
    final ClientResponseContext responseContext){
    try {
      //it could be `Transfer-Encoding: chunked` with no `Content Length` header
      //the following check might not pass:
      //if (responseContext.getLength() > 0 && responseContext.hasEntity())
      if (responseContext.hasEntity()
        || Optional.ofNullable(responseContext.getEntityStream()).isPresent()) {

        final InputStream isMarkSupported = responseContext.getEntityStream().markSupported() ?
          responseContext.getEntityStream() : new BufferedInputStream(responseContext.getEntityStream());

        final InputStream is = isGzip(responseContext) ?
          extractedFromGzipAndClearEncoding(isMarkSupported) : isMarkSupported;

        responseContext.setEntityStream(is);
        if (isGzip(responseContext)) {
          //after extracting input stream, we reset content encoding
          responseContext.getHeaders().remove("Content-Encoding");
        }

        final Charset responseCharset =
          responseContext.getMediaType() != null
            && responseContext.getMediaType().getParameters() != null
            && responseContext.getMediaType().getParameters().containsKey("charset") ?
            Charset.forName(responseContext.getMediaType().getParameters().get("charset"))
            : StandardCharsets.UTF_8;
        return extractBody(is, responseCharset);

      } else {
        return null;
      }
    } catch (Exception e) {
      throw new IllegalStateException("Could not extract information from response body", e);
    }
  }

  private static String extractBody(
    final InputStream stream,
    final Charset charset) {
    try {
      StringBuilder b = new StringBuilder();
      stream.mark(DEFAULT_MAX_ENTITY_SIZE + 1);
      final byte[] entity = new byte[DEFAULT_MAX_ENTITY_SIZE + 1];
      final int entitySize = stream.read(entity);
      if (entitySize != -1) {
        b.append(new String(entity, 0, Math.min(entitySize, DEFAULT_MAX_ENTITY_SIZE), charset));
        if (entitySize > DEFAULT_MAX_ENTITY_SIZE) {
          b.append("...more...");
        }
      }
      stream.reset();
      return b.toString();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  private static boolean isGzip(final ClientResponseContext responseContext) {
    return responseContext.getHeaders() != null
      && responseContext.getHeaders().containsKey("Content-Encoding")
      && responseContext.getHeaders().getFirst("Content-Encoding").equalsIgnoreCase("gzip");
  }

  private static InputStream extractedFromGzipAndClearEncoding(final InputStream gzippedStream) {
    try {
      return new GZIPDecodingInterceptor.FinishableGZIPInputStream(gzippedStream);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

}
