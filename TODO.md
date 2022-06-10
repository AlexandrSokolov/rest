### http engine configuration, options and features


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

### Caching

How can I cache/reuse the resource?
https://stackoverflow.com/questions/54428593/in-java-how-to-check-that-autocloseable-close-has-been-called

This approach may not work if the lifecycle of a resource is longer than action. E.g. it is necessary for the client to keep the resource for a longer time.

Then you might want to dive deep in ReferenceQueue/Cleaner (since Java 9) and related API.
https://docs.oracle.com/javase/8/docs/api/java/lang/ref/ReferenceQueue.html
https://docs.oracle.com/javase/9/docs/api/java/lang/ref/Cleaner.html


Some pools in Java:

HikariCP is a JDBC connection pool library
OkHttps ConnectionPool
Tomcat JDBC Connection pool
ThreadPoolExecutor
When implementing a pool several questions are raised:

When the resource actually should be closed?
How the resource should be shared between multiple threads?

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