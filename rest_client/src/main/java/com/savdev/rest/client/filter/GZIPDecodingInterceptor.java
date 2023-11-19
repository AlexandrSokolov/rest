package com.savdev.rest.client.filter;

import javax.annotation.Priority;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

@Provider
@Priority(4000)
public class GZIPDecodingInterceptor implements ReaderInterceptor {
  public GZIPDecodingInterceptor() {
  }

  public Object aroundReadFrom(ReaderInterceptorContext context) {
    try {
      Object encoding = context.getHeaders().getFirst("Content-Encoding");
      if (encoding != null && encoding.toString().equalsIgnoreCase("gzip")) {
        InputStream old = context.getInputStream();
        FinishableGZIPInputStream is = new FinishableGZIPInputStream(old);
        context.setInputStream(is);

        Object var5;
        try {
          var5 = context.proceed();
        } finally {
          if (!context.getType().equals(InputStream.class)) {
            is.finish();
          }

          context.setInputStream(old);
        }

        return var5;
      } else {
        return context.proceed();
      }
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static class FinishableGZIPInputStream extends GZIPInputStream {
    public FinishableGZIPInputStream(InputStream is) throws IOException {
      super(is);
    }

    public void finish() {
      this.inf.end();
    }
  }
}
