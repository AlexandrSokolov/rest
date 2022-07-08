package com.savdev.rest.client.resteasy.httpclient;

import com.savdev.rest.client.ResteasyProxyClient;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient43Engine;

import javax.ws.rs.client.ClientBuilder;
import java.util.concurrent.TimeUnit;

public class ResteasyHttpClientProvider {

  private static final int CONNECTION_TIMEOUT = (int) TimeUnit.SECONDS.toMillis(5);

  public <T> ResteasyProxyClient<T> provide(
    final String httpBaseUrl,
    final Class<T> jaxRsInterface) {

    //Do not close neither ApacheHttpClient43Engine, nor HttpClientConnectionManager
    //They get closed, by Resteasy implementation during `Client.close()` invocation
    ApacheHttpClient43Engine engine = new ApacheHttpClient43Engine(HttpClients.custom()
      .setConnectionManager(pooledConnectionManager(httpBaseUrl))
      .setDefaultRequestConfig(requestConfig())
      .setKeepAliveStrategy(keepAliveStrategy())
      .build());
    ResteasyClient client = ((ResteasyClientBuilder) ClientBuilder.newBuilder()).httpEngine(engine).build();
    return ResteasyProxyClient.builder(httpBaseUrl, jaxRsInterface)
      .client(client)
      .build();

  }

  private HttpClientConnectionManager pooledConnectionManager(final String httpBaseUrl){
    //`BasicHttpClientConnectionManager` is an alternative, not pooled implementation
    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    connectionManager.setMaxTotal(5); //the total number of connections
    //the maximum number of connections per (any) route
    connectionManager.setDefaultMaxPerRoute(4);
    //the maximum number of connections per a single, specific route, by default 2:

    HttpRoute httpRoute = new HttpRoute(HttpHost.create(httpBaseUrl));
    connectionManager.setMaxPerRoute(httpRoute, 3);

    connectionManager.setSocketConfig(httpRoute.getTargetHost(),
      SocketConfig.custom().setSoTimeout(CONNECTION_TIMEOUT).build());

    //HttpClient to check if the connection is stale before executing a request
    connectionManager.setValidateAfterInactivity(CONNECTION_TIMEOUT);

    return connectionManager;
  }

  private ConnectionKeepAliveStrategy keepAliveStrategy() {
    return (response, context) -> {
      //we apply default keep alive strategy if the header present
      //and if not present, set 5 seconds
      long keepAliveDuration = DefaultConnectionKeepAliveStrategy.INSTANCE.getKeepAliveDuration(response, context);
      return keepAliveDuration == -1 ? CONNECTION_TIMEOUT : keepAliveDuration;
    };
  }

  private RequestConfig requestConfig() {
    return RequestConfig.custom()
      .setConnectionRequestTimeout(CONNECTION_TIMEOUT)
      .setConnectTimeout(CONNECTION_TIMEOUT)
      .setSocketTimeout(CONNECTION_TIMEOUT)
      //.setStaleConnectionCheckEnabled(true) - deprecated, instead used:
      //PoolingHttpClientConnectionManager.setValidateAfterInactivity
      .build();
  }
}
