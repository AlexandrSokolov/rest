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