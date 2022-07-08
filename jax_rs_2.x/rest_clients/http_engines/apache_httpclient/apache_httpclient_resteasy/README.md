
The default implementation of RESTEasy's `ClientHttpEngine` interface is `ApacheHttpClient43Engine`, 
which uses version 4.3 of the HttpClient from the Apache HttpComponents project.

[Full configuration of `ApacheHttpClient43Engine`](src/main/java/com/savdev/rest/client/resteasy/httpclient/ResteasyHttpClientProvider.java) 

Features:

- [Setting connection pool](#setting-connection-pool)
- [Setting connection timeout](#setting-connection-timeout)
- [Setting keep alive strategy](#setting-keep-alive-strategy)
- [Connection Eviction](#connection-eviction)

### Setting connection pool

See `ResteasyHttpClientProvider#pooledConnectionManager`

### Setting connection timeout

See `ResteasyHttpClientProvider#requestConfig` 
and via configuration of connection manager:

```java
connectionManager.setSocketConfig(
  httpRoute.getTargetHost(),
  SocketConfig.custom().setSoTimeout(CONNECTION_TIMEOUT).build());
```

### Setting Keep Alive strategy

See `ResteasyHttpClientProvider#keepAliveStrategy`

### Connection Eviction

Relying on the `HttpClient` to check if the connection is stale before executing a request. 