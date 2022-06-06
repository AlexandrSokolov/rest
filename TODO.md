### closing the connection

5.7. Closing connections
The underlying connections are opened for each request and closed after the response is received and entity is processed (entity is read). See the following example:

final WebTarget target = ... some web target
Response response = target.path("resource").request().get();
System.out.println("Connection is still open.");
System.out.println("string response: " + response.readEntity(String.class));
System.out.println("Now the connection is closed.");

If you don't read the entity, then you need to close the response manually by response.close(). 
Also if the entity is read into an InputStream (by response.readEntity(InputStream.class)), 
the connection stays open until you finish reading from the InputStream. 
In that case, the InputStream or the Response should be closed manually at the end of reading from InputStream.



### Connection pool configuration

[With resteasy](https://www.baeldung.com/resteasy-client-tutorial)

### Http Engine, what does it allow to configure
```java
ResteasyClient client = (ResteasyClient)ClientBuilder.newClient();
client.httpEngine();
```
See [connection pool configuration](https://www.baeldung.com/resteasy-client-tutorial)

Supported by:
[Resteasy ClientHttpEngine implementations](https://docs.jboss.org/resteasy/docs/5.0.3.Final/userguide/html_single/index.html#transport_layer)

Not supported by:
- Jax RS api
- Jersey Rest Api

### Async invocation
```java
ResteasyClient client = (ResteasyClient)ClientBuilder.newClient();
client.asyncInvocationExecutor()

```

[See also example](http://www.mastertheboss.com/jboss-frameworks/resteasy/resteasy-client-api-tutorial/)

### Sending requests in chunked mode
```java
ResteasyClient client = (ResteasyClient)ClientBuilder.newClient();
ResteasyWebTarget target = client.target(url)
  .setChunked(true);
```

### Scheduling requests
```java
ResteasyClient client = (ResteasyClient)ClientBuilder.newClient();
client.getScheduledExecutor();
```