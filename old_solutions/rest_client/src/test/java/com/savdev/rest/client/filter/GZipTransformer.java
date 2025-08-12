package com.savdev.rest.client.filter;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class GZipTransformer extends ResponseTransformer {

  public static final String NAME = "gzip";

  @Override
  public boolean applyGlobally() {
    return false;
  }

  public String getName() {
    return NAME;
  }

  @Override
  public Response transform(Request request, Response response, FileSource fileSource, Parameters parameters) {
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
         GZIPOutputStream gzipOut = new GZIPOutputStream(baos)) {
      final byte[] body = response.getBody();
      gzipOut.write(body);
      gzipOut.close();
      return new Response.Builder()
        .headers(response.getHeaders()
          .plus(HttpHeader.httpHeader(
            javax.ws.rs.core.HttpHeaders.CONTENT_ENCODING, "gzip")))
        .body(baos.toByteArray())
        .build();
    } catch (final IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
