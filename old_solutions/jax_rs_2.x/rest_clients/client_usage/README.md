When you want to use a rest client, you must consider the following topics

- [Example of the usage of rest client](src/test/java/com/savdev/rest/client/ResteasyProxyClientUsageTest.java)
- [Understand the difference between "releasing" a connection and "closing" a connection.](#releasing-a-connection-vs-closing-a-connection)
- [The right using of `javax.ws.rs.client.Client`](#javaxwsrsclientclient-using) 

### "releasing" a connection vs "closing" a connection.

Releasing a connection makes it available for reuse. Closing a connection frees its resources and makes it unusable.
Again, releasing a connection only makes it available for another use. It does not normally close the socket.

[Full examples](src/test/java/com/savdev/rest/client/ResteasyProxyClientUsageTest.java)

**Closing a connection:** 

invocation of `close` on a `javax.ws.rs.client.Client`:
```java
  Client client = ...
  ...
  client.close();
```

**Releasing a connection:** 

invocation of `close` on a `javax.ws.rs.core.Response` and
invocation of `close` on `InputStream` if it is used:
```java
    try (Client client = ...){
      try (Response response = restApiProxy.get();
        InputStream is = response.readEntity(InputStream.class)) {
        ...
      }
    }
```

### `javax.ws.rs.client.Client` using

1. Creating of Client is a heavy operation:
```text
Clients are heavy-weight objects that manage the client-side communication infrastructure. 
Initialization as well as disposal of a Client instance may be a rather expensive operation. 
It is therefore advised to construct only a small number of Client instances in the application. 
```
2. Be careful with `Client` sharing. The spec does not guarantee `Client` to be thread safe.

The implementations do not say about thread safety neither.
You need to analyse the backends of the implementation to clarify it.
For instance Resteasy uses `ApacheHttpClient43Engine` http engine with thread safe [`PoolingHttpClientConnectionManager`](../http_engines/connection_pool/README.md)

3. Client must be closed
```text
Client instances must be properly closed before being disposed to avoid leaking resources.
```
3. Do not try to wrap Client creation inside a helper method like this:
```java
  public Client myReusedClient() {
    Client client = ...
    //configure client with http engine, filters, object mapper, etc.
    return client;
  }
```
Consumers of this client cannot close it in this case. You'll get a memory leak.