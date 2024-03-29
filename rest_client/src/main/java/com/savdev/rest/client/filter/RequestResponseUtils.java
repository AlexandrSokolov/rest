package com.savdev.rest.client.filter;

import com.google.common.collect.Sets;

import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class RequestResponseUtils {


  private static final Set<String> TEXT_CONTENT_HEADERS = Sets.newHashSet(
    MediaType.APPLICATION_JSON,
    MediaType.APPLICATION_XML,
    MediaType.TEXT_PLAIN,
    MediaType.TEXT_XML,
    MediaType.TEXT_HTML);

  public static String extractResponseBody(
    int debugLogEntrySize, final ClientResponseContext responseContext){
    try {
      //it could be `Transfer-Encoding: chunked` with no `Content Length` header
      //the following check might not pass:
      //if (responseContext.getLength() > 0 && responseContext.hasEntity())
      if (isTextualResponse(responseContext)
        && ( responseContext.hasEntity()
          || Optional.ofNullable(responseContext.getEntityStream()).isPresent())) {

        InputStream is = isGzip(responseContext) ?
          extractedFromGzipAndClearEncoding(responseContext.getEntityStream()) : responseContext.getEntityStream();

        is = is.markSupported() ? is : new BufferedInputStream(is);

        responseContext.setEntityStream(is);
        if (isGzip(responseContext)) {
          //after extracting input stream, we reset content encoding
          responseContext.getHeaders().remove(HttpHeaders.CONTENT_ENCODING);
        }

        final Charset responseCharset =
          responseContext.getMediaType() != null
            && responseContext.getMediaType().getParameters() != null
            && responseContext.getMediaType().getParameters().containsKey("charset") ?
            Charset.forName(responseContext.getMediaType().getParameters().get("charset"))
            : StandardCharsets.UTF_8;
        return extractBody(debugLogEntrySize, is, responseCharset);

      } else {
        return null;
      }
    } catch (Exception e) {
      throw new IllegalStateException("Could not extract information from response body", e);
    }
  }

  private static String extractBody(
    final int debugLogEntrySize,
    final InputStream stream,
    final Charset charset) {
    try {
      StringBuilder b = new StringBuilder();
      stream.mark(debugLogEntrySize + 1);
      final byte[] entity = new byte[debugLogEntrySize + 1];
      final int entitySize = stream.read(entity);
      if (entitySize != -1) {
        b.append(new String(entity, 0, Math.min(entitySize, debugLogEntrySize), charset));
        if (entitySize > debugLogEntrySize) {
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
      && responseContext.getHeaders().containsKey(HttpHeaders.CONTENT_ENCODING)
      && responseContext.getHeaders().getFirst(HttpHeaders.CONTENT_ENCODING).equalsIgnoreCase("gzip");
  }

  private static InputStream extractedFromGzipAndClearEncoding(final InputStream gzippedStream) {
    try {
      return new GZIPDecodingInterceptor.FinishableGZIPInputStream(gzippedStream);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  private static boolean isTextualResponse(final ClientResponseContext responseContext){
    return responseContext.getHeaders() != null
      && responseContext.getHeaders().containsKey(HttpHeaders.CONTENT_TYPE)
      && TEXT_CONTENT_HEADERS.stream().anyMatch(header ->
      responseContext.getHeaders()
        .getFirst(HttpHeaders.CONTENT_TYPE)
        .toLowerCase(Locale.getDefault())
        .startsWith(header));
  }

}
